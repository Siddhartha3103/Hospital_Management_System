package main;

public class UserSession {
    private static String username;

    // Method to set the username when a user logs in
    public static void setUsername(String user) {
        username = user;
    }

    // Method to get the username anywhere in the application
    public static String getUsername() {
        return username;
    }

    // Method to clear the session (optional for logout functionality)
    public static void clearSession() {
        username = null;
    }
}
