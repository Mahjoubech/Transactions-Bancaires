package dao.repository;
import dao.interfaces.ClientRepository;
import dataeBase.ConectionDB;
import model.entity.Client;

import  java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepoImp implements ClientRepository {
     private Connection conn;
     public ClientRepoImp(Connection conn) {
             this.conn = conn ;
     }

     @Override
    public void create(Client c) {
      String request = "insert into client (id , nom , email ) values (? , ? , ? )";
      try (var ps = conn.prepareStatement(request)) {
          ps.setString(1 , c.id());
          ps.setString(2, c.nom());
          ps.setString(3 , c.email());
          ps.executeUpdate();
      }catch (SQLException ex){
          throw new RuntimeException(ex);
      };
     }
     @Override
    public void update(Client c) {
        String request = "update client set nom = ? , email = ? where id = ? ";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1, c.nom());
            ps.setString(2 , c.email());
            ps.setString(3 , c.id());
            ps.executeUpdate();
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        };
     }
     @Override
    public void delete(String id) {
        String request = "delete from client where id = ? ";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1 , id);
            ps.executeUpdate();
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        };
     }
     @Override
     public Optional <Client> findById(String id) {
         String request = "select * from client where id = ? ";
         try (var ps = conn.prepareStatement(request)) {
             ps.setString(1 , id);
             var rs = ps.executeQuery();
             if(rs.next()){
                 Client c = new Client(
                         rs.getString("id"),
                         rs.getString("nom"),
                         rs.getString("email")
                 );
                 return Optional.of(c);
             }
         }catch (SQLException ex){
             throw new RuntimeException(ex);
         };
         return Optional.empty();
     }
     @Override
    public List<Client> findAll() {
         String request = "select * from client";
         try (var ps = conn.prepareStatement(request)) {
             var rs = ps.executeQuery();
             var clients = new ArrayList<Client>();
             while(rs.next()){
                 clients.add(new Client(
                         rs.getString("id"),
                         rs.getString("nom"),
                         rs.getString("email")
                 ));
             }
             return clients;
         }catch (SQLException ex){
             throw new RuntimeException(ex);
         }
     }
     @Override
     public List<Client> findByName(String nom) {
         String request = "select * from client where nom = ?";
         List<Client> clients = new ArrayList<>();
         try (var ps = conn.prepareStatement(request)) {
             ps.setString(1, nom);
             var rs = ps.executeQuery();
             while (rs.next()) {
                 clients.add(new Client(
                         rs.getString("id"),
                         rs.getString("nom"),
                         rs.getString("email")
                 ));
             }
         } catch (SQLException ex) {
             throw new RuntimeException(ex);
         }
         return clients;
     }

    @Override
  public Optional <Client> findByEmail(String email) {
        String request = "select * from client where email = ? ";
        try (var ps = conn.prepareStatement(request)) {
            ps.setString(1 , email);
            var rs = ps.executeQuery();
            if(rs.next()){
                return Optional.of(new Client(
                        rs.getString("id"),
                        rs.getString("nom"),
                        rs.getString("email")
                ));
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        };
        return Optional.empty();
     }

}
