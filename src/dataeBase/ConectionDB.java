package dataeBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionDB {
    private static ConectionDB instance;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/Transactions";
    private static final String USER = "root";
    private static final String PASSWORD = "Mahjoub@1230";
    private ConectionDB() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion MySQL établie !");
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException("Erreur de connexion : " + ex.getMessage());
        }
    }

    public static synchronized ConectionDB getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new ConectionDB();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}