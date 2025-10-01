package dao.interfaces;

import model.entity.Transaction;
import model.enums.typeTransaction;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends Crud<Transaction> {
    List<Transaction> findByCompte(String idCompte);
}
