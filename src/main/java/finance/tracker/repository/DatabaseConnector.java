package finance.tracker.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/finance_tracker";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private static Connection connection = null;

    private DatabaseConnector() {} // Prevent instantiation

    public static Connection getInstance() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connected to PostgreSQL");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
