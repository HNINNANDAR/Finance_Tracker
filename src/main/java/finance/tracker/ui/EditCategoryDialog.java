
package finance.tracker.ui;

import finance.tracker.model.Category;
import finance.tracker.model.TransactionType;
import finance.tracker.repository.CategoryDAO;
import finance.tracker.service.CategoryService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EditCategoryDialog extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final int FIELD_HEIGHT = 35;
    private static final Dimension DIALOG_SIZE = new Dimension(400, 300);

    private final JTextField nameField;
    private final JButton saveButton;
    private final JButton cancelButton;

    public EditCategoryDialog(JFrame parent, Category category, CategoryService service, Runnable onCategoryUpdated) {
        super(parent, "Edit Category", true);

        // Initialize components
        nameField = createStyledTextField();
        nameField.setText(category.getCategoryName());
        saveButton = createStyledButton("Save", PRIMARY_COLOR);
        cancelButton = createStyledButton("Cancel", SECONDARY_COLOR);

        // Setup dialog
        setSize(DIALOG_SIZE);
        setLocationRelativeTo(parent);
        setResizable(false);

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Add components
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        // Add main panel to dialog
        setContentPane(mainPanel);

        // Setup event handlers
        setupEventHandlers(category, service, onCategoryUpdated);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Edit Category");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;

        addFormRow(formPanel, "Category Name:", nameField, gbc, 0);

        return formPanel;
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

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(INPUT_FONT);
        field.setPreferredSize(new Dimension(200, FIELD_HEIGHT));
        return field;
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

    private void setupEventHandlers(Category category, CategoryService service, Runnable onCategoryUpdated) {
        saveButton.addActionListener(e -> {
            String newName = nameField.getText().trim();

            if (newName.isEmpty()) {
                showError("Category name is required.");
                return;
            }

            category.setCategoryName(newName);
            boolean success = service.updateCategory(category);

            if (success) {
                showSuccess("Category updated successfully!");
                onCategoryUpdated.run();
                dispose();
            } else {
                showError("Failed to update category.");
            }
        });

        cancelButton.addActionListener(e -> dispose());
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
}
