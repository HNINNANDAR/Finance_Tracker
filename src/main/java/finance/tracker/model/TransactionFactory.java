package finance.tracker.model;

import java.time.LocalDate;

public class TransactionFactory {

    public static BaseTransaction createTransaction(TransactionType type, double amount, Category category, String description, LocalDate date, int userId) {
        return new BaseTransaction(type, amount, category, description, date, userId);
    }
}