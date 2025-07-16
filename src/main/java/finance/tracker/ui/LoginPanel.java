package finance.tracker.ui;

import finance.tracker.model.User;
import finance.tracker.repository.UserDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;


public class LoginPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final int FIELD_WIDTH = 300;
    private static final int FIELD_HEIGHT = 35;

    public LoginPanel(UserDAO userDAO, Runnable onSuccess, Consumer<User> onLoginSuccess, Runnable goToRegister) {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Welcome Back!", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Please login to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        add(subtitleLabel, gbc);

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(LABEL_FONT);
        gbc.gridy = 2; gbc.gridwidth = 1;
        add(emailLabel, gbc);

        JTextField emailField = createStyledTextField();
        gbc.gridy = 3; gbc.gridwidth = 2;
        add(emailField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT);
        gbc.gridy = 4; gbc.gridwidth = 1;
        add(passwordLabel, gbc);

        JPasswordField passwordField = createStyledPasswordField();
        gbc.gridy = 5; gbc.gridwidth = 2;
        add(passwordField, gbc);

        // Login button
        JButton loginButton = createStyledButton("Login");
        gbc.gridy = 6; gbc.insets = new Insets(20, 10, 10, 10);
        add(loginButton, gbc);

        // Register link
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setOpaque(false);
        JLabel registerLabel = new JLabel("Don't have an account?");
        registerLabel.setFont(LABEL_FONT);
        JButton registerButton = createLinkButton("Sign up");
        registerPanel.add(registerLabel);
        registerPanel.add(registerButton);
        gbc.gridy = 7;
        add(registerPanel, gbc);

        // Action listeners
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                showError("Please fill in all fields");
                return;
            }

            User user = userDAO.login(email, password);
            if (user != null) {
                showSuccess("Login successful!");
                onLoginSuccess.accept(user);
                onSuccess.run();
            } else {
                showError("Invalid email or password");
            }
        });

        registerButton.addActionListener(e -> goToRegister.run());
    }

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
}