package service;

import entity.Transaction;
import enums.typeTransaction;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionServiceInterface {
    void ajoute(double amount, String idCompte, typeTransaction type, String lieu , String description);
    Optional<Transaction> findById(String id);
    List<Transaction> findByMontant(Double montant);
    List<Transaction> findByDate(Date dt);
    List<Transaction> getAll();
    List<Transaction> findByType(typeTransaction type);
    List<Transaction> findByLieu(String lieu);
    void findByCompte(String idCompte);
    Map<typeTransaction, List<Transaction>> groupByType();
    Map<String, List<Transaction>> groupByDate();
    double totalByCompte(String idCompte);
    double moyenneByCompte(String idCompte);
    double totalByClient(String idClient);
    double moyenneByClient(String idClient);
    List<Transaction> detecterTransactionsSuspectes(double seuilMontant, String idClient);

}
