package com.ai_assistant.api.model.SwingGUI;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.ai_assistant.api.model.Client;
import com.formdev.flatlaf.FlatDarculaLaf;

public class MainFrame extends JFrame implements ClientHandler, ActionListener {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel menuPanel;
    private Map<String, Component> cards;

    private LoginPage loginPage;
    private RegisterPage registerPage;
    private MainPanel mainPanel;

    //Set up menu system
    private JMenuBar menubar;
    private JMenu toolMenu, aboutMenu;
    private JMenuItem exitItem, logoutItem, reportItem, aboutItem;
    private JWindow aboutWindow;
    private JLabel lblDesc;
    private JButton btnBack, btnSponsor;

    private Locale currentLocale;
    ResourceBundle bundle;

    Client client;

    public MainFrame() {
        
        currentLocale = new Locale("en", "US"); //Default locale set to US English
        bundle = ResourceBundle.getBundle("LocaleBundle", currentLocale);
        setTitle(bundle.getString("title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        //Add Menubar system
        menubar = new JMenuBar();
        setJMenuBar(menubar);
        toolMenu = new JMenu(bundle.getString("toolMenu"));
        menubar.add(toolMenu);
        aboutMenu = new JMenu(bundle.getString("aboutMenu"));
        menubar.add(aboutMenu);

        //Setup Tools menu
        exitItem = new JMenuItem(bundle.getString("exitItem"));
        exitItem.addActionListener(this);
        toolMenu.add(exitItem);
        logoutItem = new JMenuItem(bundle.getString("logoutItem"));
        logoutItem.addActionListener(this);
        toolMenu.add(logoutItem);

        //Setup About menu
        reportItem = new JMenuItem(bundle.getString("reportItem"));
        reportItem.addActionListener(this);
        aboutMenu.add(reportItem);
        aboutItem = new JMenuItem(bundle.getString("aboutItem"));
        aboutItem.addActionListener(this);
        aboutMenu.add(aboutItem);

        //Add a popup window for about operations
        aboutWindow = new JWindow(this);
        aboutWindow.getContentPane().setLayout(new BorderLayout());
        lblDesc = new JLabel(bundle.getString("aboutDesc"));
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER); //Center the text
        lblDesc.setBorder(new EmptyBorder(15, 15, 15, 15)); //Add some padding around the text

        btnSponsor = new JButton(bundle.getString("btnSponsor"));
        btnSponsor.addActionListener(this);
        btnBack = new JButton(bundle.getString("btnBack"));
        btnBack.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); //Use FlowLayout for buttons
        buttonPanel.add(btnSponsor);
        buttonPanel.add(btnBack);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 0)); //Add padding below buttons

        aboutWindow.getContentPane().add(lblDesc, BorderLayout.CENTER);
        aboutWindow.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        aboutWindow.setMinimumSize(new Dimension(300, 150)); //Set a minimum size
        aboutWindow.pack();

        //Use a hashmap to store the card page objects: login, register, main
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        cards = new HashMap<>();

        //Left side menu panel that stores buttons to navigate through pages
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(150, getHeight()));

        client = new Client(0);//Placeholder for client object
        loginPage = new LoginPage(this);
        registerPage = new RegisterPage(this);
        mainPanel = new MainPanel(this);

        cards.put("login", loginPage);
        cards.put("register", registerPage);
        cards.put("main", mainPanel);

        contentPanel.add(loginPage, "login");
        contentPanel.add(registerPage, "register");
        contentPanel.add(mainPanel, "main");

        add(contentPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.WEST);

        cardLayout.show(contentPanel, "login");

        setVisible(true);
    }

    public void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
        if (cardName.equals("main")) {
            updateMenu();
        } else {
            menuPanel.removeAll();
            menuPanel.revalidate();
            menuPanel.repaint();
        }
    }

    //Update and pass client and locale information to next layer
    @Override
    public void setClient(Client client){
        this.client = client;
        mainPanel.setClient(client);
        showCard("main");
    }
    @Override
    public void setLocale(String language, String country){
        this.currentLocale = new Locale(language,country);
        bundle = ResourceBundle.getBundle("LocaleBundle", currentLocale);
        setTitle(bundle.getString("title"));
        updateMenu();
        mainPanel.setLocale(language, country);
        registerPage.setLocale(language, country);
    }

    private void updateMenu() {
        menuPanel.removeAll();
        addButton(bundle.getString("page1"), e -> mainPanel.showPage("page1"));
        addButton(bundle.getString("page2"), e -> mainPanel.showPage("page2"));
        addButton(bundle.getString("page3"), e -> mainPanel.showPage("history"));
        //Create a glue between page navigator buttons and profile/setting buttons
        menuPanel.add(Box.createVerticalGlue());
        //NOTE: userPage and settingPage are in WIP status. Currently using historyPage as placeholder.
        addButton(bundle.getString("userPage"), e -> mainPanel.showPage("history"));
        addButton(bundle.getString("settingPage"), e -> mainPanel.showPage("history"));
        toolMenu.setText(bundle.getString("toolMenu"));
        aboutMenu.setText(bundle.getString("aboutMenu"));
        exitItem.setText(bundle.getString("exitItem"));
        logoutItem.setText(bundle.getString("logoutItem"));
        reportItem.setText(bundle.getString("reportItem"));
        aboutItem.setText(bundle.getString("aboutItem"));
        lblDesc.setText(bundle.getString("aboutDesc"));
        btnSponsor.setText(bundle.getString("btnSponsor"));
        btnBack.setText(bundle.getString("btnBack"));
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(listener);
        //Use the Short.Max_VALUE to make the buttons as wide as the panel
        button.setMaximumSize(new Dimension(Short.MAX_VALUE, button.getPreferredSize().height));
        menuPanel.add(button);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf()); // 设置 FlatLightLaf 主题
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(MainFrame::new);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitItem){
            System.exit(0);
        }

        if (e.getSource() == logoutItem){
            showCard("login");
        }

        if (e.getSource() == aboutItem){
            Point location = menubar.getLocationOnScreen();
            lblDesc.setText(bundle.getString("aboutDesc"));
            aboutWindow.setLocation(location);
            aboutWindow.setVisible(true);
        }

        if (e.getSource() == btnBack){
            aboutWindow.setVisible(false);
        }

        if (e.getSource() == btnSponsor){
            lblDesc.setText(bundle.getString("sponsorDesc"));
        }

        if (e.getSource() == reportItem){
            Point location = menubar.getLocationOnScreen();
            lblDesc.setText(bundle.getString("reportDesc"));
            aboutWindow.setLocation(location);
            aboutWindow.setVisible(true);
        }
    }
}