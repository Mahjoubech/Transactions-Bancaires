package ui;

import model.entity.Client;
import model.entity.Compte;
import model.entity.CompteCourant;
import model.entity.CompteEpargne;
import model.enums.typeCompte;
import org.w3c.dom.ls.LSOutput;
import service.interfaces.CompteServiceInterface;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CreationCompteMenu {
    private final Scanner sc = new Scanner(System.in);
    private final CompteServiceInterface compteService;

    public CreationCompteMenu(CompteServiceInterface compteService) {
        this.compteService = compteService;
    }

    public void afficheCompte() {
        while (true) {
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║        === Menu Compte ===            ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║ 1. Create un Compte                   ║");
            System.out.println("║ 2. Update Compte                      ║");
            System.out.println("║ 3. Delete Compte                      ║");
            System.out.println("║ 4. Search Compte par ID               ║");
            System.out.println("║ 5. Afficher tous les comptes          ║");
            System.out.println("║ 6.  Trouver le compte avec le solde   ║");
            System.out.println("║     maximum ou minimum.               ║");
            System.out.println("║ 0. Quitter                            ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("Choisissez une option : ");
            String choixStr = sc.nextLine();

            switch (choixStr) {
                case "1" -> addCompte();
                case "2" -> updateCompte();
                case "3" -> deleteCompte();
                case "4" -> searchCompte();
                case "5" -> afficheComptes();
                case "6" -> {
                    System.out.println("1. Compte avec le solde maximum          2. Compte avec le solde minimum");

                    int n = Integer.parseInt(sc.nextLine());
                    if (n == 1) {
                        getCompteMaxSolde();
                    } else if (n == 2) {
                        getCompteMinSolde();
                    } else {
                        System.out.println("Option invalide.");
                    }
                }
                case "0" -> { return; }
                default -> System.out.println("Option invalide.");
            }
        }
    }

    private void addCompte() {
        List<Compte> comptes = compteService.getAllClomptes();
        if (comptes.isEmpty()) {
            System.out.println("Aucun compte disponible pour la mise à jour.");
            return;
        }
        afficheComptes();
        System.out.println("Entrez le numéro du client :");
        String numStr = sc.nextLine();
        int num;
        try{
            num = Integer.parseInt(numStr);
        }catch (NumberFormatException e) {
            System.out.println("Erreur de saisie : veuillez entrer un numéro.");
            return;
        }
        if (num < 1 || num > comptes.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        try {
            Compte compte = comptes.get(num - 1);
            System.out.print("Client ID pour Ajouter Nouveau Compte : " + compte.getIdClient() +"\n");
            String idClient = compte.getIdClient();
            System.out.print("Entrez le solde initial : ");
            double solde = Double.parseDouble(sc.nextLine());

            System.out.println("Choisir type de compte : 1.COURANT  2.EPARGNE");
            int choix = Integer.parseInt(sc.nextLine());

            if (choix == 1) {
                System.out.print("Entrer le découvert autorisé : ");
                double decouvert = Double.parseDouble(sc.nextLine());
                compteService.create(idClient, solde, typeCompte.COURANT, decouvert, 0);
            } else if (choix == 2) {
                System.out.print("Entrer le taux d'intérêt : ");
                double taux = Double.parseDouble(sc.nextLine());
                compteService.create(idClient, solde, typeCompte.EPARGNE, 0, taux);
            } else {
                System.out.println("Type de compte invalide.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du compte : " + e.getMessage());
        }
    }

    private void updateCompte() {
        List<Compte> comptes = compteService.getAllClomptes();
        if (comptes.isEmpty()) {
            System.out.println("Aucun compte disponible pour la mise à jour.");
            return;
        }
        afficheComptes();
        System.out.println("Entrez le numéro du client :");
        String numStr = sc.nextLine();
        int num;
        try{
            num = Integer.parseInt(numStr);
        }catch (NumberFormatException e) {
            System.out.println("Erreur de saisie : veuillez entrer un numéro.");
            return;
        }
        if (num < 1 || num > comptes.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        try {
            Compte compte = comptes.get(num - 1);
            System.out.print("Modification de Compte id : "+ compte.getId() +"\n");
            System.out.print("Nouveau solde : ");
            double solde = Double.parseDouble(sc.nextLine());

            System.out.println("Nouveau type de compte : 1.COURANT 2.EPARGNE");
            int choix = Integer.parseInt(sc.nextLine());

            if (choix == 1) {
                System.out.print("Nouveau découvert : ");
                double decouvert = Double.parseDouble(sc.nextLine());
                compteService.update(compte.getId(), solde, typeCompte.COURANT, decouvert, 0);
            } else if (choix == 2) {
                System.out.print("Nouveau taux d'intérêt : ");
                double taux = Double.parseDouble(sc.nextLine());
                compteService.update(compte.getId(), solde, typeCompte.EPARGNE, 0, taux);
            } else {
                System.out.println("Type de compte invalide.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    private void deleteCompte() {
        List<Compte> comptes = compteService.getAllClomptes();
        if (comptes.isEmpty()) {
            System.out.println("Aucun compte disponible pour la mise à jour.");
            return;
        }
        afficheComptes();
        System.out.println("Entrez le numéro du client :");
        String numStr = sc.nextLine();
        int num;
        try{
            num = Integer.parseInt(numStr);
        }catch (NumberFormatException e) {
            System.out.println("Erreur de saisie : veuillez entrer un numéro.");
            return;
        }
        if (num < 1 || num > comptes.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        try {
            Compte compte = comptes.get(num - 1);
            System.out.print("Supprimer la Compte ID : " +compte.getId() +"\n" );
            String idCompte = compte.getId();
            compteService.delete(idCompte);
            System.out.println("Compte supprimé avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    private void searchCompte() {
        try {
            System.out.print("Entrez l'ID du compte : ");
            String id = sc.nextLine();
            compteService.findById(id).ifPresentOrElse(
                    compte -> System.out.printf("ID: %s | Num: %s | Solde: %.2f | Type: %s%n",
                            compte.getId(), compte.getNum(), compte.getSolde(), compte.getTypeCompte()),
                    () -> System.out.println("Compte introuvable.")
            );
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche : " + e.getMessage());
        }
    }

    public void afficheComptes() {
        try {
            List<Compte> comptes = compteService.getAllClomptes();
            if (comptes.isEmpty()) {
                System.out.println("Aucun compte trouvé.");
                return;
            }

            System.out.printf("%-3s | %-36s | %-12s | %-10s | %-20s | %-36s\n",
                    "#", "ID Compte", "Type", "Solde", "Specifique", "ID Client");
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            int i = 1;
            for (Compte compte : comptes) {
                try {
                    String type = (compte.getTypeCompte() != null) ? compte.getTypeCompte().toString() : "N/A";
                    String specificField = "";

                    if (compte instanceof CompteCourant) {
                        specificField = "Découvert: " + ((CompteCourant) compte).getDecouvert();
                    } else if (compte instanceof CompteEpargne) {
                        specificField = "Taux: " + ((CompteEpargne) compte).getTauxInteret();
                    }

                    System.out.printf("%-3d | %-36s | %-12s | %-10.2f | %-20s | %-36s\n",
                            i, compte.getId(), type, compte.getSolde(), specificField, compte.getIdClient());
                    i++;
                } catch (Exception e) {
                    System.out.println("Erreur lors de l'affichage d'un compte : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des comptes : " + e.getMessage());
        }
    }
    public Optional<Compte> getCompteMinSolde(){
        List<Compte> comptes = compteService.getAllClomptes();
        if (comptes.isEmpty()) {
            return Optional.empty();
        }
        System.out.println(comptes.stream()
                .min((c1 ,c2)->Double.compare(c1.getSolde(), c2.getSolde())));
        return Optional.empty();

    }
    public Optional<Compte> getCompteMaxSolde(){
        List<Compte> comptes = compteService.getAllClomptes();
        if (comptes.isEmpty()) {
            return Optional.empty();
        }
        System.out.println(comptes.stream()
                .max((c1 ,c2)->Double.compare(c1.getSolde(), c2.getSolde())));
        return Optional.empty();

    }



}
