package finance.tracker.ui;

import finance.tracker.model.User;
import finance.tracker.repository.*;
import finance.tracker.service.CategoryService;
import finance.tracker.service.TransactionService;
import finance.tracker.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * MainFrame — vertical sidebar with **Dashboard, Add, View, Categories**.
 * Sidebar hidden on login/register screens; shown once user is authenticated.
 */
public class MainFrame extends JFrame {

    /* Constants */
    private static final int W = 1000, H = 700;
    private static final Color PRIMARY = new Color(51, 153, 255);
    private static final Color BG = new Color(245, 245, 250);

    /* Layout */
    private final CardLayout cards = new CardLayout();
    private final JPanel mainPanel = new JPanel(cards);
    private JToolBar sidebar;

    /* DAOs */
    private final Connection conn = DatabaseConnector.getInstance();
    private final CategoryDAO catDAO = new CategoryDAO(conn);
    private final CategoryService catService = new CategoryService(catDAO);
    private final TransactionDAO txDAO = new TransactionDAO(conn);
    private final TransactionService service = new TransactionService(txDAO);
    private final UserDAO userDAO = new UserDAO(conn);
    private final UserService userService = new UserService(userDAO);

    /* Feature panels */
    private DashboardPanel dash;
    private AddTransactionPanel add;
    private ViewTransactionPanel view;
    private ManageCategoryPanel cats;

    private User current; // null until login

    public MainFrame() {
        super("Finance Tracker");
        setSize(W, H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSystemLF();

        // initial unauthenticated layout
        buildLoginPanel();
        setContentPane(buildContainer());
        sidebar.setVisible(false); // hide until login
        setVisible(true);
    }

    /* ================= INITIAL BUILDERS ================= */
    private Container buildContainer() {
        JPanel c = new JPanel(new BorderLayout());
        sidebar = buildSidebar();
        c.add(sidebar, BorderLayout.WEST);
        c.add(wrapWithPadding(mainPanel), BorderLayout.CENTER);
        return c;
    }

    private JToolBar buildSidebar() {
        JToolBar bar = new JToolBar(JToolBar.VERTICAL);
        bar.setFloatable(false);
        bar.setBackground(BG);
        bar.setBorder(new EmptyBorder(10, 10, 10, 10));

        bar.add(makeNavBtn("🏠", "Dashboard", () -> cards.show(mainPanel, "HOME")));
        bar.addSeparator();
//        bar.add(makeNavBtn("➕", "Add Transaction", () -> cards.show(mainPanel, "ADD")));
        bar.add(makeNavBtn("➕", "Add Transaction", () -> {
            add.loadCategories(current.getUserId(), catService); // Refresh categories
            cards.show(mainPanel, "ADD");
        }));

        bar.addSeparator();
        bar.add(makeNavBtn("📋", "View Transactions", () -> {
            if (view != null) view.loadTransactions();
            cards.show(mainPanel, "VIEW");
        }));
        bar.addSeparator();
        bar.add(makeNavBtn("🗂", "Categories", () -> cards.show(mainPanel, "CATEGORIES")));
        return bar;
    }

    private JButton makeNavBtn(String icon, String text, Runnable act) {
        JButton b = new JButton(String.format("<html><center>%s<br>%s</center></html>", icon, text));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBackground(PRIMARY);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(140, 80));
        b.addActionListener(e -> act.run());
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(PRIMARY.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(PRIMARY); }
        });
        return b;
    }

    /* ================= AUTHENTICATED BUILDERS ================= */
    private void initFeaturePanels() {
        dash = new DashboardPanel(current.getUserId(),service);
        add = new AddTransactionPanel(catService, current.getUserId(), () -> {
            dash.refresh();
            cards.show(mainPanel, "HOME")
            ;
        }, service);
        view = new ViewTransactionPanel(service, current.getUserId(), () -> cards.show(mainPanel, "HOME"));
        cats = new ManageCategoryPanel(catService, current.getUserId(), () -> cards.show(mainPanel, "HOME"));
    }

    private void buildCardsAfterLogin() {
        mainPanel.removeAll();
        mainPanel.add(buildHomeWrapper(), "HOME");
        mainPanel.add(add, "ADD");
        mainPanel.add(view, "VIEW");
        mainPanel.add(cats, "CATEGORIES");
    }

    private JPanel buildHomeWrapper() {
        JPanel wrap = new JPanel(new BorderLayout(0, 20));
        wrap.setBackground(BG);
        wrap.setBorder(new EmptyBorder(20, 20, 20, 20));
        wrap.add(buildHeader(), BorderLayout.NORTH);
        wrap.add(dash, BorderLayout.CENTER);
        wrap.add(buildMonthPicker(), BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JPanel text = new JPanel(new GridLayout(2, 1)); text.setOpaque(false);
        JLabel t = new JLabel("Welcome, " + current.getUsername()); t.setFont(new Font("SansSerif", Font.BOLD, 24));
        JLabel s = new JLabel("Finance Tracker Dashboard"); s.setFont(new Font("SansSerif", Font.PLAIN, 16));
        text.add(t); text.add(s);
        p.add(text, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); right.setOpaque(false);
        right.add(buildLogoutButton());
        p.add(right, BorderLayout.EAST);
        return p;
    }

    /* ================= MONTH PICKER ================= */
    private JPanel buildMonthPicker() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); p.setOpaque(false);
        String[] months = Arrays.stream(Month.values()).map(m -> m.getDisplayName(TextStyle.SHORT, Locale.getDefault())).toArray(String[]::new);
        JComboBox<String> mBox = new JComboBox<>(months);
        mBox.setSelectedIndex(YearMonth.now().getMonthValue() - 1);
        Integer[] years = IntStream.rangeClosed(YearMonth.now().getYear() - 5, YearMonth.now().getYear() + 5).boxed().toArray(Integer[]::new);
        JComboBox<Integer> yBox = new JComboBox<>(years);
        yBox.setSelectedItem(YearMonth.now().getYear());
        p.add(new JLabel("Month:")); p.add(mBox); p.add(new JLabel("Year:")); p.add(yBox);
        Runnable upd = () -> dash.setMonth(YearMonth.of((int) yBox.getSelectedItem(), mBox.getSelectedIndex() + 1));
        mBox.addActionListener(e -> upd.run()); yBox.addActionListener(e -> upd.run());
        return p;
    }

    /* ================= LOGOUT ================= */
    private JButton buildLogoutButton() {
        JButton b = new JButton("Logout");
        b.setBackground(new Color(220, 53, 69)); b.setForeground(Color.BLACK); b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(90, 35));
        b.addActionListener(e -> logout());
        return b;
    }

    private void logout() {
        JOptionPane.showMessageDialog(this, "You have been logged out.");
        current = null;
        sidebar.setVisible(false);
        mainPanel.removeAll();
        buildLoginPanel();
        cards.show(mainPanel, "LOGIN");
        mainPanel.revalidate(); mainPanel.repaint();
    }

    /* ================= LOGIN SCREEN ================= */
    private void buildLoginPanel() {
        // Build Login Panel with navigation to Register
        LoginPanel login = new LoginPanel(
                userService,
                () -> {}, // onSuccess placeholder (handled inside onLoginSuccess)
                u -> {                     // onLoginSuccess
                    current = u;
                    sidebar.setVisible(true);
                    initFeaturePanels();
                    buildCardsAfterLogin();
                    cards.show(mainPanel, "HOME");
                },
                () -> cards.show(mainPanel, "REGISTER") // goToRegister
        );

        // Build Register Panel with back‑to‑Login navigation
        RegisterPanel register = new RegisterPanel(
                userService,
                () -> cards.show(mainPanel, "LOGIN")
        );

        mainPanel.add(login, "LOGIN");
        mainPanel.add(register, "REGISTER");
    }

    /* ================= HELPERS ================= */
    private JPanel wrapWithPadding(JComponent c) { JPanel p = new JPanel(new BorderLayout()); p.setBorder(new EmptyBorder(10, 10, 10, 10)); p.add(c); return p; }
    private void setSystemLF() { try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {} }

    public static void main(String[] a) { SwingUtilities.invokeLater(MainFrame::new); }
}
