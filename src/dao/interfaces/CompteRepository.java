package dao.interfaces;

import model.entity.Compte;

import java.util.List;

public interface CompteRepository extends Crud<Compte> {
    List<Compte> findByClientEmail(String email);
    List<Compte> findByClientNom(String nom);
    List<Compte> findByClientid(String idClient);
    double getTotalSoldeByClient(String clientId);
    int countByClient(String clientId);
};