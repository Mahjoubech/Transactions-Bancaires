package ui;

import entity.Client;
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
            System.out.println("1.top 5 des clients par solde.");
            System.out.println("2. Liste des transactions d'un compte");
            System.out.println("3. Retour au menu principal");
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
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }
}
