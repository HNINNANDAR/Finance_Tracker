package finance.tracker.model;

import java.time.LocalDate;

public class BaseTransaction implements Transaction {
    private TransactionType type;
    private double amount;
    private String category;
    private String description;
    private LocalDate date;

     BaseTransaction(TransactionType type, double amount, String category, String description, LocalDate date) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    @Override public double getAmount() { return amount; }
    @Override public String getCategory() { return category; }
    @Override public String getDescription() { return description; }
    @Override public LocalDate getDate() { return date; }
    @Override public TransactionType getType() { return type; }

    @Override
    public void process() {
        if (type == TransactionType.INCOME) {
            System.out.println("✅ Income of $" + amount + " processed.");
        } else {
            System.out.println("💸 Expense of $" + amount + " processed.");
        }
    }

    @Override
    public boolean validate() {
        return amount > 0 && category != null && !category.isBlank();
    }

    @Override
    public String summary() {
        return "[" + type + "] $" + amount + " - " + category + " | " + description + " on " + date;
    }
}

