package ui;

import model.entity.Client;
import model.entity.CompteCourant;
import model.entity.CompteEpargne;
import model.enums.typeCompte;
import service.ClientService;
import service.interfaces.ClientServiceInterface;
import service.interfaces.CompteServiceInterface;
import util.Helper;

import java.util.List;
import java.util.Scanner;

public class ClientMenu {
    private final Scanner sc = new Scanner(System.in);
   private final ClientServiceInterface clientService;
   private final CompteServiceInterface compteService;
    public ClientMenu(ClientServiceInterface clientService , CompteServiceInterface compteService) {
        this.clientService = clientService;
        this.compteService = compteService;
    }
    public  void affiche() {
        while (true) {
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║        === Menu Client ===            ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║ 1. Create Clients and compte          ║");
            System.out.println("║ 2. Update Client                      ║");
            System.out.println("║ 3. Delete Client                      ║");
            System.out.println("║ 4. Search for client                  ║");
            System.out.println("║ 5. get all client                     ║");
            System.out.println("║ 6. search for clien ( nom or email )  ║");
            System.out.println("║ 0. Quitter                            ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("Choisissez une option : ");
            String choixStr = sc.nextLine();

            switch (choixStr) {
                case "1":
                    addClient();
                    break;
                case "2":
                    updateClient();
                    break;
                case "3":
                    deleteClient();
                    break;
                case "4":
                    searchById();
                    break;
                case "5":
                    afficheClients();
                    break;
                case "6":
                    searchByNameOrEmail();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    public void afficheClients(){
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé.");
            return;
        }
     System.out.printf("%-3s | %-36s | %-18s | %-10s\n",
             "#", "ID", "Nom Service", "Statut");
     System.out.println("-----------------------------------------------------------------------------------------------");
     int i = 1;
     for (Client client : clients) {
            System.out.printf("%-3d | %-36s | %-18s | %-10s\n",
                    i, client.id(), client.nom(), client.email());
            i++;
     }

 }
    public  void addClient() {
        try {
            System.out.print("Enter ur name : ");
            String nom = sc.nextLine();

            System.out.print("Enter ur email : ");
            String email = sc.nextLine();
            Client c = clientService.create(nom, email);
            System.out.println("Client created successfully.");
            System.out.println("Votre Id est : " + c.id());

            System.out.print("Enter Votre Solde  : ");
            double solde = Double.parseDouble(sc.nextLine());
            String idClient = c.id();
            System.out.println("Choisir type de compte : 1.COURANT  2.EPARGNE");
            int choix = sc.nextInt();
            if (choix == 1) {
                System.out.print("Entrer le découvert autorisé : ");
                double decouvert = sc.nextDouble();
                compteService.create(idClient, solde, typeCompte.COURANT, decouvert, 0);
            } else if (choix == 2) {
                System.out.print("Entrer le taux d'intérêt : ");
                double taux = sc.nextDouble();
                compteService.create(idClient, solde, typeCompte.EPARGNE, 0, taux);
            }else {
                System.out.println("Type de compte invalide.");
            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

    }
    public  void updateClient() {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé.");
            return;
        }
        afficheClients();
        System.out.println("Entrez le numéro du client :");
        String numStr = sc.nextLine();
        int num;
        try {
            num = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            System.out.println("Erreur de saisie : veuillez entrer un numéro.");
            return;
        }
        if (num < 1 || num > clients.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        Client abn = clients.get(num - 1);
        System.out.println("Modification de Client id : " + abn.id());
        System.out.println("Nouveau nom du client (laisser vide pour inchangé) :");
        String newNom = sc.nextLine();
        System.out.println("Nouveau email (laisser vide pour inchangé) :");
        String newEmail = sc.nextLine();
        try{
            String id = abn.id();
            String nom = newNom.isEmpty() ? abn.nom() : newNom;
            String email = newEmail.isEmpty() ? abn.email() : newEmail;
            clientService.update(id, nom, email);
            System.out.println("Client mis à jour avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du client : " + e.getMessage());
        }
    }
    public  void deleteClient() {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé.");
            return;
        }
        afficheClients();
        System.out.println("Entrez le numéro du client :");
        String numStr = sc.nextLine();
        int num;
        try {
            num = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            System.out.println("Erreur de saisie : veuillez entrer un numéro.");
            return;
        }
        if (num < 1 || num > clients.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        Client abn = clients.get(num - 1);
            clientService.delete(abn);
            System.out.println("Client supprimé avec succès.");
    }
    public void searchById() {
        System.out.print("Entrez l'ID du client : ");
        String id = sc.nextLine();

        clientService.findById(id).ifPresentOrElse(
                client -> System.out.printf("ID: %s | Nom: %s | Email: %s%n", client.id(), client.nom(), client.email()),
                () -> System.out.println("Client introuvable.")
        );
    }
    public void searchByNameOrEmail() {
        System.out.print("Entrez un nom ou un email : ");
        String input = sc.nextLine();

        // d'abord chercher par email
        clientService.getByEmail(input).ifPresentOrElse(
                client -> System.out.printf("ID: %s | Nom: %s | Email: %s%n", client.id(), client.nom(), client.email()),
                () -> {
                    // sinon chercher par nom
                    List<Client> clients = clientService.getByNom(input);
                    if (clients.isEmpty()) {
                        System.out.println("Aucun client trouvé.");
                    } else {
                        clients.forEach(c -> System.out.printf("ID: %s | Nom: %s | Email: %s%n", c.id(), c.nom(), c.email()));
                    }
                }
        );
    }

}
