package finance.tracker.ui;

import finance.tracker.model.BaseTransaction;
import finance.tracker.repository.TransactionDAO;
import finance.tracker.service.TransactionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ViewTransactionPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final int FIELD_HEIGHT = 35;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private final TransactionService service;
    private int userId;

    // Filter components
    private JComboBox<String> typeFilter;
    private JComboBox<String> categoryFilter;
    private final JTextField minDateField;
    private final JTextField maxDateField;
    private final JButton clearBtn;
    private final JButton backButton;

    public ViewTransactionPanel(TransactionService service, int userId, Runnable onBack) {
        this.service = service;
        this.userId = userId;

        // Initialize components first
        typeFilter = createStyledComboBox(new String[]{"All", "INCOME", "EXPENSE"});
        categoryFilter = createStyledComboBox(new String[]{"All"});
        minDateField = createStyledTextField();
        maxDateField = createStyledTextField();
        clearBtn = createStyledButton("Clear", new Color(108, 117, 125));
        backButton = createStyledButton("Back", new Color(108, 117, 125));

        // Initialize table
        tableModel = new DefaultTableModel(new Object[]{"Type", "Amount", "Category", "Description", "Date"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = createStyledTable();
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Setup panel
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Add components
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(onBack), BorderLayout.SOUTH);

        // Setup event handlers
        setupEventHandlers();

        // Initial load
        loadTransactions();
        applyFilters();
    }
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("View Transactions");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createFilterPanel(), BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        filterPanel.setBackground(BACKGROUND_COLOR);

        addLabeledComponent(filterPanel, "Type:", typeFilter);
        addLabeledComponent(filterPanel, "Category:", categoryFilter);
        addLabeledComponent(filterPanel, "From:", minDateField);
        addLabeledComponent(filterPanel, "To:", maxDateField);
        filterPanel.add(clearBtn);

        return filterPanel;
    }

    private void addLabeledComponent(JPanel panel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        panel.add(label);
        panel.add(component);
    }

    private JPanel createButtonPanel(Runnable onBack) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        backButton.addActionListener(e -> onBack.run());
        buttonPanel.add(backButton);
        return buttonPanel;
    }

    private JTable createStyledTable() {
        JTable table = new JTable(tableModel);
        table.setFont(INPUT_FONT);
        table.getTableHeader().setFont(LABEL_FONT);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(230, 230, 230));
        return table;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(10);
        field.setFont(INPUT_FONT);
        field.setPreferredSize(new Dimension(120, FIELD_HEIGHT));
        return field;
    }

    private <T> JComboBox<T> createStyledComboBox(T... items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(INPUT_FONT);
        comboBox.setPreferredSize(new Dimension(120, FIELD_HEIGHT));
        return comboBox;
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

    private void setupEventHandlers() {
        typeFilter.addActionListener(e -> applyFilters());
        categoryFilter.addActionListener(e -> applyFilters());

        DocumentListener dl = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void removeUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilters(); }
        };

        minDateField.getDocument().addDocumentListener(dl);
        maxDateField.getDocument().addDocumentListener(dl);

        clearBtn.addActionListener(e -> {
            resetFilters();
            applyFilters();
        });
    }


    // ── UI builders ───────────────────────────────────────────────────────────

    private void buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        typeFilter = new JComboBox<>(new String[]{"All", "INCOME", "EXPENSE"});
        categoryFilter = new JComboBox<>(new String[]{"All"});

        bar.add(new JLabel("Type:"));
        bar.add(typeFilter);
        bar.add(new JLabel("Category:"));
        bar.add(categoryFilter);
        bar.add(new JLabel("From (yyyy-MM-dd):"));
        bar.add(minDateField);
        bar.add(new JLabel("To:"));
        bar.add(maxDateField);

        // ── Clear‑all button ────────────────────────────────────────────────
        JButton clearBtn = new JButton("Clear");
        bar.add(clearBtn);

        // live filter listeners
        typeFilter.addActionListener(e -> applyFilters());
        categoryFilter.addActionListener(e -> applyFilters());

        DocumentListener dl = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void removeUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilters(); }
        };
        minDateField.getDocument().addDocumentListener(dl);
        maxDateField.getDocument().addDocumentListener(dl);

        clearBtn.addActionListener(e -> {
            resetFilters();
            applyFilters();
        });

        add(bar, BorderLayout.NORTH);
    }

    private void buildTable() {
        tableModel = new DefaultTableModel(new Object[]{"Type", "Amount", "Category", "Description", "Date"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void buildBottomBar(Runnable onBack) {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> onBack.run());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ── data loading ──────────────────────────────────────────────────────────

    public void loadTransactions() {
        tableModel.setRowCount(0);

        List<BaseTransaction> transactions = service.getAllByUser(userId);

        // Stream API: add rows + collect distinct category names in one pass
        Set<String> categories = transactions.stream()
                .peek(tx -> tableModel.addRow(new Object[]{
                        tx.getType().name(),
                        tx.getAmount(),
                        tx.getCategory().getCategoryName(),
                        tx.getDescription(),
                        formatDate(tx.getDate())
                }))
                .map(tx -> tx.getCategory().getCategoryName())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        refreshCategoryCombo(categories);
        applyFilters(); // re‑apply filters after reload
    }

    private String formatDate(Object dateObj) {
        if (dateObj == null) return "";
        if (dateObj instanceof java.util.Date d) {
            return DATE_FMT.format(d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        if (dateObj instanceof LocalDate ld) {
            return DATE_FMT.format(ld);
        }
        if (dateObj instanceof LocalDateTime ldt) {
            return DATE_FMT.format(ldt.toLocalDate());
        }
        return dateObj.toString();
    }

    private void refreshCategoryCombo(Set<String> categoryNames) {
        String prevSelection = (String) categoryFilter.getSelectedItem();
        categoryFilter.removeAllItems();
        categoryFilter.addItem("All");
        categoryNames.forEach(categoryFilter::addItem);
        categoryFilter.setSelectedItem(prevSelection != null ? prevSelection : "All");
    }

    // ── live filtering logic ─────────────────────────────────────────────────

    private void applyFilters() {
        List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();

        // type filter
        String type = (String) typeFilter.getSelectedItem();
        if (type != null && !"All".equals(type)) {
            filters.add(RowFilter.regexFilter(type, 0));
        }

        // category filter
        String category = (String) categoryFilter.getSelectedItem();
        if (category != null && !"All".equals(category)) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(category) + "$", 2));
        }

        // date range filter
        LocalDate min = parseFilterDate(minDateField.getText());
        LocalDate max = parseFilterDate(maxDateField.getText());

        if (min != null || max != null) {
            filters.add(new RowFilter<>() {
                @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    try {
                        LocalDate d = LocalDate.parse((String) entry.getValue(4), DATE_FMT);
                        return (min == null || !d.isBefore(min)) && (max == null || !d.isAfter(max));
                    } catch (DateTimeParseException e) { return false; }
                }
            });
        }

        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private LocalDate parseFilterDate(String text) {
        if (text == null || text.isBlank()) return null;
        try {
            return LocalDate.parse(text.trim(), DATE_FMT);
        } catch (DateTimeParseException e) {
            return null; // ignore invalid input
        }
    }

    private void resetFilters() {
        typeFilter.setSelectedIndex(0);
        categoryFilter.setSelectedIndex(0);
        minDateField.setText("");
        maxDateField.setText("");
        sorter.setRowFilter(null);
    }

    // ── user context change ─────────────────────────────────────────────────--

    public void setUserId(int userId) {
        this.userId = userId;
        loadTransactions();
        resetFilters();
    }
}
