package dao;

import entity.Transaction;

import java.util.List;

public interface TransactionRepository extends Crud<Transaction> {
    List<Transaction> findByCompte(String idCompte);
}
