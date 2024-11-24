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

public class ReportFormUser extends JFrame {
    private JTextField patientIdField, reportDetailsField;
    private String userId; // Track user session ID (e.g., from login)

    @SuppressWarnings("unused")
    public ReportFormUser(String userId) {
        this.userId = userId;
        userId="";
        // Set window properties
        setTitle("Report Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the window

        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        add(mainPanel);

        // GridBagConstraints for controlling layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components

        // Title label
        JLabel titleLabel = new JLabel("View Your Reports", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x4CAF50)); // Green color for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Cancel Button with rounded corners
        JButton cancelButton = new JButton("Back");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setForeground(new Color(0x4CAF50)); // Green color
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        cancelButton.setPreferredSize(new Dimension(200, 40));
        cancelButton.addActionListener(e -> dispose());
        gbc.gridy = 5;
        mainPanel.add(cancelButton, gbc);

        // View Reports Button with rounded corners
        JButton viewButton = new JButton("View Reports");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewButton.setForeground(new Color(0x4CAF50)); // Green color
        viewButton.setBackground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        viewButton.setPreferredSize(new Dimension(200, 40));
        viewButton.addActionListener(this::handleViewReports);
        gbc.gridy = 6;
        mainPanel.add(viewButton, gbc);

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

    private void handleSave(ActionEvent e) {
        String patientId = patientIdField.getText().trim();
        String reportDetails = reportDetailsField.getText().trim();

        if (patientId.isEmpty() || reportDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File("reports_" + userId + ".xml"); // Save reports per user session
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
                root = document.createElement("Reports");
                document.appendChild(root);
            }

            // Add new Report node
            Element report = document.createElement("Report");

            Element patientIdElement = document.createElement("PatientID");
            patientIdElement.appendChild(document.createTextNode(patientId));
            report.appendChild(patientIdElement);

            Element detailsElement = document.createElement("Details");
            detailsElement.appendChild(document.createTextNode(reportDetails));
            report.appendChild(detailsElement);

            root.appendChild(report);

            // Save to XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(this, "Report saved successfully!");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving report!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle the "View Reports" button click
    private void handleViewReports(ActionEvent e) {
        try {
            File file = new File("reports_" + userId + ".xml");

            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "No reports available!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Load the reports XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();

            // Extract report data
            NodeList reports = document.getElementsByTagName("Report");

            // Prepare the text to display
            StringBuilder reportList = new StringBuilder();
            reportList.append("User ID: ").append(userId).append("\n\n"); // Displaying username

            for (int i = 0; i < reports.getLength(); i++) {
                Element report = (Element) reports.item(i);

                // Avoid NullPointerException: check if the nodes exist
                String patientId = null;
                String details = null;

                NodeList patientIdNodes = report.getElementsByTagName("PatientID");
                if (patientIdNodes.getLength() > 0) {
                    patientId = patientIdNodes.item(0).getTextContent();
                }

                NodeList detailsNodes = report.getElementsByTagName("Details");
                if (detailsNodes.getLength() > 0) {
                    details = detailsNodes.item(0).getTextContent();
                }

                // Append only if the data is present
                if (patientId != null && details != null) {
                    reportList.append("Patient ID: ").append(patientId).append("\n")
                            .append("Details: ").append(details).append("\n\n");
                }
            }

            // Display in a new JFrame if any reports are found
            if (reportList.length() > 0) {
                JFrame viewFrame = new JFrame("Reports");
                JTextArea textArea = new JTextArea(reportList.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                viewFrame.add(scrollPane);
                viewFrame.setSize(400, 300);
                viewFrame.setLocationRelativeTo(null);
                viewFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No valid reports found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading report data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Example: pass user ID to instantiate the form
            new ReportFormUser("user123").setVisible(true);
        });
    }
}
