package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NavbarPanel extends JPanel {

    public NavbarPanel(ActionListener listener) {
        // Set layout for the navbar
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(70, 130, 180)); // Optional: Nice background color

        // Define navbar buttons
        String[] buttonLabels = {
            "Dashboard", 
            "Manage Patients", 
            "Manage Doctors", 
            "Manage Appointments", 
            "Manage Billings", 
            "Logout"
        };

        // Create buttons and add ActionListener
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setActionCommand(label); // Set action command to identify button
            button.addActionListener(listener); // Attach the passed listener
            add(button); // Add button to the panel
        }
    }
}
