package model.entity;
import model.enums.typeCompte;

public final class CompteEpargne extends Compte {
    private double tauxInteret;
    public CompteEpargne(String id, String num, String idClient, double solde, typeCompte typeCompte , double tauxInteret) {
        super(id, num, idClient, solde, typeCompte);
        this.tauxInteret = tauxInteret;
    }
    public double getTauxInteret() { return tauxInteret; }
    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }
}