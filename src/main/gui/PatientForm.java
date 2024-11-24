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

public class PatientForm extends JFrame {
    private JTextField nameField, ageField, contactField, diseaseField;
    private String username;

    @SuppressWarnings("unused")

    // Constructor updated to accept username
    public PatientForm(String username) {
        this.username = username;

        setTitle("Patient Form");
        setSize(400, 350); // Adjusted size
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
        JLabel titleLabel = new JLabel("Patient Form", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x4CAF50)); // Green color for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Name field
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        namePanel.add(nameField);
        gbc.gridy = 1;
        mainPanel.add(namePanel, gbc);

        // Age field
        JPanel agePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        agePanel.add(new JLabel("Age:"));
        ageField = new JTextField(20);
        agePanel.add(ageField);
        gbc.gridy = 2;
        mainPanel.add(agePanel, gbc);

        // Contact field
        JPanel contactPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contactPanel.add(new JLabel("Contact:"));
        contactField = new JTextField(20);
        contactPanel.add(contactField);
        gbc.gridy = 3;
        mainPanel.add(contactPanel, gbc);

        // Disease field
        JPanel diseasePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        diseasePanel.add(new JLabel("Disease:"));
        diseaseField = new JTextField(20);
        diseasePanel.add(diseaseField);
        gbc.gridy = 4;
        mainPanel.add(diseasePanel, gbc);

        // Display the logged-in username
        JLabel usernameLabel = new JLabel("Logged in as: " + username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.GRAY);
        gbc.gridy = 5;
        mainPanel.add(usernameLabel, gbc);

        // Save Button with rounded corners
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 14));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(0x4CAF50)); // Green color
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        saveButton.setPreferredSize(new Dimension(200, 40));
        saveButton.addActionListener(this::handleSave);
        gbc.gridy = 6;
        mainPanel.add(saveButton, gbc);

        // Cancel Button with rounded corners
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setForeground(new Color(0x4CAF50)); // Green color
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        cancelButton.setPreferredSize(new Dimension(200, 40));
        cancelButton.addActionListener(e -> dispose());
        gbc.gridy = 7;
        mainPanel.add(cancelButton, gbc);

        // View Patients Button with rounded corners
        JButton viewButton = new JButton("View Patients");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewButton.setForeground(Color.WHITE);
        viewButton.setBackground(new Color(0x4CAF50)); // Green color
        viewButton.setFocusPainted(false);
        viewButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        viewButton.setPreferredSize(new Dimension(200, 40));
        viewButton.addActionListener(this::handleViewPatients);
        gbc.gridy = 8;
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
        String name = nameField.getText().trim();
        String age = ageField.getText().trim();
        String contact = contactField.getText().trim();
        String disease = diseaseField.getText().trim();

        if (name.isEmpty() || age.isEmpty() || contact.isEmpty() || disease.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File("patients.xml");
            Document document;
            Element root;

            if (file.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                document = dBuilder.parse(file);
                document.getDocumentElement().normalize();
                root = document.getDocumentElement();
            } else {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                document = dBuilder.newDocument();
                root = document.createElement("Patients");
                document.appendChild(root);
            }

            Element patient = document.createElement("Patient");

            Element nameElement = document.createElement("Name");
            nameElement.appendChild(document.createTextNode(name));
            patient.appendChild(nameElement);

            Element ageElement = document.createElement("Age");
            ageElement.appendChild(document.createTextNode(age));
            patient.appendChild(ageElement);

            Element contactElement = document.createElement("Contact");
            contactElement.appendChild(document.createTextNode(contact));
            patient.appendChild(contactElement);

            Element diseaseElement = document.createElement("Disease");
            diseaseElement.appendChild(document.createTextNode(disease));
            patient.appendChild(diseaseElement);

            Element usernameElement = document.createElement("Username");
            usernameElement.appendChild(document.createTextNode(username));
            patient.appendChild(usernameElement);

            root.appendChild(patient);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(this, "Patient details saved successfully!");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving patient details!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleViewPatients(ActionEvent e) {
        try {
            File file = new File("patients.xml");

            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "No patients available!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList patients = document.getElementsByTagName("Patient");

            StringBuilder patientList = new StringBuilder();
            for (int i = 0; i < patients.getLength(); i++) {
                Element patient = (Element) patients.item(i);
                String name = getElementTextContent(patient, "Name");
                String age = getElementTextContent(patient, "Age");
                String contact = getElementTextContent(patient, "Contact");
                String disease = getElementTextContent(patient, "Disease");
                String username = getElementTextContent(patient, "Username");

                patientList.append("Name: ").append(name).append("\n")
                        .append("Age: ").append(age).append("\n")
                        .append("Contact: ").append(contact).append("\n")
                        .append("Disease: ").append(disease).append("\n")
                        .append("Logged in by: ").append(username).append("\n\n");
            }

            // Add the logged-in username at the top
            patientList.insert(0, "Viewing Patients for: " + username + "\n\n");

            if (patientList.length() == 0) {
                JOptionPane.showMessageDialog(this, "No patient records found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JFrame viewFrame = new JFrame("Patients");
                JTextArea textArea = new JTextArea(patientList.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                viewFrame.add(scrollPane);
                viewFrame.setSize(400, 300);
                viewFrame.setLocationRelativeTo(null);
                viewFrame.setVisible(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Utility method to get text content safely
    private String getElementTextContent(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "N/A"; // Return a default value if element is not found
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PatientForm("currentUsername").setVisible(true);
        });
    }
}
