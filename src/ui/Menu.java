package ui;

import java.util.Scanner;

public class Menu {
    private final Scanner sc = new Scanner(System.in);
    private final ClientMenu clientMenu;
    private final CreationCompteMenu creationCompteMenu;
    private final TransactionMenu transactionMenu;
    public Menu(ClientMenu clientMenu, CreationCompteMenu creationCompteMenu , TransactionMenu transactionMenu) {
        this.clientMenu = clientMenu;
        this.creationCompteMenu = creationCompteMenu;
        this.transactionMenu = transactionMenu;
    }
    public  void afficheMenu(){
        try{

        }catch (Exception e){
            System.out.println("Erreur : " + e.getMessage());
        }
        while (true) {
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║        === Sub for live ===        ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Creer un client et un compte    ║");
            System.out.println("║ 2. creer un compte avec CLI        ║");
            System.out.println("║ 3. Transactions                    ║");
            System.out.println("║ 4. Rapports financiers             ║");
            System.out.println("║ 0. Quitter                         ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("Choisissez une option : ");
            int choix = sc.nextInt();
            switch (choix) {
                case 1 -> clientMenu.affiche();
                case 2 -> creationCompteMenu.afficheCompte();
                case 3 -> transactionMenu.afficherMenu();
//              case "4" -> searchCompte();
//              case "5" -> getAllComptesByClient();
                case 0 -> { return;
                }
                default -> System.out.println("Option invalide.");
            }
        }
    }



}
