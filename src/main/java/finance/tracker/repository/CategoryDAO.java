package finance.tracker.repository;

import finance.tracker.model.Category;
import finance.tracker.model.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private final Connection conn;

    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Category> getAllByUser(int userId) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT id, name, type FROM category WHERE user_id = ? OR user_id IS NULL";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                list.add(new Category(id, name, type, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Category> getCategoriesForUser(int userId) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name, type, user_id FROM category WHERE user_id IS NULL OR user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Integer userIdFromDb = null;
                if (!rs.wasNull()) {
                    userIdFromDb = rs.getInt("user_id");
                }

                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        TransactionType.valueOf(rs.getString("type")),
                        userIdFromDb
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public Category findByNameAndUserId(Category category, int userId) {
        String sql = "SELECT id, name, type, user_id FROM category WHERE name = ? AND user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String categoryName = rs.getString("name");
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                int userID = rs.getInt("user_id");

                return new Category(id, categoryName, type, userID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Category getById(int categoryId) {
        String sql = "SELECT id, name, type, user_id FROM category WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                int userId = rs.getInt("user_id");
                return new Category(categoryId, name, type, userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertCategory(Category category) {
        String sql = "INSERT INTO category (name, type, user_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getTransactionType().name());
            stmt.setInt(3, category.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Category findByNameTypeAndUserId(String name, TransactionType type, int userId) {
        String sql = "SELECT id, name, type, user_id FROM category WHERE name = ? AND type = ? AND user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, type.name());
            ps.setInt(3, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String categoryName = rs.getString("name");
                    TransactionType transactionType = TransactionType.valueOf(rs.getString("type"));
                    int uid = rs.getInt("user_id");
                    return new Category(id, categoryName, transactionType, uid);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCategory(Category category) {
        String sql = "UPDATE category SET name = ?, type = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getTransactionType().name());
            ps.setInt(3, category.getCategoryId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM category WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}

