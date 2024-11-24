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

public class AppointmentForm extends JFrame {
    private JTextField usernameField, patientField, doctorField, dateField;

    @SuppressWarnings("unused")
    public AppointmentForm(String username) {
        setTitle("Manage Appointments");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Add the navbar
        // NavbarPanel navbar = new NavbarPanel(this::handleNavClick);
        // add(navbar, BorderLayout.NORTH);

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
        JLabel titleLabel = new JLabel("Manage Appointments", JLabel.CENTER);
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
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(usernamePanel, gbc);

        // Patient Name field
        JPanel patientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        patientPanel.add(new JLabel("Patient Name:"));
        patientField = new JTextField(20);
        patientPanel.add(patientField);
        gbc.gridy = 2;
        mainPanel.add(patientPanel, gbc);

        // Doctor Name field
        JPanel doctorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        doctorPanel.add(new JLabel("Doctor Name:"));
        doctorField = new JTextField(20);
        doctorPanel.add(doctorField);
        gbc.gridy = 3;
        mainPanel.add(doctorPanel, gbc);

        // Appointment Date field
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Appointment Date (YYYY-MM-DD):"));
        dateField = new JTextField(20);
        datePanel.add(dateField);
        gbc.gridy = 4;
        mainPanel.add(datePanel, gbc);

        // Save Button with rounded corners
        JButton saveButton = new JButton("Save Appointment");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 14));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(0x4CAF50)); // Green color
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        saveButton.setPreferredSize(new Dimension(200, 40));
        saveButton.addActionListener(this::handleSave);
        gbc.gridy = 5;
        mainPanel.add(saveButton, gbc);

        // Back Button with rounded corners
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setForeground(new Color(0x4CAF50)); // Green color
        backButton.setBackground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.addActionListener(e -> dispose());
        gbc.gridy = 6;
        mainPanel.add(backButton, gbc);

        // View Appointments button with rounded corners
        JButton viewButton = new JButton("View Appointments");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewButton.setForeground(Color.WHITE);
        viewButton.setBackground(new Color(0x4CAF50)); // Green color
        viewButton.setFocusPainted(false);
        viewButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        viewButton.setPreferredSize(new Dimension(200, 40));
        viewButton.addActionListener(this::handleViewAppointments);
        gbc.gridy = 7;
        mainPanel.add(viewButton, gbc);
    }

    
    private void handleSave(ActionEvent e) {

        String username = usernameField.getText().trim();

        String patient = patientField.getText().trim();

        String doctor = doctorField.getText().trim();

        String date = dateField.getText().trim();

        if (patient.isEmpty() || doctor.isEmpty() || date.isEmpty()) {

            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);

            return;

        }

        try {

            File file = new File("appointments.xml");

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

                root = document.createElement("Appointments");

                document.appendChild(root);

            }

            // Create new Appointment node

            Element appointment = document.createElement("Appointment");

            Element usernameElement = document.createElement("Username");

            usernameElement.appendChild(document.createTextNode(username));

            appointment.appendChild(usernameElement);

            Element patientElement = document.createElement("Patient");

            patientElement.appendChild(document.createTextNode(patient));

            appointment.appendChild(patientElement);

            Element doctorElement = document.createElement("Doctor");

            doctorElement.appendChild(document.createTextNode(doctor));

            appointment.appendChild(doctorElement);

            Element dateElement = document.createElement("Date");

            dateElement.appendChild(document.createTextNode(date));

            appointment.appendChild(dateElement);

            root.appendChild(appointment);

            // Save the updated XML to the file

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);

            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

            JOptionPane.showMessageDialog(this, "Appointment saved successfully!");

            dispose();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(this, "Error saving appointment!", "Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    // Handle the "View Appointments" button click

    private void handleViewAppointments(ActionEvent e) {

        try {

            File file = new File("appointments.xml");

            if (!file.exists()) {

                JOptionPane.showMessageDialog(this, "No appointments available!", "Error", JOptionPane.ERROR_MESSAGE);

                return;

            }

            // Load the appointments XML

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document document = dBuilder.parse(file);

            document.getDocumentElement().normalize();

            // Extract appointment data

            NodeList appointments = document.getElementsByTagName("Appointment");

            // Prepare the text to display

            StringBuilder appointmentList = new StringBuilder();

            for (int i = 0; i < appointments.getLength(); i++) {

                Element appointment = (Element) appointments.item(i);

                // Extract data safely using null checks

                String username = getElementText(appointment, "Username");

                String patient = getElementText(appointment, "Patient");

                String doctor = getElementText(appointment, "Doctor");

                String date = getElementText(appointment, "Date");

                appointmentList.append("Username: ").append(username).append("\n")

                        .append("Patient: ").append(patient).append("\n")

                        .append("Doctor: ").append(doctor).append("\n")

                        .append("Date: ").append(date).append("\n\n");

            }

            // Check if there are any appointments

            if (appointmentList.length() == 0) {

                JOptionPane.showMessageDialog(this, "No appointment records found!", "Info",

                        JOptionPane.INFORMATION_MESSAGE);

            } else {

                // Create a new JFrame to display the appointment list

                JFrame viewFrame = new JFrame("Appointments");

                JTextArea textArea = new JTextArea(appointmentList.toString());

                textArea.setEditable(false); // Disable editing

                JScrollPane scrollPane = new JScrollPane(textArea);

                viewFrame.add(scrollPane);

                viewFrame.setSize(400, 300);

                viewFrame.setLocationRelativeTo(null);

                viewFrame.setVisible(true);

            }

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(this, "Error loading appointments data!", "Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    private String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "N/A"; // Return a default value if the element is not found
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentForm("currentUsername").setVisible(true));
    }
}
