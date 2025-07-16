package finance.tracker.repository;

import static org.junit.jupiter.api.Assertions.*;


import finance.tracker.model.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionDAOTest {

    private static Connection conn;
    private TransactionDAO dao;

    @BeforeAll
    static void spinUpDb() throws Exception {
        conn = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MYSQL", "sa", "");
        // run schema
        try (Statement st = conn.createStatement()) {
            st.execute(new String(Files.readAllBytes(
                    Path.of("src/test/resources/schema.sql"))));
        }
    }

    @BeforeEach
    void initDao() {
        dao = new TransactionDAO(conn);
    }

    @Test
    void insertTransaction_persistsRow() {
        Transaction tx = TransactionFactory.createTransaction(
                TransactionType.INCOME, 500.0,
                new Category(2,"Salary", TransactionType.EXPENSE, 1), "Testing",LocalDate.now(),1);


        assertTrue(dao.insertTransaction(tx));

        // verify via plain SQL
        try (PreparedStatement ps =
                     conn.prepareStatement("SELECT COUNT(*) FROM transactions WHERE amount = 500")) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals(1, rs.getInt(1));
        } catch (SQLException e) { fail(e); }
    }

    @Test
    void monthlyTotals_returnCorrectSums() {
        // seed two rows
        dao.insertTransaction(TransactionFactory.createTransaction(
                TransactionType.EXPENSE, 100, new Category(1,"Food",TransactionType.EXPENSE,1),
                "Groceries", LocalDate.of(2025,7,10), 1));
        dao.insertTransaction(TransactionFactory.createTransaction(
                TransactionType.INCOME,  800, new Category(2,"Salary",TransactionType.INCOME,1),
                "Pay", LocalDate.of(2025,7,1), 1));

        BigDecimal income  = dao.getTotalByUserAndTypeInMonth(1, TransactionType.INCOME,  YearMonth.of(2025,7));
        BigDecimal expense = dao.getTotalByUserAndTypeInMonth(1, TransactionType.EXPENSE, YearMonth.of(2025,7));

        assertEquals(new BigDecimal("800"), income);
        assertEquals(new BigDecimal("100"), expense);
    }
}
