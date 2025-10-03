import java.sql.Connection;

import dao.ClientRepository;
import dao.CompteRepository;
import dao.TransactionRepository;
import dao.ClientRepoImp;
import dao.CompteRepoImp;
import dao.TransactionRepoImp;
import dataBase.ConectionDB;
import service.*;
import ui.*;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn  = ConectionDB.getInstance().getConnection();
            ClientRepository clientRepo = new ClientRepoImp(conn);
            ClientServiceInterface clientService = new ClientService(clientRepo);
            CompteRepository comptetRepo = new CompteRepoImp(conn);
            CompteServiceInterface compteService = new CompteService(comptetRepo);
            TransactionRepository transactionRepo = new TransactionRepoImp(conn);
            RapportServiceInterface rapportService = new RapportService( comptetRepo ,clientRepo);
            TransactionServiceInterface transactionService = new TransactionService(transactionRepo , comptetRepo);
            ClientMenu hh = new ClientMenu(clientService , compteService);
            CreationCompteMenu kk = new CreationCompteMenu( compteService);
            TransactionMenu tt = new TransactionMenu(transactionService);
            RapportMenu rr = new RapportMenu(rapportService);
            Menu JJ = new Menu( hh, kk , tt , rr);
            JJ.afficheMenu();
            System.out.println("Test de connexion réussi !");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}