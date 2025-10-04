package ui;

import entity.Client;
import entity.Transaction;
import service.RapportServiceInterface;

import java.util.List;
import java.util.Scanner;

public class RapportMenu {
    private final Scanner sc = new Scanner(System.in);
    private final RapportServiceInterface rapportService;

    public RapportMenu(RapportServiceInterface rapportService) {
        this.rapportService = rapportService;
    }
    public void showMenu() {
        while (true) {
            System.out.println("\n--- Rapport Menu ---");
            System.out.println("1. top 5 des clients par solde.");
            System.out.println("2. produire un rapport mensuel des transactions.");
            System.out.println("3. Détecter les transactions suspectes");
            System.out.println("0. Retour au menu principal");
            System.out.print("Choix: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    try{
                        List<Client> topClients = rapportService.topFiveClientsSolde();

                        System.out.println("\n=== Top 5 Clients par Solde ===");
                        System.out.printf("%-10s %-15s  %-15s\n", "ID", "Nom", "Solde Total");
                        System.out.println("-------------------------------------------------------");

                        for (Client c : topClients) {
                            double solde = rapportService.totalSolde(c.id());
                            System.out.printf("%-10s %-15s %-15.2f Dh\n",
                                    c.id(),
                                    c.nom(),
                                    solde);
                        }
                    }catch (Exception e){
                        System.out.println("\nErreur lors de la récupération des données: " + e.getMessage());
                    }
                    break;
                case "2": try {
                        System.out.print("Entrer l'année (ex: 2025): ");
                        int year = Integer.parseInt(sc.nextLine());
                        System.out.print("Entrer le mois (1-12): ");
                        int month = Integer.parseInt(sc.nextLine());
                        rapportService.rapportMensuel(year , month);
                        break;
                    } catch (Exception e) {
                        System.out.println("Erreur: " + e.getMessage());
                    }
                    break;
                case "3":
                    try {
                        System.out.print("Entrer le seuil de montant (ex: 10000): ");
                        double seuil = Double.parseDouble(sc.nextLine());
                        System.out.print("Entrer le pays habituel (ex: Maroc): ");
                        String paysHabituel = sc.nextLine();
                        System.out.print("Entrer le nombre max de transactions par minute: ");
                        int maxTx = Integer.parseInt(sc.nextLine());

                        // Appel du service
                        List<Transaction> suspectes = rapportService.detecterTransactionsSuspectes(seuil, paysHabituel, maxTx);

                        if (suspectes.isEmpty()) {
                            System.out.println("Aucune transaction suspecte détectée.");
                        } else {
                            System.out.println("\n=== Transactions suspectes ===");
                            for (Transaction t : suspectes) {
                                System.out.printf("%s | Compte: %s | Type: %s | Montant: %.2f MAD | Lieu: %s | Date: %s\n",
                                        t.id(),
                                        t.idCompte(),
                                        t.type(),
                                        t.montant(),
                                        t.lieu(),
                                        t.date());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Erreur lors de la détection: " + e.getMessage());
                    }
                    break;
                case "0":
                    System.out.println("Au revoir!");
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }
}
