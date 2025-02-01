import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Main() {
        setTitle("Login");
        setSize(400, 300);
        setUndecorated(false); // Remove window borders
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make full-screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // Use GridBagLayout to center the form

        // Create a panel to hold the form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for the form layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components

        // Username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // Align label to the left
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton.addActionListener(e -> verifyLogin());
        formPanel.add(loginButton, gbc);

        // Links: Create Account and Forgot Password
        JLabel createAccountLink = new JLabel("Create an Account");
        createAccountLink.setForeground(Color.BLUE); // Set text color to blue
        createAccountLink.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand
        createAccountLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new CreateAccountWindow(); // Open the create account window
            }
        });

        JLabel forgotPasswordLink = new JLabel("Forgot Password?");
        forgotPasswordLink.setForeground(Color.BLUE); // Set text color to blue
        forgotPasswordLink.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand
        forgotPasswordLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showForgotPasswordScreen();
            }
        });

        JPanel linkPanel = new JPanel();
        linkPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        linkPanel.add(createAccountLink);
        linkPanel.add(forgotPasswordLink);

        gbc.gridy = 3;
        formPanel.add(linkPanel, gbc);

        // Add form panel to the main frame
        add(formPanel);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    private void verifyLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Database connection details
        String url = "jdbc:mysql://localhost:3306/librax"; // Replace 'librax' with your database name
        String dbUser = "root"; // Replace with your MySQL username
        String dbPassword = ""; // Replace with your MySQL password

        // Queries to check both tables
        String userQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
        String adminQuery = "SELECT * FROM admins WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword)) {
            // Check in the users table
            try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                userStatement.setString(1, username);
                userStatement.setString(2, password);
                ResultSet userResult = userStatement.executeQuery();

                if (userResult.next()) {
                    JOptionPane.showMessageDialog(this, "Welcome, User!");
                    new DashboardScreen(username); // Ensure this class exists
                    dispose(); // Close login window
                    return;
                }
            }

            // Check in the admins table
            try (PreparedStatement adminStatement = connection.prepareStatement(adminQuery)) {
                adminStatement.setString(1, username);
                adminStatement.setString(2, password);
                ResultSet adminResult = adminStatement.executeQuery();

                if (adminResult.next()) {
                    JOptionPane.showMessageDialog(this, "Welcome, Admin!");
                    new AdminDashboard(); // Ensure this class exists
                    dispose(); // Close login window
                    return;
                }
            }

            // If no match is found in either table
            JOptionPane.showMessageDialog(this, "Invalid Credentials!");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.");
        }
    }




    private void showForgotPasswordScreen() {
        JOptionPane.showMessageDialog(this, "Forgot password functionality coming soon!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
