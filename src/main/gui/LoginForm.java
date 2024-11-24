package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import main.UserSession;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    @SuppressWarnings("unused")
    public LoginForm() {
        setTitle("Hospital Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set background color for the entire frame
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(new Color(255, 204, 203)); // Light green background
        setContentPane(backgroundPanel);

        // Top Panel for Logo
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Add logo here using JLabel with an ImageIcon
        JLabel logoLabel = new JLabel("IHC"); // Replace "LOGO" with your image icon
        logoLabel.setFont(new Font("Arial", Font.BOLD,20)); // Placeholder text
        topPanel.add(logoLabel);
        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 255, 250)); // Light Mint Cream
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Heading
        JLabel headingLabel = new JLabel("Hospital Management System", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0x4CAF50)); // Green
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(headingLabel, gbc);

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(usernameLabel, gbc);
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50), 2));
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50), 2));
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0x4CAF50)); // Green
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.addActionListener(this::handleLogin);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.setForeground(new Color(0x4CAF50)); // Green
        registerButton.setBackground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        registerButton.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });
        gbc.gridy = 4;
        mainPanel.add(registerButton, gbc);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File("users.xml");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "No users registered yet. Please register first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);

            NodeList users = document.getElementsByTagName("User");
            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);
                String storedUsername = user.getElementsByTagName("Username").item(0).getTextContent();
                String storedPassword = user.getElementsByTagName("Password").item(0).getTextContent();

                if (storedUsername.equals(username) && storedPassword.equals(password)) {
                    JOptionPane.showMessageDialog(this, "Login successful!");

                    UserSession.setUsername(username);

                    if ("admin".equalsIgnoreCase(username)) {
                        new MainDashboard(username).setVisible(true);
                    } else {
                        new PatientDashboard(username).setVisible(true);
                    }

                    dispose();
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
