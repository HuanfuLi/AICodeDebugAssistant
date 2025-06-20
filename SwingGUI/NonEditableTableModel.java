package com.ai_assistant.api.model.SwingGUI;

import javax.swing.table.DefaultTableModel;

public class NonEditableTableModel extends DefaultTableModel {

    public NonEditableTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // 所有的单元格都不可编辑
        return false;
    }
}