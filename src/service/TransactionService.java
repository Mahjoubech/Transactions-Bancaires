package service;

import dao.CompteRepository;
import dao.TransactionRepository;
import entity.Compte;
import entity.Transaction;
import enums.typeTransaction;
import util.DateUtil;
import util.Helper;
import util.Validateur;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionService implements TransactionServiceInterface {
    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;

    public TransactionService(TransactionRepository transactionRepository, CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void ajoute(double amount, String idCompte, typeTransaction type, String lieu) {
        try {
            String accountId = Helper.generateTransactionCode();
            if (!Validateur.isPositiveAmount(amount)) {
                throw new IllegalArgumentException("Montant doit être positif !");
            }
            Date date = new Date();
            if (type == null) {
                throw new IllegalArgumentException("Type de transaction requis !");
            }
            Compte compte = compteRepository.findById(idCompte)
                    .orElseThrow(() -> new IllegalArgumentException("Compte introuvable !"));
            if (type.equals(typeTransaction.RETRAIT)) {
                if (compte.getSolde() < amount) {
                    throw new IllegalArgumentException("Solde insuffisant pour le retrait !");
                }
                compte.setSolde(compte.getSolde() - amount);
                compteRepository.update(compte);
            } else if (type.equals(typeTransaction.VERSEMENT)) {
                compte.setSolde(compte.getSolde() + amount);
                compteRepository.update(compte);
            } else if (type.equals(typeTransaction.VIREMENT)) {
                Validateur.validateVirementDetails(lieu);
                String[] detailParts = lieu.split("->");
                String sourceAccountId = detailParts[0].trim();
                String destinationAccountId = detailParts[1].trim();
                Compte sourceCompte = compteRepository.findById(sourceAccountId)
                        .orElseThrow(() -> new IllegalArgumentException("Compte source introuvable !"));
                Compte destinationCompte = compteRepository.findById(destinationAccountId)
                        .orElseThrow(() -> new IllegalArgumentException("Compte destination introuvable !"));
                if (sourceCompte.getSolde() < amount) {
                    throw new IllegalArgumentException("Solde insuffisant pour le virement !");
                }
                sourceCompte.setSolde(sourceCompte.getSolde() - amount);
                destinationCompte.setSolde(destinationCompte.getSolde() + amount);
                compteRepository.update(sourceCompte);
                compteRepository.update(destinationCompte);
            }
            Transaction t = new Transaction(accountId, date, amount, idCompte, type, lieu);
            transactionRepository.create(t);
            System.out.println("Transaction ajoutée avec succès !");
        } catch (Exception e) {
            System.out.println("Error adding transaction: " + e.getMessage());
        }
    }

    @Override
    public Optional<Transaction> findById(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("L'ID du Transaction ne peut pas être vide !");
            }

            return transactionRepository.findById(id);

        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche du compte : " + e.getMessage());
            return Optional.empty();
        }
    }

    ;

    @Override
    public List<Transaction> getAll() {
        try {
            List<Transaction> transactions = transactionRepository.findAll();
            if (transactions.isEmpty()) {
                throw new IllegalArgumentException("Aucune transaction trouvée !");
            }
            return transactions;
        } catch (Exception e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
            return List.of();
        }

    }

    @Override
    public List<Transaction> findByMontant(Double montant) {
        try {
            if (montant == null || montant <= 0) {
                throw new IllegalArgumentException("Le montant doit être positif !");
            }
            return transactionRepository.findAll().stream()
                    .filter(t -> t.montant() == montant).toList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findByType(typeTransaction type) {
        try {
            if (type == null) {
                throw new IllegalArgumentException("Le type de transaction ne peut pas être nul !");
            }
            return transactionRepository.findAll().stream()
                    .filter(t -> t.type() == type).toList();
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
    public List<Transaction> findByDate(Date date) {
        try {
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
}