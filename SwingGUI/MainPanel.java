package com.ai_assistant.api.model.SwingGUI;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import com.ai_assistant.api.model.Client;

public class MainPanel extends JPanel implements ClientHandler {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Map<String, Component> cards;
    private MainFrame mainFrame;
    private Client client;
    private Page1 page1;
    private Page2 page2;
    private HistoryPage historyPage;
    private Locale locale;
    ResourceBundle bundle;

    public MainPanel(MainFrame frame) {
        locale = new Locale("en", "US");
        bundle = ResourceBundle.getBundle("LocaleBundle", locale);
        this.mainFrame = frame;
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        cards = new HashMap<>();

        page1 = new Page1();
        page2 = new Page2();
        historyPage = new HistoryPage(this);

        //Insert pages into Hashmap for later showPage function
        cards.put("page1", page1);
        cards.put("page2", page2);
        cards.put("history", historyPage);

        contentPanel.add(page1, "page1");
        contentPanel.add(page2, "page2");
        contentPanel.add(historyPage, "history");

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        showPage("page1"); //Display page1 by default
    }

    //Show pages based on button actions in menuPanel of MainFrame
    public void showPage(String pageName) {
        cardLayout.show(contentPanel, pageName);
    }
    
    //Update and pass client and locale information to next layer
    @Override
    public void setClient(Client client){
        this.client = client;
        page1.setClient(client);
        page2.setClient(client);
        historyPage.setClient(client);
    }
    @Override
    public void setLocale(String language, String country){
        page1.setLocale(language, country);
        page2.setLocale(language, country);
        historyPage.setLocale(language, country);
    }
}