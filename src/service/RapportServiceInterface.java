package service;

import entity.Client;

import java.util.List;

public interface RapportServiceInterface {
    List<Client> topFiveClientsSolde();

    double totalSolde(String idClient);

    void rapportMensuel(int year, int month);
}
