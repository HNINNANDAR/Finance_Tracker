package finance.tracker;

import finance.tracker.repository.DatabaseConnector;
import finance.tracker.repository.TransactionDAO;
import finance.tracker.model.Transaction;
import finance.tracker.model.TransactionType;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        Connection conn = DatabaseConnector.getInstance();
        TransactionDAO dao = new TransactionDAO(conn);

//        Transaction t1 = TransactionFactory.createTransaction(TransactionType.INCOME, 1500, "Salary", "August Salary", LocalDate.now());
//        dao.insertTransaction(t1);

        List<Transaction> all = dao.getAllTransactions();
        all.forEach(tx -> {
            tx.process();
            System.out.println(tx.summary());
        });

    }
}