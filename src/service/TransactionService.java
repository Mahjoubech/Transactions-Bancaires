package service;

import dao.ClientRepository;
import dao.CompteRepository;
import dao.TransactionRepository;
import entity.Client;
import entity.Compte;
import entity.Transaction;
import enums.typeTransaction;
import util.DateUtil;
import util.Helper;
import util.Validateur;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionService implements TransactionServiceInterface {
    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    public TransactionService(TransactionRepository transactionRepository , CompteRepository compteRepository , ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.compteRepository = compteRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void ajoute(double amount, String idCompte, typeTransaction type, String lieu, String destination) {
        try {
            String accountId = Helper.generateTransactionCode();

            if (!Validateur.isPositiveAmount(amount)) {
                throw new IllegalArgumentException("Montant doit être positif !");
            }

            if (type == null) {
                throw new IllegalArgumentException("Type de transaction requis !");
            }

            Date date = new Date();

            Compte compte = compteRepository.findById(idCompte)
                    .orElseThrow(() -> new IllegalArgumentException("Compte introuvable !"));
            if (type.equals(typeTransaction.RETRAIT)) {
                if (compte.getSolde() < amount) {
                    throw new IllegalArgumentException("Solde insuffisant pour le retrait !");
                }
                compte.setSolde(compte.getSolde() - amount);
                compteRepository.update(compte);

                Transaction t = new Transaction(accountId, date, amount, idCompte, type, lieu);
                transactionRepository.create(t);
                System.out.println("✅ Retrait effectué avec succès !");

            }
            else if (type.equals(typeTransaction.VERSEMENT)) {
                compte.setSolde(compte.getSolde() + amount);
                compteRepository.update(compte);

                Transaction t = new Transaction(accountId, date, amount, idCompte, type, lieu);
                transactionRepository.create(t);
                System.out.println("✅ Versement effectué avec succès !");

            }
            else if (type.equals(typeTransaction.VIREMENT)) {
                Compte sourceCompte = compteRepository.findById(idCompte)
                        .orElseThrow(() -> new IllegalArgumentException("Compte source introuvable !"));

                Compte destinationCompte = compteRepository.findById(destination)
                        .orElseThrow(() -> new IllegalArgumentException("Compte destination introuvable !"));
                if (sourceCompte.getSolde() < amount) {
                    throw new IllegalArgumentException("Solde insuffisant pour le virement !");
                }
                sourceCompte.setSolde(sourceCompte.getSolde() - amount);
                destinationCompte.setSolde(destinationCompte.getSolde() + amount);

                compteRepository.update(sourceCompte);
                compteRepository.update(destinationCompte);
                String relation = sourceCompte.getId() + "->" + destinationCompte.getId();
                Transaction tSource = new Transaction(
                        Helper.generateTransactionCode(),
                        date,
                        amount,
                        sourceCompte.getId(),
                        typeTransaction.VIREMENT,
                        relation
                );
                transactionRepository.create(tSource);
                Transaction tDest = new Transaction(
                        Helper.generateTransactionCode(),
                        date,
                        amount,
                        destinationCompte.getId(),
                        typeTransaction.VIREMENT,
                        relation
                );
                transactionRepository.create(tDest);

                System.out.println("✅ Virement effectué avec succès de "
                        + sourceCompte.getIdClient() + " vers " + destinationCompte.getIdClient());
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'ajout de la transaction : " + e.getMessage());
        }
    }
    @Override
    public Optional<Transaction> findById(String id){
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("L'ID du Transaction ne peut pas être vide !");
            }

            return transactionRepository.findById(id);

        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche du compte : " + e.getMessage());
            return Optional.empty();
        }
    };
    @Override
    public List<Transaction> getAll(){
        try{
            List<Transaction> transactions = transactionRepository.findAll();
            if(transactions.isEmpty()){
                throw new IllegalArgumentException("Aucune transaction trouvée !");
            }
            return  transactions;
        }catch(Exception e){
            System.out.println("Error fetching transactions: " + e.getMessage());
            return List.of();
        }

    }
    @Override
    public List<Transaction> findByMontant(Double montant){
        try{
            if (montant == null || montant <= 0) {
                throw new IllegalArgumentException("Le montant doit être positif !");
            }
            return transactionRepository.findAll().stream()
                    .filter(t->t.montant() == montant).toList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Transaction> findByType(typeTransaction type){
        try{
            if (type == null) {
                throw new IllegalArgumentException("Le type de transaction ne peut pas être nul !");
            }
            return transactionRepository.findAll().stream()
                    .filter(t->t.type() == type).toList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Transaction> findByLieu(String lieu) {
        try {
            if (lieu == null || lieu.isBlank()) {
                throw new IllegalArgumentException("Le lieu ne peut pas être vide !");
            }

            String lieuSearch = lieu.trim().toLowerCase();

            return transactionRepository.findAll().stream()
                    .filter(t -> t.lieu() != null && t.lieu().toLowerCase().contains(lieuSearch))
                    .toList();

        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche par lieu: " + e.getMessage());
            return List.of();
        }
    }
    @Override
    public List<Transaction> findByDate(Date date){
        try{
            if (date == null) {
                throw new IllegalArgumentException("Date ne peut pas être nulle !");
            }
            return transactionRepository.findAll().stream()
                    .filter(t -> DateUtil.isSameDay(t.date(), date)) // نستخدم isSameDay
                    .toList();
        } catch (Exception e) {
            System.out.println("Erreur findByDate: " + e.getMessage());
            return List.of();
        }
    }
    @Override
    public void findByCompte(String idCompte) {
        try {
            if (idCompte == null || idCompte.isBlank()) {
                throw new IllegalArgumentException("ID du compte ne peut pas être vide !");
            }
            List<Transaction> allTransactions = transactionRepository.findAll();
            Set<String> displayedTx = new HashSet<>();
            Compte compte = compteRepository.findById(idCompte)
                    .orElseThrow(() -> new IllegalArgumentException("Compte introuvable !"));
            System.out.println("\n=== Transactions du compte " + idCompte + " ===");
            System.out.println("Solde actuel: " + compte.getSolde() + " MAD");
            System.out.println("-----------------------------------");
            for (Transaction t : allTransactions) {
                if (displayedTx.contains(t.id())) continue;
                String message = "";
                String couleur = "\u001B[0m";
                switch (t.type()) {
                    case RETRAIT -> {
                        if (t.idCompte().equalsIgnoreCase(idCompte)) {
                            couleur = "\u001B[31m";
                            message = String.format("- %.2f MAD", t.montant());
                            displayedTx.add(t.id());
                        }
                    }
                    case VERSEMENT -> {
                        if (t.idCompte().equalsIgnoreCase(idCompte)) {
                            couleur = "\u001B[32m";
                            message = String.format("+ %.2f MAD", t.montant());
                            displayedTx.add(t.id());
                        }
                    }
                    case VIREMENT -> {
                        String sourceId = null;
                        String destId = null;

                        if (t.lieu() != null && t.lieu().contains("->")) {
                            String[] parts = t.lieu().split("->");
                            sourceId = parts[0].trim();
                            destId = parts[1].trim();
                        } else {
                            sourceId = t.idCompte();
                        }

                        Compte source = (sourceId != null) ? compteRepository.findById(sourceId).orElse(null) : null;
                        Compte dest = (destId != null) ? compteRepository.findById(destId).orElse(null) : null;

                        Client clientSource = (source != null)
                                ? clientRepository.findById(source.getIdClient()).orElse(null)
                                : null;
                        Client clientDest = (dest != null)
                                ? clientRepository.findById(dest.getIdClient()).orElse(null)
                                : null;

                        if (sourceId != null && sourceId.equalsIgnoreCase(idCompte)) {
                            couleur = "\u001B[31m";
                            message = String.format("- %.2f MAD → %s (%s)",
                                    t.montant(),
                                    clientDest != null ? clientDest.nom() : "Client inconnu",
                                    destId != null ? destId : "");
                            displayedTx.add(t.id());
                        } else if (destId != null && destId.equalsIgnoreCase(idCompte)) {
                            couleur = "\u001B[32m";
                            message = String.format("+ %.2f MAD ← %s (%s)",
                                    t.montant(),
                                    clientSource != null ? clientSource.nom() : "Client inconnu",
                                    sourceId != null ? sourceId : "");
                            displayedTx.add(t.id());
                        } else if (sourceId != null && destId != null) {
                            couleur = "\u001B[33m";
                            message = String.format("VIREMENT : %.2f MAD de %s vers %s",
                                    t.montant(),
                                    clientSource != null ? clientSource.nom() : "Client inconnu",
                                    clientDest != null ? clientDest.nom() : "Client inconnu");
                            displayedTx.add(t.id());
                        }
                    }
                }

                if (!message.isEmpty())
                    System.out.println(couleur + message + "\u001B[0m");
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche par compte: " + e.getMessage());
        }
    }
    @Override
    public Map<typeTransaction, List<Transaction>> groupByType() {
        return transactionRepository.findAll().stream()
                .collect(Collectors.groupingBy(Transaction::type));
    }
    @Override
    public Map<String, List<Transaction>> groupByDate() {
        return transactionRepository.findAll().stream()
                .collect(Collectors.groupingBy(t -> DateUtil.formatDate(t.date())));
    }
    @Override
    public double totalByCompte(String idCompte) {
        return transactionRepository.findAll().stream()
                .filter(t -> t.idCompte().equalsIgnoreCase(idCompte))
                .mapToDouble(Transaction::montant)
                .sum();
    }
    @Override
    public double moyenneByCompte(String idCompte) {
        return transactionRepository.findAll().stream()
                .filter(t -> t.idCompte().equalsIgnoreCase(idCompte))
                .mapToDouble(Transaction::montant)
                .average()
                .orElse(0.0);
    }
    @Override
    public double totalByClient(String idClient) {
        // خاصك هنا يكون عندك relation client -> comptes
        List<Compte> comptes = compteRepository.findAll().stream()
                .filter(c -> c.getIdClient().equalsIgnoreCase(idClient))
                .toList();

        return transactionRepository.findAll().stream()
                .filter(t -> comptes.stream().anyMatch(c -> c.getId().equals(t.idCompte())))
                .mapToDouble(Transaction::montant)
                .sum();
    }
    @Override
    public double moyenneByClient(String idClient) {
        List<Compte> comptes = compteRepository.findAll().stream()
                .filter(c -> c.getIdClient().equalsIgnoreCase(idClient))
                .toList();

        return transactionRepository.findAll().stream()
                .filter(t -> comptes.stream().anyMatch(c -> c.getId().equals(t.idCompte())))
                .mapToDouble(Transaction::montant)
                .average()
                .orElse(0.0);
    }
    @Override
    public List<Transaction> detecterTransactionsSuspectes(double seuilMontant, String idClient) {
        // 1. récupérer tous les comptes du client
        List<Compte> comptes = compteRepository.findAll().stream()
                .filter(c -> c.getIdClient().equalsIgnoreCase(idClient))
                .toList();

        List<Transaction> transactionsClient = transactionRepository.findAll().stream()
                .filter(t -> comptes.stream().anyMatch(c -> c.getId().equals(t.idCompte())))
                .toList();

        // 2. filtrer par montant élevé
        List<Transaction> suspectes = transactionsClient.stream()
                .filter(t -> t.montant() >= seuilMontant)
                .toList();

        // 3. détecter lieux inhabituels
        // ex: si le client n'a jamais utilisé ce lieu
        List<String> lieuxConnus = transactionsClient.stream()
                .map(Transaction::lieu)
                .distinct()
                .toList();

        List<Transaction> lieuxInhabituels = transactionsClient.stream()
                .filter(t -> !lieuxConnus.contains(t.lieu()))
                .toList();

        // 4. détecter fréquence excessive: +3 transactions par jour
        List<Transaction> frequencesExcessives = transactionsClient.stream()
                .collect(Collectors.groupingBy(
                        t -> DateUtil.formatDate(t.date()),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .filter(e -> e.getValue() > 3)
                .flatMap(e -> transactionsClient.stream()
                        .filter(t -> DateUtil.formatDate(t.date()).equals(e.getKey()))
                )
                .toList();

        // fusionner toutes les transactions suspectes (sans doublons)
        return Stream.of(suspectes, lieuxInhabituels, frequencesExcessives)
                .flatMap(List::stream)
                .distinct()
                .toList();
    }

}
