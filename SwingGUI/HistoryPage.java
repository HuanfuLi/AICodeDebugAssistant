package com.ai_assistant.api.model.SwingGUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.ai_assistant.api.model.Client;
import com.ai_assistant.api.model.Prompt;
import com.ai_assistant.api.model.RecordRetriever;

public class HistoryPage extends JPanel implements ClientHandler {

    private JComboBox<String> functionFilterComboBox;
    private JTable historyTable;
    JLabel filterLabel;
    private DefaultTableModel tableModel;
    private MainPanel mainPanel;
    private Client client;
    Locale currentLocale;
    ResourceBundle bundle;
    private RecordRetriever recordRetriever;

    private String[] columnNames;

    public HistoryPage(MainPanel panel) {
        currentLocale = new Locale("en", "US"); //Default Locale
        bundle = ResourceBundle.getBundle("LocaleBundle", currentLocale);
        columnNames = new String[]{bundle.getString("colNameInput"), bundle.getString("colNameOutput"),
                                bundle.getString("colNameSelection"), bundle.getString("colNameTime")};;

        this.mainPanel = panel;
        this.recordRetriever = new RecordRetriever();
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterLabel = new JLabel(bundle.getString("lblFunctionFilter"));
        String[] functions = {bundle.getString("selection5"), bundle.getString("selection1"), 
            bundle.getString("selection2"), bundle.getString("selection3"), bundle.getString("selection4")};
        functionFilterComboBox = new JComboBox<>(functions);
        filterPanel.add(filterLabel);
        filterPanel.add(functionFilterComboBox);
        add(filterPanel, BorderLayout.NORTH);

        tableModel = new NonEditableTableModel(null, columnNames);
        historyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane, BorderLayout.CENTER);

        //NOTE: Does not load any data upon initialization. Wait for client to be set.

        //Monitor filter dropdown list actions to load table.
        functionFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Only load data if client is not null
                if (client != null) {
                    int selectedFunction = functionFilterComboBox.getSelectedIndex();
                    loadHistoryData(selectedFunction);
                }
            }
        });

        //Monitor double click actions on table rows
        historyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int row = historyTable.getSelectedRow();
                    if (row != -1) {
                        String input = (String) tableModel.getValueAt(row, 0);
                        String output = (String) tableModel.getValueAt(row, 1);
                        String selection = (String) tableModel.getValueAt(row, 2);
                        System.out.println("<CLIENT> Double clicked.");
                        showHistoryDetail(input, output, getSelectionIndex(selection));
                    }
                }
            }
        });
    }

    //Helper function used to extract selection index for re-send functionality
    private int getSelectionIndex(String selectionStr){
        switch (selectionStr) {
            case "Hint":
                return 0;
            case "Suggestion":
                return 1;
            case "Debug":
                return 2;
            case "Generic":
                return 3;
            default:
                throw new AssertionError();
        }
    }

    private void loadHistoryData(int function) {
        if (client == null) {
            System.err.println("<CLIENT> Waiting for Client setup, cannot show history.");
            tableModel.setRowCount(0); //Empty the table
            return;
        }

        try {
            int functionIndex = getFunctionIndex(function);
            ResultSet rs = recordRetriever.getTable(client.getUID(), functionIndex);
            //Empty the current data in the table
            tableModel.setRowCount(0);
            //Update the table with new data
            while (rs.next()) {
                String selection = rs.getString("SELECTION");
                String content = rs.getString("CONTENT");
                String response = rs.getString("RESPONSE");
                Timestamp timeStamp = rs.getTimestamp("TIME_STAMP");
                Object[] rowData = {content, response, selection, timeStamp};
                tableModel.addRow(rowData);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Reversed method of getSelection. This helps database lookup.
    //Sequence is changed to make the interaction more intuitive ("All" placed at first).
    private int getFunctionIndex(int function) {
        switch (function) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 0:
                return 5; //Set to all by default
            default: // "All"
                return 5; //Set to all by default
        }
    }

    private void showHistoryDetail(String input, String output, int selectedFunction) {
        //Use JDialog as new window for history detail
        JDialog detailDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                                         bundle.getString("historyDetailTitle"), 
                                         true);
        detailDialog.setLayout(new BorderLayout(5, 5));
        detailDialog.setSize(800, 600);
        detailDialog.setLocationRelativeTo(detailDialog.getParent());
    
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
    
        JTextArea inputTextArea = new JTextArea(input);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(inputTextArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder(bundle.getString("inputBorder")));
        
        JTextArea outputTextArea = new JTextArea(output.replace("\\n", "\n"));//Format response by replacing literal line separator
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        JScrollPane outputScroll = new JScrollPane(outputTextArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder(bundle.getString("outputBorder")));
    
        splitPane.setLeftComponent(inputScroll);
        splitPane.setRightComponent(outputScroll);
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton resendButton = new JButton(bundle.getString("historyResendButton"));
        JButton closeButton = new JButton(bundle.getString("historyCloseButton"));
    
        resendButton.addActionListener(e -> {
            Prompt prompt = new Prompt(client.getUID(), selectedFunction, inputTextArea.getText(), null);
            String newOutput = client.runConnection(8189, "127.0.0.1", prompt);
            outputTextArea.setText(newOutput.replace("\\n", "\n"));
        });
    
        closeButton.addActionListener(e -> detailDialog.dispose());
    
        buttonPanel.add(resendButton);
        buttonPanel.add(closeButton);
    
        detailDialog.add(splitPane, BorderLayout.CENTER);
        detailDialog.add(buttonPanel, BorderLayout.SOUTH);
    
        detailDialog.setVisible(true);
    }


    @Override
    public void setClient(Client client) {
        this.client = client;
        //Load history data after the client is setup
        loadHistoryData(functionFilterComboBox.getSelectedIndex());
    }

    @Override
    public void setLocale(String language, String country) {
        currentLocale = new Locale(language, country);
        bundle = ResourceBundle.getBundle("LocaleBundle", currentLocale);
        //Update locale in history
        String[] columnNames = {bundle.getString("colNameInput"), bundle.getString("colNameOutput"),
                                bundle.getString("colNameSelection"), bundle.getString("colNameTime")};
        tableModel.setColumnIdentifiers(columnNames);
        //Update selection
        filterLabel.setText(bundle.getString("lblFunctionFilter"));
        String[] functions = {bundle.getString("selection5"), bundle.getString("selection1"), 
        bundle.getString("selection2"), bundle.getString("selection3"), bundle.getString("selection4")};
        DefaultComboBoxModel<String> newModel = new DefaultComboBoxModel<>(functions);
        functionFilterComboBox.setModel(newModel);
    }
}