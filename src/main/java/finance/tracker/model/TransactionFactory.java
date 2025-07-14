package finance.tracker.model;

import java.time.LocalDate;

public class TransactionFactory {

    public static Transaction createTransaction(TransactionType type, double amount, String category, String description, LocalDate date) {
        return new BaseTransaction(type, amount, category, description, date);
    }
}