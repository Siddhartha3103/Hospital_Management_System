package main;


import main.gui.LoginForm;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginScreen = new LoginForm();
            loginScreen.setVisible(true);
            // Homepage home = new Homepage();
            // home.setVisible(true);
        });
    }
}
