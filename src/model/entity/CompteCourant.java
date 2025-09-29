package model.entity;
import model.enums.typeCompte;
public final class CompteCourant extends Compte {
    private double decouvert;
   public  CompteCourant(String id, String num, String idClient, double solde, typeCompte typeCompte ,double decouvert ) {
       super(id, num, idClient, solde, typeCompte);
       this.decouvert = decouvert;

   }
   public double getDecouvert() {
         return decouvert;
   }
    public void setDecouvert(double decouvert) {
            this.decouvert = decouvert;
    }
}