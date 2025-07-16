
package finance.tracker.ui;

import finance.tracker.model.*;
import finance.tracker.repository.CategoryDAO;
import finance.tracker.service.CategoryService;
import finance.tracker.service.TransactionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class AddTransactionPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color SECONDARY_COLOR = new Color(240, 240, 245);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final int FIELD_HEIGHT = 35;

    private final JTextField amountField;
    private final JTextField descriptionField;
    private final JComboBox<String> typeBox;
    private final JComboBox<Category> categoryBox;
    private final JSpinner dateSpinner;
    private final JButton submitBtn;
    private int userId;
    private final CategoryService service;
    private final TransactionService transactionService;


    public AddTransactionPanel(CategoryService catService, int userId, Runnable onBack, TransactionService transactionService) {
        this.service = catService;
        this.transactionService = transactionService;
//        this.transactionDAO = transactionDAO;
//        this.userId = userId;
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Initialize components first
        amountField = createStyledTextField();
        descriptionField = createStyledTextField();
        typeBox = createStyledComboBox(new String[]{"INCOME", "EXPENSE"});
        categoryBox = createStyledComboBox();
        dateSpinner = createStyledDateSpinner();
        submitBtn = createStyledButton("Submit", PRIMARY_COLOR);

        setUserId(userId);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Form Panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel(onBack);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add components to form
        addFormComponents(formPanel);

        // Setup event handlers
        setupEventHandlers(onBack);
    }
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Add New Transaction");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return formPanel;
    }

    private JPanel createButtonPanel(Runnable onBack) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton backButton = createStyledButton("Back", new Color(108, 117, 125));
        backButton.addActionListener(e -> onBack.run());

        buttonPanel.add(backButton);
        buttonPanel.add(submitBtn);

        return buttonPanel;
    }

    private void addFormComponents(JPanel formPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;

        // Add form fields
        addFormRow(formPanel, "Amount:", amountField, gbc, 0);
        addFormRow(formPanel, "Description:", descriptionField, gbc, 1);
        addFormRow(formPanel, "Type:", typeBox, gbc, 2);
        addFormRow(formPanel, "Category:", categoryBox, gbc, 3);
        addFormRow(formPanel, "Date:", dateSpinner, gbc, 4);
    }

    private void addFormRow(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(INPUT_FONT);
        field.setPreferredSize(new Dimension(200, FIELD_HEIGHT));
        return field;
    }

    private <T> JComboBox<T> createStyledComboBox(T... items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(INPUT_FONT);
        comboBox.setPreferredSize(new Dimension(200, FIELD_HEIGHT));
        return comboBox;
    }

    private JSpinner createStyledDateSpinner() {
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(dateModel);
        spinner.setFont(INPUT_FONT);
        spinner.setPreferredSize(new Dimension(200, FIELD_HEIGHT));

        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);
        spinner.setValue(new java.util.Date());

        return spinner;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

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
        submitBtn.addActionListener(e -> {
            if (validateAndSubmit()) {
                resetForm();
                onBack.run(); // Navigate back to home after successful submission
            }
        });
    }

    private boolean validateAndSubmit() {
        try {
            String typeStr = (String) typeBox.getSelectedItem();
            Category category = (Category) categoryBox.getSelectedItem();
            String amountStr = amountField.getText().trim();
            String description = descriptionField.getText().trim();
            java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();

            if (typeStr == null || category == null || amountStr.isEmpty() || description.isEmpty()) {
                showError("Please fill in all fields.");
                return false;
            }

            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                showError("Amount must be greater than zero.");
                return false;
            }

            LocalDate date = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            TransactionType type = TransactionType.valueOf(typeStr);

            BaseTransaction transaction = TransactionFactory.createTransaction(
                    type, amount, category, description, date, userId
            );

            transactionService.addTransaction(transaction);
            showSuccess("Transaction added successfully!");
            return true;

        } catch (NumberFormatException ex) {
            showError("Please enter a valid number for amount.");
            return false;
        } catch (Exception ex) {
            showError("An error occurred while saving the transaction.");
            return false;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void resetForm() {
        amountField.setText("");
        descriptionField.setText("");
        typeBox.setSelectedIndex(0);
        categoryBox.setSelectedIndex(0);
        dateSpinner.setValue(new java.util.Date());
    }

    public void loadCategories(int userId, CategoryService service) {
        List<Category> categories = service.getCategoriesForUser(userId);
        categoryBox.removeAllItems();
        for (Category c : categories) {
            categoryBox.addItem(c);
        }
    }

    public void setUserId(int userId) {
        this.userId = userId;
        loadCategories(userId, service);
    }
}