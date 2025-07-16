package finance.tracker.ui;

import finance.tracker.repository.UserDAO;
import finance.tracker.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final int FIELD_WIDTH = 300;
    private static final int FIELD_HEIGHT = 35;

    public RegisterPanel(UserService service, Runnable goToLogin) {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(LABEL_FONT);
        gbc.gridy = 1; gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        JTextField usernameField = createStyledTextField();
        gbc.gridy = 2; gbc.gridwidth = 2;
        add(usernameField, gbc);

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(LABEL_FONT);
        gbc.gridy = 3; gbc.gridwidth = 1;
        add(emailLabel, gbc);

        JTextField emailField = createStyledTextField();
        gbc.gridy = 4; gbc.gridwidth = 2;
        add(emailField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT);
        gbc.gridy = 5; gbc.gridwidth = 1;
        add(passwordLabel, gbc);

        JPasswordField passwordField = createStyledPasswordField();
        gbc.gridy = 6; gbc.gridwidth = 2;
        add(passwordField, gbc);

        // Confirm Password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(LABEL_FONT);
        gbc.gridy = 7; gbc.gridwidth = 1;
        add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = createStyledPasswordField();
        gbc.gridy = 8; gbc.gridwidth = 2;
        add(confirmPasswordField, gbc);

        // Register button
        JButton registerButton = createStyledButton("Create Account");
        gbc.gridy = 9; gbc.insets = new Insets(20, 10, 10, 10);
        add(registerButton, gbc);

        // Login link
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setOpaque(false);
        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabel.setFont(LABEL_FONT);
        JButton loginButton = createLinkButton("Sign in");
        loginPanel.add(loginLabel);
        loginPanel.add(loginButton);
        gbc.gridy = 10;
        add(loginPanel, gbc);

        // Action listeners
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError("Please fill in all fields");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match");
                return;
            }

            if (!isValidEmail(email)) {
                showError("Please enter a valid email address");
                return;
            }

            if (password.length() < 6) {
                showError("Password must be at least 6 characters long");
                return;
            }

            if (service.register(email, password, username)) {
                showSuccess("Registration successful!");
                goToLogin.run();
            } else {
                showError("Email already exists");
            }
        });

        loginButton.addActionListener(e -> goToLogin.run());
    }

    // Reuse the same styling methods from LoginPanel
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setFont(LABEL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setFont(LABEL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setFont(LABEL_FONT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(PRIMARY_COLOR);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
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

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}