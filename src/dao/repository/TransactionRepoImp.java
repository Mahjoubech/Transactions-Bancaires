package dao.repository;

import dao.interfaces.TransactionRepository;
import model.entity.Transaction;
import model.enums.typeTransaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TransactionRepoImp implements TransactionRepository {
    private static Connection conn;
    public TransactionRepoImp(Connection conn) {
        this.conn = conn;
    }
    @Override
    public void create(Transaction transaction) {
        String request = "INSERT INTO Transaction (id, date, montant, typeTransaction, lieu ,idCompte) VALUES (?, ?, ?, ?, ? ,?)";
        try(var ps = conn.prepareStatement(request)){
            ps.setString(1 ,transaction.id());
            ps.setString(2 ,transaction.date().toString());
            ps.setDouble(3 ,transaction.montant());
            ps.setString(4 ,transaction.type().toString());
            ps.setString(5 ,transaction.lieu());
            ps.setString(6 ,transaction.idCompte());
            ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Transaction transaction) {
        String request = "UPDATE Transaction SET date = ?, montant = ?, typeTransaction = ?, lieu = ? ,idCompte = ? WHERE id = ?";
        try(var ps = conn.prepareStatement(request)){
            ps.setString(1 ,transaction.date().toString());
            ps.setDouble(2 ,transaction.montant());
            ps.setString(3 ,transaction.type().toString());
            ps.setString(4 ,transaction.lieu());
            ps.setString(5 ,transaction.idCompte());
            ps.setString(6 ,transaction.id());
            ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delete(String id) {
        String request = "DELETE FROM Transaction WHERE id = ?";
        try(var ps = conn.prepareStatement(request)){
            ps.setString(1 ,id);
            ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Optional<Transaction> findById(String id){
        String request = "SELECT * FROM transactions WHERE id = ?";
        try(var ps = conn.prepareStatement(request)){
            ps.setString(1 ,id);
            var rs = ps.executeQuery();
            if(rs.next()){
                return Optional.of(new Transaction(
                        rs.getString("id"),
                        rs.getDate("date"),
                        rs.getDouble("montant"),
                        rs.getString("idCompte"),
                        typeTransaction.valueOf(rs.getString("typeTransaction")),
                        rs.getString("lieu")
                ));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    public List<Transaction> findAll(){
        String request = "SELECT * FROM Transaction";
        try(var ps = conn.prepareStatement(request)){
            var rs = ps.executeQuery();
            var transactions = new java.util.ArrayList<Transaction>();
            while(rs.next()){
                transactions.add(new Transaction(
                        rs.getString("id"),
                        rs.getDate("date"),
                        rs.getDouble("montant"),
                        rs.getString("idCompte"),
                        typeTransaction.valueOf(rs.getString("typeTransaction")),
                        rs.getString("lieu")
                ));
            }
            return transactions;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    };
    public List<Transaction> findByCompte(String idCompte){
        String request = "SELECT * FROM Transaction WHERE idCompte = ?";
        try(var ps = conn.prepareStatement(request)){
            ps.setString(1 ,idCompte);
            var rs = ps.executeQuery();
            var transactions = new java.util.ArrayList<Transaction>();
            while(rs.next()){
                transactions.add(new Transaction(
                        rs.getString("id"),
                        rs.getDate("date"),
                        rs.getDouble("montant"),
                        rs.getString("idCompte"),
                        typeTransaction.valueOf(rs.getString("typeTransaction")),
                        rs.getString("lieu")
                ));
            }
            return transactions;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


}
