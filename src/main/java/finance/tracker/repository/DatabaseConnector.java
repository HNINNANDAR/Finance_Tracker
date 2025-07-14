package finance.tracker.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static final String URL ="jdbc:postgresql://localhost:5432/finance_tracker";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "postgres";
    public static Connection connect(){
        try{
            Connection con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("connected to database..");
            return con;

        } catch (SQLException e) {
            System.out.println("database connection failed..");
            e.getStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        connect();
    }
}
