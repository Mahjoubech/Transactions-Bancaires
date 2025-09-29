import java.sql.Connection;

import dataeBase.ConectionDB;
public class Main {
    public static void main(String[] args) {
        try {
            ConectionDB db = ConectionDB.getInstance();
            Connection conn = db.getConnection();
            System.out.println("Test de connexion réussi !");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}