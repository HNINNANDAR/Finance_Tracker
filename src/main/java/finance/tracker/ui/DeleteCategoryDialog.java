
package finance.tracker.ui;

import finance.tracker.model.Category;
import finance.tracker.repository.CategoryDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DeleteCategoryDialog extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(220, 53, 69); // Red color for delete
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Dimension DIALOG_SIZE = new Dimension(400, 250);

    private final JButton deleteButton;
    private final JButton cancelButton;

    public DeleteCategoryDialog(JFrame parent, Category category, CategoryDAO categoryDAO, Runnable onCategoryDeleted) {
        super(parent, "Delete Category", true);

        // Initialize components
        deleteButton = createStyledButton("Delete", PRIMARY_COLOR);
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
        mainPanel.add(createMessagePanel(category), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        // Add main panel to dialog
        setContentPane(mainPanel);

        // Setup event handlers
        setupEventHandlers(category, categoryDAO, onCategoryDeleted);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Confirm Deletion");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createMessagePanel(Category category) {
        JPanel messagePanel = new JPanel(new BorderLayout(0, 15));
        messagePanel.setBackground(BACKGROUND_COLOR);

        JLabel warningIcon = new JLabel("⚠️", SwingConstants.CENTER);
        warningIcon.setFont(new Font("SansSerif", Font.PLAIN, 48));

        JLabel messageLabel = new JLabel(String.format(
                "<html><center>Are you sure you want to delete the category<br><b>%s</b>?<br><br>" +
                        "This action cannot be undone.</center></html>",
                category.getCategoryName()
        ));
        messageLabel.setFont(INPUT_FONT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        messagePanel.add(warningIcon, BorderLayout.NORTH);
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        return messagePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);

        return buttonPanel;
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

    private void setupEventHandlers(Category category, CategoryDAO categoryDAO, Runnable onCategoryDeleted) {
        deleteButton.addActionListener(e -> {
            boolean success = categoryDAO.deleteCategory(category.getCategoryId());

            if (success) {
                showSuccess("Category deleted successfully!");
                onCategoryDeleted.run();
                dispose();
            } else {
                showError("Failed to delete category.");
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