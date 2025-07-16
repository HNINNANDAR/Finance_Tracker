package finance.tracker.repository;

import finance.tracker.model.Category;
import finance.tracker.model.TransactionType;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDAOTest {

    private static Connection connection;
    private CategoryDAO categoryDAO;
    private static final int TEST_USER_ID = 106;

    @BeforeAll
    static void initDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/finance_tracker", "postgres", "postgres");
    }

    @BeforeEach
    void setUp() throws SQLException {
        categoryDAO = new CategoryDAO(connection);
        Statement stmt = connection.createStatement();

        // Delete transactions first, so category rows are not referenced
        stmt.execute("DELETE FROM transactions");

        // Now delete categories
        stmt.execute("DELETE FROM category");

        // Also clear users if needed
        stmt.execute("DELETE FROM users");

        // Insert a test user
        stmt.execute("INSERT INTO users (id, email, username, password) VALUES (106,'test@gmail.com', 'testuser', 'password')");
    }


    @Test
    void testInsertAndGetAllByUser() {
        Category category = new Category(0, "Groceries", TransactionType.EXPENSE, TEST_USER_ID);
        assertTrue(categoryDAO.insertCategory(category));

        List<Category> categories = categoryDAO.getAllByUser(TEST_USER_ID);
        assertFalse(categories.isEmpty());
        assertEquals("Groceries", categories.get(0).getCategoryName());
    }

    @Test
    void testGetById() {
        Category category = new Category(0, "Transport", TransactionType.EXPENSE, TEST_USER_ID);
        assertTrue(categoryDAO.insertCategory(category));

        Category inserted = categoryDAO.getAllByUser(TEST_USER_ID).get(0);
        Category found = categoryDAO.getById(inserted.getCategoryId());
        assertNotNull(found);
        assertEquals("Transport", found.getCategoryName());
    }

    @Test
    void testFindByNameAndUserId() {
        Category category = new Category(0, "Bills", TransactionType.EXPENSE, TEST_USER_ID);
        assertTrue(categoryDAO.insertCategory(category));

        Category found = categoryDAO.findByNameAndUserId(category, TEST_USER_ID);
        assertNotNull(found);
        assertEquals("Bills", found.getCategoryName());
    }

    @Test
    void testFindByNameTypeAndUserId() {
        Category category = new Category(0, "Health", TransactionType.EXPENSE, TEST_USER_ID);
        assertTrue(categoryDAO.insertCategory(category));

        Category found = categoryDAO.findByNameTypeAndUserId("Health", TransactionType.EXPENSE, TEST_USER_ID);
        assertNotNull(found);
        assertEquals("Health", found.getCategoryName());
    }

    @Test
    void testUpdateCategory() {
        Category category = new Category(0, "Clothing", TransactionType.EXPENSE, TEST_USER_ID);
        assertTrue(categoryDAO.insertCategory(category));

        Category inserted = categoryDAO.getAllByUser(TEST_USER_ID).get(0);
        inserted.setCategoryName("Apparel");
        inserted.setTransactionType(TransactionType.INCOME);
        assertTrue(categoryDAO.updateCategory(inserted));

        Category updated = categoryDAO.getById(inserted.getCategoryId());
        assertEquals("Apparel", updated.getCategoryName());
        assertEquals(TransactionType.INCOME, updated.getTransactionType());
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category(0, "Leisure", TransactionType.EXPENSE, TEST_USER_ID);
        assertTrue(categoryDAO.insertCategory(category));

        Category inserted = categoryDAO.getAllByUser(TEST_USER_ID).get(0);
        assertTrue(categoryDAO.deleteCategory(inserted.getCategoryId()));

        Category deleted = categoryDAO.getById(inserted.getCategoryId());
        assertNull(deleted);
    }

    @Test
    void testGetCategoriesForUser() throws SQLException {
        // Insert global category (user_id = NULL)
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO category (name, type, user_id) VALUES ('Global', 'EXPENSE', NULL)");

        // Insert user-specific category
        Category category = new Category(0, "Rent", TransactionType.EXPENSE, TEST_USER_ID);
        assertTrue(categoryDAO.insertCategory(category));

        List<Category> categories = categoryDAO.getCategoriesForUser(TEST_USER_ID);
        assertEquals(2, categories.size());
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
