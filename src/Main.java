import java.sql.Connection;

import dao.ClientRepository;
import dao.CompteRepository;
import dao.TransactionRepository;
import dao.ClientRepoImp;
import dao.CompteRepoImp;
import dao.TransactionRepoImp;
import dataBase.ConectionDB;
import service.ClientService;
import service.CompteService;
import service.TransactionService;
import service.ClientServiceInterface;
import service.CompteServiceInterface;
import service.TransactionServiceInterface;
import ui.ClientMenu;
import ui.CreationCompteMenu;
import ui.Menu;
import ui.TransactionMenu;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn  = ConectionDB.getInstance().getConnection();
            ClientRepository clientRepo = new ClientRepoImp(conn);
            ClientServiceInterface clientService = new ClientService(clientRepo);
            CompteRepository comptetRepo = new CompteRepoImp(conn);
            CompteServiceInterface compteService = new CompteService(comptetRepo);
            TransactionRepository transactionRepo = new TransactionRepoImp(conn);
            TransactionServiceInterface transactionService = new TransactionService(transactionRepo , comptetRepo);
            ClientMenu hh = new ClientMenu(clientService , compteService);
            CreationCompteMenu kk = new CreationCompteMenu( compteService);
            TransactionMenu tt = new TransactionMenu(transactionService);
            Menu JJ = new Menu( hh, kk , tt);
            JJ.afficheMenu();
            System.out.println("Test de connexion réussi !");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}