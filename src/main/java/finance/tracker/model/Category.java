package finance.tracker.model;

public class Category {
    private int categoryId;
    private String categoryName;
    private TransactionType transactionType;
    private int userId;

    public Category(int categoryId, String categoryName, TransactionType transactionType, int userId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.transactionType = transactionType;
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
