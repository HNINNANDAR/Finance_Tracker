package finance.tracker.service;

import finance.tracker.model.BaseTransaction;
import finance.tracker.model.Transaction;
import finance.tracker.model.TransactionType;
import finance.tracker.repository.TransactionDAO;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class TransactionService {
    private final TransactionDAO txDao;
    public TransactionService(TransactionDAO dao) { this.txDao = dao; }

    public boolean addTransaction(Transaction tx) {
        // validation rules go here
        if (tx.getAmount() <= 0) throw new IllegalArgumentException("Amount must be positive");
        return txDao.insertTransaction(tx);
    }

    public List<BaseTransaction> listTransactions(int userId) {
        return txDao.getAllByUser(userId);
    }
    public List<BaseTransaction> getAllByUser(int userId){
        return txDao.getAllByUser(userId);
    }
    public BigDecimal getTotalByUserAndTypeInMonth(int userId, TransactionType type, YearMonth month) {
        return txDao.getTotalByUserAndTypeInMonth(userId, type, month);
    }
}
