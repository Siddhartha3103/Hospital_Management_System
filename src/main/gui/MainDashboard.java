package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainDashboard extends JFrame {

    @SuppressWarnings("unused")
    private String username;

    @SuppressWarnings("unused")
    public MainDashboard(String username) {
        this.username = username;  // Initialize the username

        setTitle("Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        // setUndecorated(true); // Remove title bar and borders for full-screen effect
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
        JLabel titleLabel = new JLabel("Admin Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x4CAF50)); // Green color for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Patient Button with rounded corners
        JButton patientButton = new JButton("Manage Patients");
        patientButton.setFont(new Font("Arial", Font.PLAIN, 14));
        patientButton.setForeground(Color.WHITE);
        patientButton.setBackground(new Color(0x4CAF50)); // Green color
        patientButton.setFocusPainted(false);
        patientButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        patientButton.setPreferredSize(new Dimension(200, 40));
        patientButton.addActionListener(e -> new PatientForm(username).setVisible(true));  // Pass username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(patientButton, gbc);

        // Doctor Button with rounded corners
        JButton doctorButton = new JButton("Manage Doctors");
        doctorButton.setFont(new Font("Arial", Font.PLAIN, 14));
        doctorButton.setForeground(Color.WHITE);
        doctorButton.setBackground(new Color(0x4CAF50)); // Green color
        doctorButton.setFocusPainted(false);
        doctorButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        doctorButton.setPreferredSize(new Dimension(200, 40));
        doctorButton.addActionListener(e -> new DoctorForm().setVisible(true));
        gbc.gridy = 2;
        mainPanel.add(doctorButton, gbc);

        // Appointment Button with rounded corners
        JButton appointmentButton = new JButton("Manage Appointments");
        appointmentButton.setFont(new Font("Arial", Font.PLAIN, 14));
        appointmentButton.setForeground(Color.WHITE);
        appointmentButton.setBackground(new Color(0x4CAF50)); // Green color
        appointmentButton.setFocusPainted(false);
        appointmentButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        appointmentButton.setPreferredSize(new Dimension(200, 40));
        appointmentButton.addActionListener(e -> new AppointmentForm(username).setVisible(true));  // Pass username
        gbc.gridy = 3;
        mainPanel.add(appointmentButton, gbc);

        // Billing Button with rounded corners
        JButton billingButton = new JButton("Manage Billings");
        billingButton.setFont(new Font("Arial", Font.PLAIN, 14));
        billingButton.setForeground(Color.WHITE);
        billingButton.setBackground(new Color(0x4CAF50)); // Green color
        billingButton.setFocusPainted(false);
        billingButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        billingButton.setPreferredSize(new Dimension(200, 40));
        billingButton.addActionListener(e -> new BillingForm(username).setVisible(true));  // Pass username
        gbc.gridy = 4;
        mainPanel.add(billingButton, gbc);

        // Report Button with rounded corners
        JButton reportButton = new JButton("Manage Reports");
        reportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        reportButton.setForeground(Color.WHITE);
        reportButton.setBackground(new Color(0x4CAF50)); // Green color
        reportButton.setFocusPainted(false);
        reportButton.setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50)));
        reportButton.setPreferredSize(new Dimension(200, 40));
        reportButton.addActionListener(e -> new ReportForm(username).setVisible(true));  // Pass username
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

    private void handleLogout(ActionEvent e) {
        new LoginForm().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainDashboard("currentUsername").setVisible(true);  // Pass the actual username here
        });
    }
}
