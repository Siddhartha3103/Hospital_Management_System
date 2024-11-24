package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;

public class RegisterForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    @SuppressWarnings("unused")
    public RegisterForm() {
        setTitle("Register");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        // setUndecorated(true); // Remove title bar and borders for full-screen effect
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the window

        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        add(mainPanel);

        // GridBagConstraints for controlling the layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around the components

        // Title label
        JLabel titleLabel = new JLabel("Register", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x4CAF50)); // Green color for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Username field
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        usernamePanel.add(usernameField);
        gbc.gridy = 1;
        mainPanel.add(usernamePanel, gbc);

        // Password field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        passwordPanel.add(passwordField);
        gbc.gridy = 2;
        mainPanel.add(passwordPanel, gbc);

        // Register Button with rounded corners
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(0x4CAF50)); // Green color
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        registerButton.setPreferredSize(new Dimension(200, 40));
        registerButton.addActionListener(this::handleRegister);
        gbc.gridy = 3;
        mainPanel.add(registerButton, gbc);

        // Back Button with rounded corners
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setForeground(new Color(0x4CAF50)); // Green color
        backButton.setBackground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
        gbc.gridy = 4;
        mainPanel.add(backButton, gbc);

        // Set the background image (optional)
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = new ImageIcon("background.jpg").getImage(); // Optional background image
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        add(backgroundPanel);

        // Set an optional title icon
        setIconImage(new ImageIcon("icon.png").getImage());
    }

    private void handleRegister(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File("users.xml");
            Document document;
            Element root;

            if (file.exists()) {
                // Load existing XML
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                document = dBuilder.parse(file);
                root = document.getDocumentElement();
            } else {
                // Create new XML document
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                document = dBuilder.newDocument();
                root = document.createElement("Users");
                document.appendChild(root);
            }

            // Check if the username already exists
            NodeList users = root.getElementsByTagName("User");
            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);
                String existingUsername = user.getElementsByTagName("Username").item(0).getTextContent();
                if (existingUsername.equals(username)) {
                    JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Add new User node
            Element user = document.createElement("User");

            Element usernameElement = document.createElement("Username");
            usernameElement.appendChild(document.createTextNode(username));
            user.appendChild(usernameElement);

            Element passwordElement = document.createElement("Password");
            passwordElement.appendChild(document.createTextNode(password));
            user.appendChild(passwordElement);

            root.appendChild(user);

            // Save the updated XML to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(this, "User registered successfully!");
            new LoginForm().setVisible(true);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during registration!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
