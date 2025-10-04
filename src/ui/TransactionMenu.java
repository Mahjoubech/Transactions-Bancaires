package ui;

import entity.Transaction;
import enums.typeTransaction;
import service.TransactionServiceInterface;
import util.DateUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TransactionMenu {

    private final Scanner sc = new Scanner(System.in);
        private final TransactionServiceInterface transactionService;

        public TransactionMenu(TransactionServiceInterface transactionService) {
            this.transactionService = transactionService;
        }

        public void afficherMenu() {

            int choix;

            do {
                System.out.println("\n===== MENU Transaction =====");
                System.out.println("1. Ajouter transaction");
                System.out.println("2. Afficher toutes les transactions");
                System.out.println("3. Rechercher transaction par : ");
                System.out.println("4. regrouper par :");
                System.out.println("5. Total/Moyenne par compte");
                System.out.println("6. Total/Moyenne par client");
                System.out.println("7. Détecter transactions suspectes");
                System.out.println("0. Quitter");
                System.out.print("Votre choix: ");
                choix = sc.nextInt();
                sc.nextLine();

                switch (choix) {
                    case 1:
                        ajouterTransaction();
                        break;
                    case 2:
                         afficheAllTransactions();
                         break;
                    case 3:
                        System.out.println("1: Mpntant, 2: Date, 3: type, 4:Lieu : , 5: Compte");
                        int chx = sc.nextInt();
                        sc.nextLine();
                        switch (chx) {
                            case 1: searchByMontant(); ; break;
                            case 2: searchByDate(); break;
                            case 3: searchByType(); break;
                            case 4: searchByLieu(); break;
                            case 5: searchByCompte(); break;
                            default:
                                System.out.println("Choix invalide !");
                        }
                          break;
                    case 4:
                        System.out.println("1: Type, 2: Date : ");
                        int chxx = sc.nextInt();
                        sc.nextLine();
                        switch (chxx) {
                            case 1: groupByType(); ; break;
                            case 2: groupByDate(); break;

                        }
                        break;
                    case 5:
                        totalMoyenneParCompte();
                        break;
                    case 6:
                        totalMoyenneParClient();
                        break;
                    case 7 :
                        detecterSuspectes();
                        break;
                    case 0:
                        System.out.println("Au revoir!");
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } while (choix != 2);
        }

//        private void ajouterTransaction() {
//            try {
//                System.out.print("Id du compte: ");
//                String idCompte = sc.nextLine();
//
//                System.out.print("Montant: ");
//                double montant = sc.nextDouble();
//                sc.nextLine();
//                System.out.println("Type de transaction (1: RETRAIT, 2: VERSEMENT, 3: VIREMENT) : ");
//                int typeChoix = sc.nextInt();
//                sc.nextLine();
//
//                typeTransaction type = null;
//                switch (typeChoix) {
//                    case 1: type = typeTransaction.RETRAIT; break;
//                    case 2: type = typeTransaction.VERSEMENT; break;
//                    case 3: type = typeTransaction.VIREMENT; break;
//                }
//
//                String lieu = "";
//                if (type == typeTransaction.VIREMENT) {
//                    System.out.print("Entrez les détails du virement (source->destination): ");
//                    lieu = sc.nextLine();
//                } else {
//                    System.out.print("Lieu: ");
//                    lieu = sc.nextLine();
//                }
//
//                transactionService.ajoute( montant, idCompte, type, lieu);
//            } catch (Exception e) {
//                System.out.println("Erreur: " + e.getMessage());
//            }
//        }
private void ajouterTransaction() {
    try {
        System.out.print("Id du compte: ");
        String idCompte = sc.nextLine();

        System.out.print("Montant: ");
        while (!sc.hasNextDouble()) {
            System.out.println("Veuillez entrer un nombre valide !");
            sc.next();
        }
        double montant = sc.nextDouble();
        sc.nextLine();
        System.out.print("Lieu: ");
        String lieu = sc.nextLine();

        System.out.println("Type de transaction (1: RETRAIT, 2: VERSEMENT, 3: VIREMENT) : ");
        while (!sc.hasNextInt()) {
            System.out.println("Veuillez entrer un nombre valide !");
            sc.next();
        }
        int typeChoix = sc.nextInt();
        sc.nextLine();

        typeTransaction type = null;
        switch (typeChoix) {
            case 1: type = typeTransaction.RETRAIT; break;
            case 2: type = typeTransaction.VERSEMENT; break;
            case 3: type = typeTransaction.VIREMENT; break;
            default:
                System.out.println("Type invalide !");
                return;
        }

        String destination = "";
        if (type == typeTransaction.VIREMENT) {
            System.out.print("Entrez les détails du virement destination: ");
            destination = sc.nextLine();
        }

        transactionService.ajoute(montant, idCompte, type, lieu , destination);
    } catch (Exception e) {
        System.out.println("Erreur: " + e.getMessage());
        sc.nextLine(); // nettoyer buffer en cas d'erreur
    }
}
        private void afficheAllTransactions() {
            try {
                List<Transaction> transactions = transactionService.getAll();
                System.out.printf("%-3s | %-36s | %-12s | %-10s | %-30s | %-36s\n",
                        "#", "ID Transaction", "Type", "Montant", "Lieu", "ID Compte");
                System.out.println("-----------------------------------------------------------------------------------------------------------------------");
                int i = 1;
                for (Transaction t : transactions) {
                    try {
                        System.out.printf("%-3d | %-36s | %-12s | %-10.2f | %-30s | %-36s\n",
                                i,
                                t.id(),
                                t.type().toString(),
                                t.montant(),
                                t.lieu(),
                                t.idCompte());
                        i++;
                    } catch (Exception e) {
                        System.out.println("Erreur lors de l'affichage d'une transaction : " + e.getMessage());
                    }
                }
            }catch (Exception e){
                System.out.println("Erreur: " + e.getMessage());
            }
        }
        private void searchByMontant() {
        try {
            System.out.print("Entrez le montant à rechercher : ");
            double mt = sc.nextDouble();
            sc.nextLine();

            List<Transaction> transactions = transactionService.findByMontant(mt);

            if(transactions.isEmpty()) {
                System.out.println("Aucune transaction trouvée pour ce montant.");
                return;
            }

            System.out.printf("%-3s | %-36s | %-12s | %-10s | %-20s | %-36s\n",
                    "#", "ID Transaction", "Type", "Montant", "Lieu", "ID Compte");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------");
            int i = 1;
            for (Transaction t : transactions) {
                try {
                    System.out.printf("%-3d | %-36s | %-12s | %-10.2f | %-20s | %-36s\n",
                            i,
                            t.id(),
                            t.type().toString(),
                            t.montant(),
                            t.lieu(),
                            t.idCompte());
                    i++;
                } catch (Exception e) {
                    System.out.println("Erreur lors de l'affichage d'une transaction : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
        private void searchByDate() {
            System.out.print("Entrez la date (yyyy-MM-dd) : ");
            String input = sc.nextLine();
            try {
                Date date = DateUtil.parseDate(input);
                List<Transaction> results = transactionService.findByDate(date);
                System.out.printf("%-3s | %-36s | %-12s | %-10s | %-30s | %-36s\n",
                        "#", "ID Transaction", "Type", "Montant", "Lieu", "ID Compte");
                System.out.println("-----------------------------------------------------------------------------------------------------------------------");

                int i = 1;
                for (Transaction t : results) {
                    try {
                        System.out.printf("%-3d | %-36s | %-12s | %-10.2f | %-30s | %-36s\n",
                                i,
                                t.id(),
                                t.type().toString(),
                                t.montant(),
                                t.lieu(),
                                t.idCompte());
                        i++;
                    } catch (Exception e) {
                        System.out.println("Erreur lors de l'affichage d'une transaction : " + e.getMessage());
                    }
                }
            } catch (ParseException e) {
                System.out.println("Format de date invalide. Format attendu : yyyy-MM-dd");
            }}
        private void searchByType() {
    System.out.print("Entrez le Type de Transaction : ");
    String input = sc.nextLine();
    try{
        typeTransaction type = typeTransaction.valueOf(input.toUpperCase());
        List<Transaction> results = transactionService.findByType(type);
        System.out.printf("%-3s | %-36s | %-12s | %-10s | %-30s | %-36s\n",
                "#", "ID Transaction", "Type", "Montant", "Lieu", "ID Compte");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");

        int i = 1;
        for (Transaction t : results) {
            try {
                System.out.printf("%-3d | %-36s | %-12s | %-10.2f | %-30s | %-36s\n",
                        i,
                        t.id(),
                        t.type().toString(),
                        t.montant(),
                        t.lieu(),
                        t.idCompte());
                i++;
            } catch (Exception e) {
                System.out.println("Erreur lors de l'affichage d'une transaction : " + e.getMessage());
            }
        }
}catch (Exception e){
        System.out.println("Erreur: " + e.getMessage());
}
        }
        private  void searchByLieu() {
            System.out.print("Entrez le Lieu de Transaction : ");
            String input = sc.nextLine();
            try{
                List<Transaction> results = transactionService.findByLieu(input);
                System.out.printf("%-3s | %-36s | %-12s | %-10s | %-30s | %-36s\n",
                        "#", "ID Transaction", "Type", "Montant", "Lieu", "ID Compte");
                System.out.println("-----------------------------------------------------------------------------------------------------------------------");

                int i = 1;
                for (Transaction t : results) {
                    try {
                        System.out.printf("%-3d | %-36s | %-12s | %-10.2f | %-30s | %-36s\n",
                                i,
                                t.id(),
                                t.type().toString(),
                                t.montant(),
                                t.lieu(),
                                t.idCompte());
                        i++;
                    } catch (Exception e) {
                        System.out.println("Erreur lors de l'affichage d'une transaction : " + e.getMessage());
                    }
                }
            }catch (Exception e){
                System.out.println("Erreur: " + e.getMessage());
            }
        }
        private void groupByType() {
        var grouped = transactionService.groupByType();
        System.out.println("==== Transactions groupées par type ====");
        grouped.forEach((type, list) -> {
            System.out.println("Type : " + type);
            list.forEach(t -> System.out.printf("ID: %s | Montant: %.2f | Date: %s | Compte: %s\n",
                    t.id(), t.montant(), DateUtil.formatDateTime(t.date()), t.idCompte()));
            System.out.println("------------------------------------");
        });
       }
        private void groupByDate() {
        var grouped = transactionService.groupByDate();
        System.out.println("==== Transactions groupées par date ====");
        grouped.forEach((date, list) -> {
            System.out.println("Date : " + date);
            list.forEach(t -> System.out.printf("ID: %s | Type: %s | Montant: %.2f | Compte: %s\n",
                    t.id(), t.type(), t.montant(), t.idCompte()));
            System.out.println("------------------------------------");
        });
    }
        private void searchByCompte() {
            System.out.print("Entrez votre Id De Compte: ");
            String id = sc.nextLine();
            try {
                transactionService.findByCompte(id);
            } catch (Exception e) {
            }}
        public void totalMoyenneParCompte() {
        System.out.print("Entrez l'ID du compte : ");
        String id = sc.nextLine();
        double total = transactionService.totalByCompte(id);
        double moyenne = transactionService.moyenneByCompte(id);
        System.out.println("Total : " + total);
        System.out.println("Moyenne : " + moyenne);
    }
        public void totalMoyenneParClient() {
        System.out.print("Entrez l'ID du client : ");
        String id = sc.nextLine();
        double total = transactionService.totalByClient(id);
        double moyenne = transactionService.moyenneByClient(id);
        System.out.println("Total : " + total);
        System.out.println("Moyenne : " + moyenne);
    }
        public void detecterSuspectes() {
        System.out.print("Entrez l'ID du client : ");
        String idClient = sc.nextLine();
        System.out.print("Entrez le seuil de montant pour suspicion : ");
        double seuil = sc.nextDouble();
        sc.nextLine();

        List<Transaction> suspectes = transactionService.detecterTransactionsSuspectes(seuil, idClient);
        if(suspectes.isEmpty()){
            System.out.println("Aucune transaction suspecte détectée.");
            return;
        }

        System.out.println("==== Transactions suspectes ====");
        for(Transaction t : suspectes){
            System.out.printf("ID: %s | Compte: %s | Type: %s | Montant: %.2f | Lieu: %5s | Date: %20s\n",
                    t.id(), t.idCompte(), t.type(), t.montant(), t.lieu(), DateUtil.formatDateTime(t.date()));
        }
    }


}
