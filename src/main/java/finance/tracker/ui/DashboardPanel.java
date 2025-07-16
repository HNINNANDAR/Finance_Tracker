
package finance.tracker.ui;

import finance.tracker.model.BaseTransaction;
import finance.tracker.model.TransactionType;
import finance.tracker.repository.TransactionDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardPanel extends JPanel {
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color CARD_BORDER = new Color(208, 208, 208);
    private static final Color POSITIVE = new Color(46, 125, 50);
    private static final Color NEGATIVE = new Color(198, 40, 40);

    private final TransactionDAO dao;
    private final int userId;
    private YearMonth currentMonth;
    private final NumberFormat moneyFmt = NumberFormat.getCurrencyInstance();

    private final JLabel monthLabel;
    private final JLabel incomeValue;
    private final JLabel expenseValue;
    private final JLabel netValue;
    private ChartPanel chartPanel;

    public DashboardPanel(TransactionDAO dao, int userId) {
        this.dao = dao;
        this.userId = userId;
        this.currentMonth = YearMonth.now();

        // Initialize labels
        monthLabel = new JLabel();
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        incomeValue = new JLabel();
        incomeValue.setFont(new Font("SansSerif", Font.BOLD, 24));

        expenseValue = new JLabel();
        expenseValue.setFont(new Font("SansSerif", Font.BOLD, 24));

        netValue = new JLabel();
        netValue.setFont(new Font("SansSerif", Font.BOLD, 24));

        // Setup layout
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 245, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createSummaryPanel(), BorderLayout.NORTH);
        add(createChartPanel(), BorderLayout.CENTER);

        refresh();
    }

    private JPanel createSummaryPanel() {
        JPanel summary = new JPanel(new GridLayout(1, 3, 20, 0));
        summary.setOpaque(false);

        // Income Card
        summary.add(createSummaryCard("Income", incomeValue, "💰"));

        // Expense Card
        summary.add(createSummaryCard("Expenses", expenseValue, "💸"));

        // Net Balance Card
        summary.add(createSummaryCard("Net Balance", netValue, "📊"));

        return summary;
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel, String icon) {
        JPanel card = new JPanel(new GridLayout(3, 1, 0, 5));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(titleLabel);
        card.add(valueLabel);
        card.add(iconLabel);

        return card;
    }

    private JPanel createChartPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        // Initialize chart panel
        chartPanel = new ChartPanel(null);
        chartPanel.setBackground(CARD_BG);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));
        chartPanel.setPreferredSize(new Dimension(600, 400));

        wrapper.add(chartPanel, BorderLayout.CENTER);
        return wrapper;
    }

    public void setMonth(YearMonth month) {
        this.currentMonth = month;
        refresh();
    }

    public void refresh() {
        updateDisplayData();
        updateChart();
    }

    private void updateDisplayData() {
        BigDecimal income = dao.getTotalByUserAndTypeInMonth(userId, TransactionType.INCOME, currentMonth);
        BigDecimal expense = dao.getTotalByUserAndTypeInMonth(userId, TransactionType.EXPENSE, currentMonth);
        BigDecimal net = income.subtract(expense);

        incomeValue.setText(moneyFmt.format(income));
        expenseValue.setText(moneyFmt.format(expense));
        netValue.setText(moneyFmt.format(net));
        netValue.setForeground(net.signum() >= 0 ? POSITIVE : NEGATIVE);

        monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                " " + currentMonth.getYear());
    }

    private void updateChart() {
        List<BaseTransaction> transactions = dao.getAllByUser(userId).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .filter(t -> YearMonth.from(t.getDate()).equals(currentMonth))
                .toList();

        Map<String, Double> categoryTotals = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getCategoryName(),
                        Collectors.summingDouble(BaseTransaction::getAmount)
                ));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        categoryTotals.forEach((category, amount) ->
                dataset.addValue(amount, "Expenses", category));

        JFreeChart chart = ChartFactory.createBarChart(
                "Expenses by Category",
                "Category",
                "Amount",
                dataset,
                PlotOrientation.VERTICAL,
                false, // legend
                false, // tooltips
                false  // urls
        );

        // Customize chart appearance
        chart.setBackgroundPaint(CARD_BG);
        chart.getPlot().setBackgroundPaint(CARD_BG);

        chartPanel.setChart(chart);
        chartPanel.repaint();
    }

    /** Compatibility alias for MainFrame */
    public void updateForDate(YearMonth month) {
        setMonth(month);
    }

}