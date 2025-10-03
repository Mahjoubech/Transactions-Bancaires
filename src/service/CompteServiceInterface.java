package service;

import entity.Compte;
import enums.typeCompte;

import java.util.List;
import java.util.Optional;

public interface CompteServiceInterface {
    void create(String idClient, double solde, typeCompte typeCompte , double decouvert , double taux);
    void update(String id, double solde, typeCompte typeCompte, double decouvert, double taux);
    void delete(String id);
    Optional<Compte> findById(String id);
    List<Compte> getAllClomptes();
//    List<Compte> getByIdClient(String id);


}
