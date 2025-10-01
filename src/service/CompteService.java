package service;

import dao.interfaces.ClientRepository;
import dao.interfaces.CompteRepository;
import model.entity.Compte;
import model.entity.CompteCourant;
import model.entity.CompteEpargne;
import model.enums.typeCompte;
import service.interfaces.CompteServiceInterface;
import util.Helper;
import util.Validateur;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;

public class CompteService implements CompteServiceInterface {
    private final CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @Override
    public void create(String idClient, double solde, typeCompte typeCompte, double decouvert, double taux) {
        try {
            String id = Helper.generateCompteCode();
            if (!Validateur.isValidCompteCode(id))
                throw new IllegalArgumentException("Format code compte invalide ! (CPT - XXXXX)");
            String num = Helper.generateCompteNum();
            if (!Validateur.isPositiveAmount(solde))
                throw new IllegalArgumentException("Solde doit être positif !");
            if (decouvert < 0)
                throw new IllegalArgumentException("Découvert doit être positif !");
            if (taux < 0)
                throw new IllegalArgumentException("Taux doit être positif !");

            if (typeCompte == typeCompte.COURANT) {
                if (taux != 0)
                    throw new IllegalArgumentException("Un compte courant ne peut pas avoir de taux d'intérêt !");
                // création CompteCourant
                CompteCourant compte = new CompteCourant(id, num, idClient, solde, typeCompte, decouvert);
                compteRepository.create(compte);

            } else if (typeCompte == typeCompte.EPARGNE) {
                if (decouvert != 0)
                    throw new IllegalArgumentException("Un compte épargne ne peut pas avoir de découvert !");
                // création CompteEpargne
                CompteEpargne compte = new CompteEpargne(id, num, idClient, solde, typeCompte, taux);
                compteRepository.create(compte);

            } else {
                throw new IllegalArgumentException("Type de compte inconnu !");
            }

            System.out.println("Compte créé avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du compte : " + e.getMessage());
        }
    }

    public void update(String id, double solde, typeCompte typeCompte, double decouvert, double taux) {
        try {
            Compte compte = compteRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Compte introuvable avec id: " + id));

            // Validations
            if (!Validateur.isPositiveAmount(solde))
                throw new IllegalArgumentException("Solde doit être positif !");

            if (decouvert < 0)
                throw new IllegalArgumentException("Découvert doit être positif !");

            if (taux < 0)
                throw new IllegalArgumentException("Taux doit être positif !");

            // Mise à jour selon le type
            if (typeCompte == typeCompte.COURANT) {
                if (taux != 0)
                    throw new IllegalArgumentException("Un compte courant ne peut pas avoir de taux d'intérêt !");
                if (compte instanceof CompteCourant cc) {
                    cc.setSolde(solde);
                    cc.setTypeCompte(typeCompte);
                    cc.setDecouvert(decouvert);
                } else {
                    // Transformer en CompteCourant si besoin
                    compte = new CompteCourant(compte.getId(), compte.getNum(), compte.getIdClient(), solde, typeCompte, decouvert);
                }
            } else if (typeCompte == typeCompte.EPARGNE) {
                if (decouvert != 0)
                    throw new IllegalArgumentException("Un compte épargne ne peut pas avoir de découvert !");
                if (compte instanceof CompteEpargne ce) {
                    ce.setSolde(solde);
                    ce.setTypeCompte(typeCompte);
                    ce.setTauxInteret(taux);
                } else {
                    // Transformer en CompteEpargne si besoin
                    compte = new CompteEpargne(compte.getId(), compte.getNum(), compte.getIdClient(), solde, typeCompte, taux);
                }
            } else {
                throw new IllegalArgumentException("Type de compte inconnu !");
            }

            compteRepository.update(compte);
            System.out.println("Compte mis à jour avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du compte : " + e.getMessage());
        }
    }
   @Override
    public void delete(String id) {
        try {
            Compte compte = compteRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Compte introuvable avec id: " + id));
            compteRepository.delete(id);
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression du compte : " + e.getMessage());
        }
    }
    @Override
    public Optional<Compte> findById(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("L'ID du compte ne peut pas être vide !");
            }

            return compteRepository.findById(id);

        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche du compte : " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Compte> getAllClomptes() {
        try {
            List<Compte> comptes = compteRepository.findAll();

            if (comptes.isEmpty()) {
                System.out.println("Aucun compte trouvé !");
            }

            return comptes;

        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des comptes : " + e.getMessage());
            return List.of(); // retourne une liste vide en cas d'erreur
        }
    }






}
