package entity;
import util.Validateur;

public class CompteEpargne extends Compte {
    private double tauxInteret;
    public CompteEpargne(String code, double solde, double tauxInteret) {
        super(code, solde);
        if (!Validateur.isPositiveDouble(tauxInteret)) throw new IllegalArgumentException("Taux doit être positif !");
        this.tauxInteret = tauxInteret;
    }
    public double getTauxInteret() { return tauxInteret; }
    @Override
    public void retirer(double montant, String destination) throws Exception {
        if (!Validateur.isPositiveAmount(montant)) throw new Exception("Montant négatif ou nul !");
        if (solde < montant) throw new Exception("Solde insuffisant !");
        ajouterOperation(new Retrait(montant, destination));
        solde -= montant;
    }
    @Override
    public double calculerInteret() { return solde * tauxInteret / 100.0; }
    @Override
    public void afficherDetails() {
        System.out.println(util.ConsoleColor.colorize(util.ConsoleColor.MAGENTA, "CompteEpargne [" + code + "] Solde: " + solde + " Taux: " + tauxInteret + "%"));
        System.out.println(util.ConsoleColor.colorize(util.ConsoleColor.CYAN, "Historique opérations:"));
        listeOperations.forEach(op -> System.out.println(util.ConsoleColor.colorize(util.ConsoleColor.YELLOW, op.toString())));
    }
}