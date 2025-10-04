package service;

import dao.ClientRepository;
import dao.CompteRepository;
import dao.TransactionRepository;
import entity.Client;
import entity.Compte;
import entity.Transaction;
import enums.typeTransaction;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class RapportService implements RapportServiceInterface {
    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    public RapportService(CompteRepository compteRepository, ClientRepository clientRepository, TransactionRepository transactionRepository) {
        this.compteRepository = compteRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
    }

    public double totalSolde(String idClient){
        List<Compte> cmp = compteRepository.findAll().
                stream().
                filter(c -> c.getIdClient().equals(idClient)).
                toList();
        return cmp.stream()
                .mapToDouble(Compte::getSolde)
                .sum();
    }
    @Override
    public List<Client>  topFiveClientsSolde(){
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .sorted((a , b)->Double.compare(totalSolde(b.id()) ,totalSolde(a.id())))
                .limit(5)
                .collect(Collectors.toList());
    }
    @Override
    public void rapportMensuel(int year, int month) {
        try{
            if(month < 1 || month > 12){
                throw new IllegalArgumentException("Mois invalide. Veuillez entrer un mois entre 1 et 12.");
            }
            if(year < 1900 || year > 2100){
                throw new IllegalArgumentException("Année invalide. Veuillez entrer une année entre 1900 et 2100.");
            }
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        try{
            List<Transaction> transactions = transactionRepository.findByMonth(year, month)
                    .stream()
                    .filter(t -> t.type() != null)
                    .toList();

            Map<typeTransaction, Long> countByType = transactions.stream()
                    .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));

            Map<typeTransaction, Double> totalByType = transactions.stream()
                    .collect(Collectors.groupingBy(Transaction::type, Collectors.summingDouble(Transaction::montant)));
            System.out.println("\n=== Rapport Mensuel (" + month + "/" + year + ") ===");
            System.out.printf("%-15s %-15s %-15s\n", "Type", "Nombre", "Volume Total");
            System.out.println("--------------------------------------------------");
            for (typeTransaction type : countByType.keySet()) {
                long count = countByType.get(type);
                double total = totalByType.getOrDefault(type, 0.0);
                System.out.printf("%-15s %-15d %-15.2f\n", type, count, total);
            }
        }catch (Exception e){
            System.out.println("Erreur lors de la génération du rapport: " + e.getMessage());
        }
    }
    @Override
    public List<Transaction> detecterTransactionsSuspectes(double seuilMontant, String paysHabituel, int maxTransactionsParMinute) {
        List<Transaction> suspectes = new ArrayList<>();
        try {
            List<Transaction> toutesTransactions = transactionRepository.findAll();

            for (Transaction t : toutesTransactions) {
                boolean suspect = false;

                if (t.montant() > seuilMontant) {
                    suspect = true;
                }

                if (t.lieu() != null) {
                    String lieu = t.lieu().trim();
                    if (!lieu.equalsIgnoreCase(paysHabituel)) {
                        suspect = true;
                    }
                }

                long count = toutesTransactions.stream()
                        .filter(trx -> trx.idCompte().equalsIgnoreCase(t.idCompte())
                                && trx.date().equals(t.date()))
                        .count();
                if (count > maxTransactionsParMinute) {
                    suspect = true;
                }

                if (suspect) {
                    suspectes.add(t);
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de la détection des transactions suspectes: " + e.getMessage());
        }
        return suspectes;
    }
    @Override
    public List<String> comptesInactifs(int periodeJours) {
        List<String> inactifs = new ArrayList<>();
        Date maintenant = new Date();

        for (Compte c : compteRepository.findAll()) {
            try {
                Optional<Transaction> derniereTx = transactionRepository.findAll().stream()
                        .filter(t -> t.idCompte().equalsIgnoreCase(c.getId()))
                        .max(Comparator.comparing(Transaction::date));

                if (derniereTx.isEmpty()) {
                    inactifs.add(c.getId());
                } else {
                    Date dateTx = derniereTx.get().date(); // java.sql.Date ou java.util.Date
                    long diffMillis = maintenant.getTime() - dateTx.getTime();
                    long diffJours = diffMillis / (1000 * 60 * 60 * 24);

                    if (diffJours >= periodeJours) {
                        inactifs.add(c.getId());
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur pour le compte " + c.getId() + ": " + e.getMessage());
            }
        }

        return inactifs;
    }


}
