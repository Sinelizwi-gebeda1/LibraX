import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Random;
import java.util.regex.Pattern;

public class CreateAccountWindow extends JFrame {
    private JTextField usernameField, fullNameField, emailField, phoneNumberField, dobField, locationField, passwordField, empNumberField;
    private JComboBox<String> roleDropdown, libraryField;
    private JPanel formPanel;

    public CreateAccountWindow() {
        setTitle("Create an Account");
        setSize(400, 550);
        setUndecorated(false); // Remove window borders
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make full-screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Role selection
        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(roleLabel, gbc);

        roleDropdown = new JComboBox<>(new String[]{"User", "Admin"});
        roleDropdown.addActionListener(e -> updateFormFields());
        gbc.gridx = 1;
        add(roleDropdown, gbc);

        // Form panel
        formPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(formPanel, gbc);

        // Generate password button
        JButton generatePasswordButton = new JButton("Generate Password");
        generatePasswordButton.addActionListener(e -> generateRandomPassword());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(generatePasswordButton, gbc);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveAccount());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);

        // Link to go back to the login screen
        JLabel alreadyHaveAccountLink = new JLabel("Already have an account?");
        alreadyHaveAccountLink.setForeground(Color.BLUE);
        alreadyHaveAccountLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        alreadyHaveAccountLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new Main();
                dispose();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(alreadyHaveAccountLink, gbc);

        setLocationRelativeTo(null);
        setVisible(true);
        updateFormFields();
    }

    private void updateFormFields() {
        String selectedRole = (String) roleDropdown.getSelectedItem();
        formPanel.removeAll();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Common fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Optional fields
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Full Name:"), gbc);
        fullNameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        phoneNumberField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(phoneNumberField, gbc);

        if ("User".equals(selectedRole)) {
            // User-specific fields
            gbc.gridx = 0;
            gbc.gridy = 5;
            formPanel.add(new JLabel("Date of Birth:"), gbc);
            dobField = new JTextField(15);
            gbc.gridx = 1;
            formPanel.add(dobField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            formPanel.add(new JLabel("Location:"), gbc);
            locationField = new JTextField(15);
            gbc.gridx = 1;
            formPanel.add(locationField, gbc);
        } else if ("Admin".equals(selectedRole)) {
            // Admin-specific fields
            gbc.gridx = 0;
            gbc.gridy = 5;
            formPanel.add(new JLabel("Library:"), gbc);
            libraryField = new JComboBox<>(new String[]{"Magxaki", "Dwesi"});
            gbc.gridx = 1;
            formPanel.add(libraryField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            formPanel.add(new JLabel("Employee Number:"), gbc);
            empNumberField = new JTextField(15);
            gbc.gridx = 1;
            formPanel.add(empNumberField, gbc);
        }

        formPanel.revalidate();
        formPanel.repaint();
    }

    private void generateRandomPassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String symbols = "!@#$%^&*()";
        String allChars = upperCase + lowerCase + digits + symbols;

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(symbols.charAt(random.nextInt(symbols.length())));

        while (password.length() < 8) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        passwordField.setText(password.toString());
    }


    private void saveAccount() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String selectedRole = (String) roleDropdown.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        if (!isValidEmail(email, selectedRole)) {
            JOptionPane.showMessageDialog(this, "Invalid email address format.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/librax";
        String user = "root";
        String pass = "";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {

            // Check if the username exists in both users and admins table
            String checkUsernameSQL = "SELECT COUNT(*) FROM users WHERE username = ? UNION ALL SELECT COUNT(*) FROM admins WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUsernameSQL)) {
                checkStmt.setString(1, username);
                checkStmt.setString(2, username);

                ResultSet rs = checkStmt.executeQuery();
                int totalCount = 0;

                while (rs.next()) {
                    totalCount += rs.getInt(1);
                }

                if (totalCount > 0) {
                    JOptionPane.showMessageDialog(this, "Username already exists. Please choose another username.");
                    return;
                }
            }

            String sql;
            PreparedStatement stmt;

            if ("User".equals(selectedRole)) {
                String dob = dobField.getText();
                String location = locationField.getText();

                if (dob.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields for User are required.");
                    return;
                }

                sql = "INSERT INTO users (username, password, fullName, email, phoneNumber, dob, location) VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, fullName);
                stmt.setString(4, email);
                stmt.setString(5, phoneNumber);
                stmt.setString(6, dob);
                stmt.setString(7, location);

            } else {
                String empNumber = empNumberField.getText();
                String library = (String) libraryField.getSelectedItem();

                if (empNumber.isEmpty() || library.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields for Admin are required.");
                    return;
                }

                if (!empNumber.startsWith("BIL")) {
                    JOptionPane.showMessageDialog(this, "Please contact your supervisor.");
                    return;
                }

                sql = "INSERT INTO admins (username, password, fullName, empNumber, email, library, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, fullName);
                stmt.setString(4, empNumber);
                stmt.setString(5, email);
                stmt.setString(6, library);
                stmt.setString(7, phoneNumber);
            }

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create account.");
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }



    private boolean isValidEmail(String email, String role) {
        if ("User".equals(role)) {
            // Simple regex for normal email validation
            return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email);
        } else if ("Admin".equals(role)) {
            // Admin-specific work email validation
            return email.contains("work") || email.endsWith("@librax.com");
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreateAccountWindow::new);
    }
}
