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

public class DoctorForm extends JFrame {
    private JTextField nameField, specializationField, contactField;
    @SuppressWarnings("unused")


    public DoctorForm() {
        setTitle("Manage Doctors");
        setSize(400, 300); // Adjusted size
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
        JLabel titleLabel = new JLabel("Manage Doctors", JLabel.CENTER);
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

        // Specialization field
        JPanel specializationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        specializationPanel.add(new JLabel("Specialization:"));
        specializationField = new JTextField(20);
        specializationPanel.add(specializationField);
        gbc.gridy = 2;
        mainPanel.add(specializationPanel, gbc);

        // Contact field
        JPanel contactPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contactPanel.add(new JLabel("Contact:"));
        contactField = new JTextField(20);
        contactPanel.add(contactField);
        gbc.gridy = 3;
        mainPanel.add(contactPanel, gbc);

        // Save Button with rounded corners
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 14));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(0x4CAF50)); // Green color
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        saveButton.setPreferredSize(new Dimension(200, 40));
        saveButton.addActionListener(this::handleSave);
        gbc.gridy = 4;
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
        gbc.gridy = 5;
        mainPanel.add(backButton, gbc);

        // View Doctors button with rounded corners
        JButton viewButton = new JButton("View Doctors");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewButton.setForeground(Color.WHITE);
        viewButton.setBackground(new Color(0x4CAF50)); // Green color
        viewButton.setFocusPainted(false);
        viewButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        viewButton.setPreferredSize(new Dimension(200, 40));
        viewButton.addActionListener(this::handleViewDoctors);
        gbc.gridy = 6;
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
        String specialization = specializationField.getText().trim();
        String contact = contactField.getText().trim();

        if (name.isEmpty() || specialization.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File("doctors.xml");
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
                root = document.createElement("Doctors");
                document.appendChild(root);
            }

            // Create new Doctor node
            Element doctor = document.createElement("Doctor");

            Element nameElement = document.createElement("Name");
            nameElement.appendChild(document.createTextNode(name));
            doctor.appendChild(nameElement);

            Element specializationElement = document.createElement("Specialization");
            specializationElement.appendChild(document.createTextNode(specialization));
            doctor.appendChild(specializationElement);

            Element contactElement = document.createElement("Contact");
            contactElement.appendChild(document.createTextNode(contact));
            doctor.appendChild(contactElement);

            root.appendChild(doctor);

            // Save the updated XML to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(this, "Doctor details saved successfully!");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving doctor details!", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
    // Handle navbar clicks


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DoctorForm().setVisible(true);
        });
    }
}
