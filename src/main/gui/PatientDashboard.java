package main.gui;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class PatientDashboard extends JFrame {
    private JTextField usernameField;
    private String userId;

    @SuppressWarnings("unused")
    private String username;

    @SuppressWarnings("unused")
    public PatientDashboard(String username) {
        this.username = username; // Initialize the username

        setTitle("Patient Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the window

        // Main panel with rounded corners and shadow effect
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        add(mainPanel, BorderLayout.CENTER);

        // Create a GridBagConstraints object for layout control
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for the components

        // Title label
        JLabel titleLabel = new JLabel("Patient Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x4CAF50)); // Green color for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Doctor Button with rounded corners
        JButton doctorButton = new JButton("View Available Doctors");
        doctorButton.setFont(new Font("Arial", Font.PLAIN, 14));
        doctorButton.setForeground(Color.WHITE);
        doctorButton.setBackground(new Color(0x4CAF50)); // Green color
        doctorButton.setFocusPainted(false);
        doctorButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        doctorButton.setPreferredSize(new Dimension(200, 40));
        doctorButton.addActionListener(this::handleViewDoctors);
        gbc.gridy = 1;
        mainPanel.add(doctorButton, gbc);

        // Appointment Button with rounded corners
        JButton appointmentButton = new JButton("Manage Appointments");
        appointmentButton.setFont(new Font("Arial", Font.PLAIN, 14));
        appointmentButton.setForeground(Color.WHITE);
        appointmentButton.setBackground(new Color(0x4CAF50)); // Green color
        appointmentButton.setFocusPainted(false);
        appointmentButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        appointmentButton.setPreferredSize(new Dimension(200, 40));
        appointmentButton.addActionListener(e -> new AppointmentForm(username).setVisible(true)); // Pass username
        gbc.gridy = 2;
        mainPanel.add(appointmentButton, gbc);

        // Billing Button with rounded corners
        JButton billingButton = new JButton("View Billings");
        billingButton.setFont(new Font("Arial", Font.PLAIN, 14));
        billingButton.setForeground(Color.WHITE);
        billingButton.setBackground(new Color(0x4CAF50)); // Green color
        billingButton.setFocusPainted(false);
        billingButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        billingButton.setPreferredSize(new Dimension(200, 40));
        billingButton.addActionListener(e -> new BillingFormUser(username).setVisible(true));  // Pass username this::handleViewPatients
        // billingButton.addActionListener(this::handleViewPatients);  // Pass username this::handleViewPatients
        gbc.gridy = 4;
        mainPanel.add(billingButton, gbc);

        // Report Button with rounded corners
        JButton reportButton = new JButton("View Reports");
        reportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        reportButton.setForeground(Color.WHITE);
        reportButton.setBackground(new Color(0x4CAF50)); // Green color
        reportButton.setFocusPainted(false);
        reportButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        reportButton.setPreferredSize(new Dimension(200, 40));
        reportButton.addActionListener(e -> new ReportFormUser(username).setVisible(true));  // Pass username
        // reportButton.addActionListener(this::handleViewReports);  // Pass username
        gbc.gridy = 5;
        mainPanel.add(reportButton, gbc);

        // Logout Button with rounded corners
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.setForeground(new Color(0x4CAF50)); // Green color
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        logoutButton.addActionListener(this::handleLogout);
        gbc.gridy = 6;
        mainPanel.add(logoutButton, gbc);

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

    // Handle the "View Doctors" button click
    private void handleViewDoctors(ActionEvent e) {
        try {
            File file = new File("doctors.xml");

            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "No doctors available!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Load the doctors XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();

            // Extract doctor data
            NodeList doctors = document.getElementsByTagName("Doctor");

            // Prepare the text to display
            StringBuilder doctorList = new StringBuilder();
            for (int i = 0; i < doctors.getLength(); i++) {
                Element doctor = (Element) doctors.item(i);
                String name = doctor.getElementsByTagName("Name").item(0).getTextContent();
                String specialization = doctor.getElementsByTagName("Specialization").item(0).getTextContent();
                String contact = doctor.getElementsByTagName("Contact").item(0).getTextContent();

                doctorList.append("Name: ").append(name).append("\n")
                        .append("Specialization: ").append(specialization).append("\n")
                        .append("Contact: ").append(contact).append("\n\n");
            }

            // Display in a new JFrame
            JFrame viewFrame = new JFrame("Doctors");
            JTextArea textArea = new JTextArea(doctorList.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            viewFrame.add(scrollPane);
            viewFrame.setSize(400, 300);
            viewFrame.setLocationRelativeTo(null);
            viewFrame.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctor data!", "Error", JOptionPane.ERROR_MESSAGE);
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

    private String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "N/A"; // Return a default value if element is not found
    }

    private void handleLogout(ActionEvent e) {
        new LoginForm().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PatientDashboard("currentUsername").setVisible(true); // Pass the actual username here
        });
    }
}
