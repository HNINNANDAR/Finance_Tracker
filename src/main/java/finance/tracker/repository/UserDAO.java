package finance.tracker.repository;

import finance.tracker.model.User;

import java.sql.*;

    public class UserDAO {
        private final Connection conn;

        public UserDAO(Connection conn) {
            this.conn = conn;
        }

        public User login(String email, String password) {
            String sql = "SELECT id, email, username FROM users WHERE email = ? AND password = ? ";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password); // ⚠️ we'll hash later
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String foundEmail = rs.getString("email");
                    return new User(id, username, foundEmail);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public boolean register(String email, String password, String username) {
            String sql = "INSERT INTO users (email, password, username) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                stmt.setString(3, username);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                if (e.getMessage().contains("duplicate")) {
                    System.out.println("Email already exists!");
                }
                e.printStackTrace();
                return false;
            }
        }

    }


