package finance.tracker.model;

import java.time.LocalDate;

public class BaseTransaction implements Transaction {
    private TransactionType type;
    private double amount;
    private Category category;
    private String description;
    private LocalDate date;
    private int userId;

     public BaseTransaction(TransactionType type, double amount, Category category, String description, LocalDate date, int userId) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.userId = userId;
    }

    @Override public double getAmount() { return amount; }
    @Override public Category getCategory() { return category; }
    @Override public String getDescription() { return description; }
    @Override public LocalDate getDate() { return date; }
    @Override public TransactionType getType() { return type; }
    @Override public int getUserId() { return userId; }

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
        return amount > 0 && category != null && category.getCategoryId() !=0;
    }

    @Override
    public String summary() {
        return "[" + type + "] $" + amount + " - " + category + " | " + description + " on " + date;
    }
}

