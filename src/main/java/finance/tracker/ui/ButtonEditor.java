package finance.tracker.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean clicked;
    private int row, col;
    private JTable table;
    private ManageCategoryPanel parentPanel;

    public ButtonEditor(JCheckBox checkBox, ManageCategoryPanel parent) {
        super(checkBox);
        this.parentPanel = parent;

        button = new JButton();
        button.setOpaque(true);

        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {

        this.table = table;
        this.row = row;
        this.col = column;

        label = (value == null) ? "" : value.toString();
        button.setText(label);
        clicked = true;

        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            if (col == 2) {
                // Edit clicked
                parentPanel.editCategory(row);
            } else if (col == 3) {
                // Delete clicked
                parentPanel.deleteCategory(row);
            }
        }
        clicked = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
