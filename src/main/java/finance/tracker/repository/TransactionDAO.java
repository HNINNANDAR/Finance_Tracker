package finance.tracker.repository;

import finance.tracker.model.*;
import finance.tracker.service.CategoryService;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private final Connection connection;
    private final CategoryDAO categoryDAO;
    private final CategoryService service;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
        categoryDAO = new CategoryDAO(connection);
        service = new CategoryService(categoryDAO);
    }
    public BigDecimal getTotalByUserAndTypeInMonth(int userId, TransactionType type, YearMonth month) {
        String sql = """
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE user_id = ?
          AND type = ?
          AND date >= ? AND date < ?
    """;

        LocalDate firstDay = month.atDay(1);
        LocalDate firstDayNextMonth = month.plusMonths(1).atDay(1);

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, type.name());
            pstmt.setDate(3, Date.valueOf(firstDay));
            pstmt.setDate(4, Date.valueOf(firstDayNextMonth));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Use BigDecimal for precise money calculation
                    return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    public boolean insertTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (type, amount, category_id, description, date, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getType().name());
            pstmt.setDouble(2, transaction.getAmount());
            pstmt.setInt(3, transaction.getCategory().getCategoryId());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setDate(5, Date.valueOf(transaction.getDate()));
            pstmt.setInt(6, transaction.getUserId());
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
                int categoryId = rs.getInt("category_id");
                String description = rs.getString("description");
                LocalDate date = rs.getDate("date").toLocalDate();
                int userId = rs.getInt("user_id");

                Category category = service.getById(categoryId);
                
                //create transaction with factory method
                transactions.add(TransactionFactory.createTransaction(type, amount, category, description, date, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<BaseTransaction> getAllByUser(int userId) {
        List<BaseTransaction> transactions = new ArrayList<>();
        String sql = "SELECT t.type, t.amount, t.description, t.date, t.category_id, t.user_id " +
                "FROM transactions t JOIN category c ON t.category_id = c.id " +
                "WHERE t.user_id = ? ORDER BY t.date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                double amount = rs.getDouble("amount");
                String description = rs.getString("description");
                LocalDate date = rs.getDate("date").toLocalDate();
                int categoryId = rs.getInt("category_id");
                int userIdFromDB = rs.getInt("user_id");

                Category category = service.getById(categoryId); // we added this earlier
                transactions.add(TransactionFactory.createTransaction(type, amount, category, description, date, userIdFromDB));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

}