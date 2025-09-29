package entity;
import util.Validateur;
public class CompteCourant extends Compte {
    private double decouvert;
    public CompteCourant(String code, double solde, double decouvert) {
        super(code, solde);
        if (!Validateur.isPositiveDouble(decouvert)) throw new IllegalArgumentException("Découvert doit être positif !");
        this.decouvert = decouvert;
    }
    public double getDecouvert() { return decouvert; }
    @Override
    public void retirer(double montant, String destination) throws Exception {
        if (!Validateur.isPositiveAmount(montant)) throw new Exception("Montant négatif ou nul !");
        if (solde - montant < -decouvert) throw new Exception("Solde insuffisant (dépassement du découvert autorisé) !");
        ajouterOperation(new Retrait(montant, destination));
        solde -= montant;
    }
    @Override
    public double calculerInteret() { return 0; }
    @Override
    public void afficherDetails() {
        System.out.println(util.ConsoleColor.colorize(util.ConsoleColor.BLUE, "CompteCourant [" + code + "] Solde: " + solde + " Découvert: " + decouvert));
        System.out.println(util.ConsoleColor.colorize(util.ConsoleColor.CYAN, "Historique opérations:"));
        listeOperations.forEach(op -> System.out.println(util.ConsoleColor.colorize(util.ConsoleColor.YELLOW, op.toString())));
    }
}