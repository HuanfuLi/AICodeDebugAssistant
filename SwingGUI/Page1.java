package com.ai_assistant.api.model.SwingGUI;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ai_assistant.api.model.Client;
import com.ai_assistant.api.model.FileExtractor;
import com.ai_assistant.api.model.Prompt;

public class Page1 extends JPanel implements ActionListener, ClientHandler{

    private JTextField filePathField;
    private JTextArea responseTextArea;
    private JComboBox<String> functionComboBox;
    private JButton sendButton, overwriteButton;
    private JLabel filePathLabel;
    private Client client;
    private Locale currentLocale;
    ResourceBundle bundle;

    public Page1() {
        currentLocale = new Locale("en", "US");
        bundle = ResourceBundle.getBundle("LocaleBundle", currentLocale);
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePathLabel = new JLabel(bundle.getString("lblPath"));
        filePathField = new JTextField(30);
        inputPanel.add(filePathLabel);
        inputPanel.add(filePathField);
        add(inputPanel, BorderLayout.NORTH);

        responseTextArea = new JTextArea(10, 50);
        responseTextArea.setEditable(true); //Allow user to modify response before overwrite
        responseTextArea.setLineWrap(true);//Set automatic line changing according to size of panel
        responseTextArea.setWrapStyleWord(true);//Break the lines by word boundaries
        add(new JScrollPane(responseTextArea), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] functions = {bundle.getString("selection1"), bundle.getString("selection2"), bundle.getString("selection3"), bundle.getString("selection4")};
        functionComboBox = new JComboBox<>(functions);
        sendButton = new JButton(bundle.getString("btnSend"));
        sendButton.addActionListener(this);
        overwriteButton = new JButton(bundle.getString("btnOverride"));
        overwriteButton.addActionListener(this);
        controlPanel.add(functionComboBox);
        controlPanel.add(sendButton);
        controlPanel.add(overwriteButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    //Update client and locale information
    @Override
    public void setClient(Client client) {
        this.client = client;
    }
    @Override
    public void setLocale(String language, String country){
        this.currentLocale = new Locale(language, country);
        bundle = ResourceBundle.getBundle("LocaleBundle", currentLocale);
        filePathLabel.setText(bundle.getString("lblPath"));
        sendButton.setText(bundle.getString("btnSend"));
        overwriteButton.setText(bundle.getString("btnOverride"));
        //Update the function selection comboBox
        String[] functions = {bundle.getString("selection1"), bundle.getString("selection2"), bundle.getString("selection3"), bundle.getString("selection4")};
        DefaultComboBoxModel<String> newModel = new DefaultComboBoxModel<>(functions);
        functionComboBox.setModel(newModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton){
            String filePath = filePathField.getText();
            FileExtractor file = new FileExtractor(filePath);
            String question = file.getContent();
            //Indexes of functions: 0. Hint 1. Suggestion 2. Debug 3. Generic
            int selectedFunction = functionComboBox.getSelectedIndex();
            Prompt prompt = new Prompt(client.getUID(), selectedFunction, question, null);
            //Hard coded port and address for test
            String response = client.runConnection(8189, "127.0.0.1", prompt);
            System.out.println("<CLIENT> Received Response. ");
            String formattedResponse = response.replace("\\n", "\n");//Replace literal line separator with actual line separator for formatting
            responseTextArea.setText(formattedResponse);
            System.out.println("<CLIENT> Request completed.");
        } else if (e.getSource() == overwriteButton){
            String filePath = filePathField.getText();
            String lines = responseTextArea.getText();

            try (FileWriter writer = new FileWriter(filePath)){
                writer.write(lines);
            } catch(IOException e1){
                System.out.println("<CLIENT> Error Message: " + e1.getMessage());
                e1.printStackTrace();
            }
        }
    }
}