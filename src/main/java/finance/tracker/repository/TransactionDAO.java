package finance.tracker.repository;

import finance.tracker.model.Transaction;
import finance.tracker.model.TransactionFactory;
import finance.tracker.model.TransactionType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private final Connection connection;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean insertTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (type, amount, category, description, date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getType().name());
            pstmt.setDouble(2, transaction.getAmount());
            pstmt.setString(3, transaction.getCategory());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setDate(5, Date.valueOf(transaction.getDate()));
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transactions ORDER BY date DESC";
        List<Transaction> transactions = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                double amount = rs.getDouble("amount");
                String category = rs.getString("category");
                String description = rs.getString("description");
                LocalDate date = rs.getDate("date").toLocalDate();

                // Use your factory here
                transactions.add(TransactionFactory.createTransaction(type, amount, category, description, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}