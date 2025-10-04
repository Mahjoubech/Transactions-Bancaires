package service;

import entity.Client;
import entity.Transaction;

import java.util.List;

public interface RapportServiceInterface {
    List<Client> topFiveClientsSolde();

    double totalSolde(String idClient);

    void rapportMensuel(int year, int month);
    List<Transaction> detecterTransactionsSuspectes(double seuil, String paysHabituel, int maxTransactionsParMinute);
    List<String> comptesInactifs(int periodeJours);

}
