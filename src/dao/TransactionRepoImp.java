package dao;

import entity.Transaction;
import enums.typeTransaction;
import util.DateUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
            ps.setTimestamp(2, DateUtil.toTimestamp(transaction.date()));
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
    public void update(Transaction transaction){
        System.out.println("Update not supported");
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

    @Override
    public List<Transaction> findByMonth(int year, int month) {
        String request = "SELECT * FROM transaction WHERE YEAR(date) = ? AND MONTH(date) = ?";
        List<Transaction> list = new ArrayList<>();

        try (var ps = conn.prepareStatement(request)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            var rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getString("id"),
                        rs.getTimestamp("date"),
                        rs.getDouble("montant"),
                        rs.getString("idCompte"),
                        typeTransaction.valueOf(rs.getString("typeTransaction")),
                        rs.getString("lieu")
                );
                list.add(t);
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }




}
