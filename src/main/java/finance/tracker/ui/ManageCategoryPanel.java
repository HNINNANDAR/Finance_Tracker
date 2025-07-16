
package finance.tracker.ui;

import finance.tracker.model.Category;
import finance.tracker.model.TransactionType;
import finance.tracker.repository.CategoryDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ManageCategoryPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color SECONDARY_COLOR = new Color(240, 240, 245);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final int FIELD_HEIGHT = 35;

    private final CategoryDAO categoryDAO;
    private final int userId;
    private final DefaultTableModel tableModel;
    private final JTable categoryTable;
    private final JTextField nameField;
    private final JComboBox<TransactionType> typeBox;
    private final JButton addButton;
    private final JButton backButton;

    public ManageCategoryPanel(CategoryDAO categoryDAO, int userId, Runnable onBack) {
        this.categoryDAO = categoryDAO;
        this.userId = userId;

        // Initialize components
        nameField = createStyledTextField();
        typeBox = createStyledComboBox(TransactionType.values());
        addButton = createStyledButton("Add ", PRIMARY_COLOR);
        backButton = createStyledButton("Back", new Color(108, 117, 125));

        // Setup table
        tableModel = new DefaultTableModel(new Object[]{"Name", "Type", "", ""}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 2;
            }
        };
        categoryTable = createStyledTable();

        // Setup panel
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Add components
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        // Setup event handlers
        setupEventHandlers(onBack);

        // Initial load
        refreshTable();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Manage Categories");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        setupTableColumns();
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBackground(BACKGROUND_COLOR);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createFormPanel() {
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(BACKGROUND_COLOR);
        formWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Left panel for form components
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftPanel.setBackground(BACKGROUND_COLOR);

        addLabeledComponent(leftPanel, "Name:", nameField);
        addLabeledComponent(leftPanel, "Type:", typeBox);
        leftPanel.add(addButton);

        // Right panel for back button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.add(backButton);

        formWrapper.add(leftPanel, BorderLayout.WEST);
        formWrapper.add(rightPanel, BorderLayout.EAST);

        return formWrapper;
    }


    private void setupTableColumns() {
        categoryTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer("Edit"));
        categoryTable.getColumnModel().getColumn(2).setCellEditor(
                new ButtonEditor("Edit", () -> editCategory(categoryTable.getSelectedRow())));

        categoryTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer("Delete"));
        categoryTable.getColumnModel().getColumn(3).setCellEditor(
                new ButtonEditor("Delete", () -> deleteCategory(categoryTable.getSelectedRow())));
    }

    private void addLabeledComponent(JPanel panel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        panel.add(label);
        panel.add(component);
    }

    private JTable createStyledTable() {
        JTable table = new JTable(tableModel);
        table.setFont(INPUT_FONT);
        table.getTableHeader().setFont(LABEL_FONT);
        table.setRowHeight(40);
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(230, 230, 230));

        // Center align for type column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        return table;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15);
        field.setFont(INPUT_FONT);
        field.setPreferredSize(new Dimension(150, FIELD_HEIGHT));
        return field;
    }

    private <T> JComboBox<T> createStyledComboBox(T... items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(INPUT_FONT);
        comboBox.setPreferredSize(new Dimension(150, FIELD_HEIGHT));
        return comboBox;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 35)); // Adjusted size to match other components
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }


    private void setupEventHandlers(Runnable onBack) {
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            TransactionType type = (TransactionType) typeBox.getSelectedItem();

            if (name.isEmpty()) {
                showError("Category name is required.");
                return;
            }

            Category category = new Category(0, name, type, userId);
            boolean success = categoryDAO.insertCategory(category);

            if (success) {
                showSuccess("Category added successfully!");
                nameField.setText("");
                typeBox.setSelectedIndex(0);
                refreshTable();
            } else {
                showError("Failed to add category.");
            }
        });

        backButton.addActionListener(e -> onBack.run());
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Category> categories = categoryDAO.getAllByUser(userId);
        for (Category c : categories) {
            tableModel.addRow(new Object[]{
                    c.getCategoryName(),
                    c.getTransactionType(),
                    "Edit",
                    "Delete"
            });
        }
    }

    public void editCategory(int row) {
        if (row < 0) return;

        String name = (String) tableModel.getValueAt(row, 0);
        TransactionType type = (TransactionType) tableModel.getValueAt(row, 1);
        Category category = categoryDAO.findByNameTypeAndUserId(name, type, userId);

        if (category != null) {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            EditCategoryDialog dialog = new EditCategoryDialog(
                    (JFrame) parent,
                    category,
                    categoryDAO,
                    this::refreshTable
            );
            dialog.setVisible(true);
        }
    }

    public void deleteCategory(int row) {
        if (row < 0) return;

        String name = (String) tableModel.getValueAt(row, 0);
        TransactionType type = (TransactionType) tableModel.getValueAt(row, 1);
        Category category = categoryDAO.findByNameTypeAndUserId(name, type, userId);

        if (category != null) {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            DeleteCategoryDialog dialog = new DeleteCategoryDialog(
                    (JFrame) parent,
                    category,
                    categoryDAO,
                    this::refreshTable
            );
            dialog.setVisible(true);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                "❌ " + message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this,
                "✅ " + message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Custom button renderer and editor classes...
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;

        public ButtonEditor(String text, Runnable onClick) {
            super(new JCheckBox());
            button = new JButton(text);
            button.setFont(new Font("SansSerif", Font.PLAIN, 14));
            button.addActionListener(e -> onClick.run());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}