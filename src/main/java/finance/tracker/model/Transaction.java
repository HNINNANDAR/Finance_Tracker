package finance.tracker.model;

import java.time.LocalDate;

public interface Transaction {
    double getAmount();
    Category getCategory();
    String getDescription();
    LocalDate getDate();
    TransactionType getType();
    int getUserId();

    void process();       // E.g., business logic
    boolean validate();   // E.g., is amount > 0, category not empty
    String summary();     // E.g., print one-line summary

    // Default method
    default void printReceipt() {
        System.out.println("🧾 Transaction Receipt");
        System.out.println("Type: " + getType());
        System.out.println("Amount: $" + getAmount());
        System.out.println("Category: " + getCategory());
        System.out.println("Date: " + getDate());
        System.out.println("Description: " + getDescription());
    }
}

