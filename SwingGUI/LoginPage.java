package com.ai_assistant.api.model.SwingGUI;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.ai_assistant.api.model.Client;

public class LoginPage extends JPanel {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JComboBox localeSelection;
    String[] locales = {"English (US)", "简体中文 (中国大陆)", "Français"};
    private MainFrame mainFrame;
    private JLabel usernameLabel, passwordLabel;
    private String language, country;
    private Locale locale;
    ResourceBundle bundle;

    protected  static final String JDBC_URL = "jdbc:mysql://localhost:3306/ai_assistant_database";
    protected  static final String USERNAME = "root";
    protected  static final String PASSWORD = "rootroot";

    public LoginPage(MainFrame frame) {
        language = "en";
        country = "US";
        locale = new Locale(language, country); //Set default locale to US English
        bundle = ResourceBundle.getBundle("LocaleBundle", locale);
        this.mainFrame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameLabel = new JLabel(bundle.getString("lblUID"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernameLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(emailField, gbc);

        passwordLabel = new JLabel(bundle.getString("lblPwd"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        loginButton = new JButton(bundle.getString("btnLogin"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Login logics
                System.out.println("<CLIENT> Login attempted: " + emailField.getText() + ", " + new String(passwordField.getPassword()));
                String email = emailField.getText();
                String password = new String(passwordField.getPassword()).trim();

                //Authentication in DB
                try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                    String sql = "SELECT UID, PASSWORD FROM USER WHERE EMAIL = ?";
                    System.out.println(sql);
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, email);
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                            String storedPassword = rs.getString("PASSWORD");
                            if (password.equals(storedPassword)) {
                                // Authentication successful
                                int UID = rs.getInt("UID");
                                System.out.println(UID);
                                Client client = new Client(UID);
                                mainFrame.setClient(client);
                                mainFrame.setLocale(language, country);
                                mainFrame.showCard("main");
                            }else {
                                // Wrong password
                                JOptionPane.showMessageDialog(LoginPage.this, bundle.getString("popErrorWrongPwd"), bundle.getString("popErrorTitle"), JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            // User not found
                            JOptionPane.showMessageDialog(LoginPage.this, bundle.getString("popErrorWrongUsr"), bundle.getString("popErrorTitle"), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // JOptionPane.showMessageDialog(LoginPage.this, 
                    //         bundle.getString("login.dbError"),
                    //         bundle.getString("login.error"),
                    //         JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(loginButton, gbc);

        registerButton = new JButton(bundle.getString("btnReg"));
        gbc.gridy = 3;
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setLocale(language, country);
                mainFrame.showCard("register");
            }
        });
        add(registerButton, gbc);

        localeSelection = new JComboBox<>(locales);
        gbc.gridy = 4;
        localeSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = localeSelection.getSelectedIndex();
                switch (index) {
                    case 0:
                        language = "en";
                        country = "US";
                        break;
                    case 1:
                        language = "zh";
                        country = "CN";
                        break;
                    case 2:
                        language = "fr";
                        country = "FR";
                        break;
                    default:
                        language = "en";
                        country = "US";
                        break;
                }
                locale = new Locale(language, country);
                updateUI(locale);
            }
        });
        add(localeSelection, gbc);
    }// End Constructor
    public void updateUI(Locale locale){
        this.locale = locale;
        bundle = ResourceBundle.getBundle("LocaleBundle", locale);
        usernameLabel.setText(bundle.getString("lblUID"));
        passwordLabel.setText(bundle.getString("lblPwd"));
        loginButton.setText(bundle.getString("btnLogin"));
        registerButton.setText(bundle.getString("btnReg"));
        revalidate();
        repaint();
    }
}