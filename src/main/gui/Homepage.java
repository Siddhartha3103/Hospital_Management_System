package main.gui;
import javax.swing.*;
import java.awt.*;

public class Homepage {
    public static void main(String[] args) {
        // Frame Setup
        JFrame frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(null);

        // Logo (Top Left Corner)
        // Add your logo image as a JLabel or JButton here

        // Heading (Center)
        JLabel heading = new JLabel("Hospital Management System");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBounds(100, 50, 400, 40);
        frame.add(heading);

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        registerButton.setBounds(200, 150, 200, 40);
        registerButton.setFocusPainted(false);
        frame.add(registerButton);
        // Action for Register Button
        // registerButton.addActionListener(e -> { /* Add your function here */ });

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setBounds(200, 210, 200, 40);
        loginButton.setFocusPainted(false);
        frame.add(loginButton);
        // Action for Login Button
        // loginButton.addActionListener(e -> { /* Add your function here */ });

        // Background Style
        frame.getContentPane().setBackground(new Color(230, 240, 255)); // Light Blue Background

        // Make Frame Visible
        frame.setVisible(true);
    }
}
