import java.sql.Connection;

import dao.interfaces.ClientRepository;
import dao.interfaces.CompteRepository;
import dao.repository.ClientRepoImp;
import dao.repository.CompteRepoImp;
import dataeBase.ConectionDB;
import service.ClientService;
import service.CompteService;
import service.interfaces.ClientServiceInterface;
import service.interfaces.CompteServiceInterface;
import ui.ClientMenu;
import ui.CreationCompteMenu;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn  = ConectionDB.getInstance().getConnection();
            ClientRepository clientRepo = new ClientRepoImp(conn);
            ClientServiceInterface clientService = new ClientService(clientRepo);
            CompteRepository comptetRepo = new CompteRepoImp(conn);
            CompteServiceInterface compteService = new CompteService(comptetRepo);
            ClientMenu hh = new ClientMenu(clientService , compteService);
            CreationCompteMenu kk = new CreationCompteMenu( compteService);
            Menu JJ = new Menu( hh, kk);
            JJ.afficheMenu();
            System.out.println("Test de connexion réussi !");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}