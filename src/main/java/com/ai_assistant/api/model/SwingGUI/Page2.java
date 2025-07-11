package com.ai_assistant.api.model.SwingGUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.ai_assistant.api.model.Client;
import com.ai_assistant.api.model.Prompt;

public class Page2 extends JPanel implements ClientHandler {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JComboBox<String> functionComboBox;
    private JButton sendButton;
    private JSplitPane splitPane;
    private Client client;
    Locale currentLocale;
    ResourceBundle bundle;
    TitledBorder inputAreaBorder, outputAreaBorder;

    public Page2() {
        currentLocale = new Locale("en", "US");
        bundle = ResourceBundle.getBundle("LocaleBundle", currentLocale);

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Use split pane to provide better view of information in GUI since it needs to show 2 pages at the same time
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        inputTextArea = new JTextArea(10, 25);
        inputAreaBorder = BorderFactory.createTitledBorder(bundle.getString("inputBorder"));
        inputTextArea.setBorder(inputAreaBorder);
        splitPane.setLeftComponent(new JScrollPane(inputTextArea));

        outputTextArea = new JTextArea(10, 25);
        outputTextArea.setEditable(false);
        outputAreaBorder = BorderFactory.createTitledBorder(bundle.getString("outputBorder"));
        outputTextArea.setBorder(outputAreaBorder);
        outputTextArea.setLineWrap(true); //Set line break functionality
        outputTextArea.setWrapStyleWord(true);
        splitPane.setRightComponent(new JScrollPane(outputTextArea));

        add(splitPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] functions = {"Hint", "Suggestion", "Debug", "Generic"};
        functionComboBox = new JComboBox<>(functions);
        sendButton = new JButton(bundle.getString("btnSend"));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedFunction = functionComboBox.getSelectedIndex();
                Prompt prompt = new Prompt(client.getUID(), selectedFunction, inputTextArea.getText(), null);
                //Send request logics below
                String output = client.runConnection(8189, "127.0.0.1", prompt);
                //Parse the AI output to format line changes
                String formattedResponse = output.replace("\\n", "\n");//Replace literal line separator with actual line separator for formatting
                outputTextArea.setText(formattedResponse);
                System.out.println("Client Debug: Request completed.");
            }
        });
        controlPanel.add(functionComboBox);
        controlPanel.add(sendButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    //Update client and locale information
    @Override
    public void setClient(Client client) {
        this.client = client;
    }
    @Override
    public void setLocale(String language, String country) {
        currentLocale = new Locale(language, country);
        bundle = ResourceBundle.getBundle("LocaleBundle", currentLocale);
        String[] functions = {bundle.getString("selection1"), bundle.getString("selection2"), bundle.getString("selection3"), bundle.getString("selection4")};
        DefaultComboBoxModel<String> newModel = new DefaultComboBoxModel<>(functions);
        functionComboBox.setModel(newModel);
        sendButton.setText(bundle.getString("btnSend"));
        inputAreaBorder.setTitle(bundle.getString("inputBorder"));
        outputAreaBorder.setTitle(bundle.getString("outputBorder"));
        splitPane.repaint();
    }
}