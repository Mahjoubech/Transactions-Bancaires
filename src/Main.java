import java.sql.Connection;

import dao.interfaces.ClientRepository;
import dao.repository.ClientRepoImp;
import dataeBase.ConectionDB;
import service.ClientService;
import service.interfaces.ClientServiceInterface;
import ui.ClientMenu;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn  = ConectionDB.getInstance().getConnection();
            ClientRepository clientRepo = new ClientRepoImp(conn);
            ClientServiceInterface clientService = new ClientService(clientRepo);
            ClientMenu hh = new ClientMenu(clientService);
            hh.affiche();
            System.out.println("Test de connexion réussi !");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}