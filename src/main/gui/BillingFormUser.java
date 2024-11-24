package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
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

public class BillingFormUser extends JFrame {
    private JTextField usernameField, patientIdField, amountField, dateField;

    @SuppressWarnings("unused")
    public BillingFormUser(String username) {
        setTitle("Billing Form");
        setSize(400, 300); // Adjusted size for proper fit
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        // GridBagConstraints object for layout control
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for the components

        // Title label
        JLabel titleLabel = new JLabel("Billing Form", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x4CAF50)); // Green color for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Username field (non-editable)
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(username);
        usernameField.setEditable(false);
        usernamePanel.add(usernameField);
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(usernamePanel, gbc);

        // Cancel Button with rounded corners
        JButton cancelButton = new JButton("Back");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setForeground(new Color(0x4CAF50)); // Green color
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        cancelButton.setPreferredSize(new Dimension(200, 40));
        cancelButton.addActionListener(e -> dispose());
        gbc.gridy = 6;
        mainPanel.add(cancelButton, gbc);

        // View Patients button with rounded corners
        JButton viewButton = new JButton("View Billings");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewButton.setForeground(Color.WHITE);
        viewButton.setBackground(new Color(0x4CAF50)); // Green color
        viewButton.setFocusPainted(false);
        viewButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        viewButton.setPreferredSize(new Dimension(200, 40));
        viewButton.addActionListener(this::handleViewPatients);
        gbc.gridy = 7;
        mainPanel.add(viewButton, gbc);

        // Set the background image for the entire frame (if any)
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = new ImageIcon("background.jpg").getImage(); // Add a background image file
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        add(backgroundPanel);

        // Set a subtle title icon (if any)
        setIconImage(new ImageIcon("icon.png").getImage());
    }

    private void handleSave(ActionEvent e) {
        String username = usernameField.getText().trim();
        String patientId = patientIdField.getText().trim();
        String amount = amountField.getText().trim();
        String date = dateField.getText().trim();

        if (patientId.isEmpty() || amount.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File("billing.xml");
            Document document;
            Element root;

            if (file.exists()) {
                // Load existing XML
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                document = dBuilder.parse(file);
                document.getDocumentElement().normalize();
                root = document.getDocumentElement();
            } else {
                // Create new XML document
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                document = dBuilder.newDocument();
                root = document.createElement("Bills");
                document.appendChild(root);
            }

            // Add new Billing node
            Element bill = document.createElement("Bill");

            Element usernameElement = document.createElement("Username");
            usernameElement.appendChild(document.createTextNode(patientId));
            bill.appendChild(usernameElement);

            Element patientIdElement = document.createElement("PatientID");
            patientIdElement.appendChild(document.createTextNode(patientId));
            bill.appendChild(patientIdElement);

            Element amountElement = document.createElement("Amount");
            amountElement.appendChild(document.createTextNode(amount));
            bill.appendChild(amountElement);

            Element dateElement = document.createElement("Date");
            dateElement.appendChild(document.createTextNode(date));
            bill.appendChild(dateElement);

            root.appendChild(bill);

            // Save to XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(this, "Bill saved successfully!");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving bill!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle the "View Patients" button click
    // Handle the "View Patients" button click
    private void handleViewPatients(ActionEvent e) {
        try {
            File file = new File("billing.xml");

            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "No bills available!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Load the billing XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();

            // Extract billing data
            NodeList bills = document.getElementsByTagName("Bill");

            // Prepare the text to display
            StringBuilder billList = new StringBuilder();
            for (int i = 0; i < bills.getLength(); i++) {
                Element bill = (Element) bills.item(i);

                // Make sure each element is present before accessing
                String username = getElementText(bill, "Username");

                // Only show bills for the current user
                if (username.equals(usernameField.getText())) {
                    String patientId = getElementText(bill, "PatientID");
                    String amount = getElementText(bill, "Amount");
                    String date = getElementText(bill, "Date");

                    billList.append("Username: ").append(username).append("\n")
                            .append("Amount: ").append(amount).append("\n")
                            .append("Date: ").append(date).append("\n\n");
                }
            }

            // Display in a new JFrame if any reports are found
            if (billList.length() > 0) {
                JFrame viewFrame = new JFrame("Your Billing Information");
                JTextArea textArea = new JTextArea(billList.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                viewFrame.add(scrollPane);
                viewFrame.setSize(400, 300);
                viewFrame.setLocationRelativeTo(null);
                viewFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No bills found for your username.", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading billing data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Utility method to safely retrieve element text content
    private String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "N/A"; // Return a default value if element is not found
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BillingFormUser("currentUsername").setVisible(true); // Replace "currentUsername" with actual username
        });
    }
}
