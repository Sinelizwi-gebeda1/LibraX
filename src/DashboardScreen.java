import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.border.Border;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DashboardScreen extends JFrame {
    private JPanel rightPanel;
    private JPanel booksPanel;
    private String username;
    private JLabel welcomeLabel;  // Declare welcomeLabel as a class-level member
    private String fullName;
    private String email;
    private String dob;
    private String location;
    private String PhoneNumber;
    //private String status="borrowed";



    public DashboardScreen(String username) {
        this.username = username;

        setTitle("LibraX - Dashboard");
        setSize(800, 600);
        setUndecorated(false); // Remove window borders
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make full-screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize and add the welcome label
        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(new Color(51, 153, 255));
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        profilePanel.add(welcomeLabel);
        add(profilePanel, BorderLayout.NORTH);



        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for precise control
        buttonsPanel.setBackground(new Color(240, 240, 240)); // Panel background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Single column layout
        gbc.fill = GridBagConstraints.HORIZONTAL; // Buttons should stretch horizontally
        gbc.insets = new Insets(5, 0, 5, 0); // Minimal spacing (top, left, bottom, right)

// Add buttons
        String[] buttonLabels = {"Search Books", "Return Books", "History", "Edit Profile", "Logout"};
        ActionListener[] buttonActions = {
                e -> showSearchInterface(),
                e -> showReturnInterface(),
                e -> displayBorrowHistory(),
                e -> showEditProfileInterface(),
                e -> {
                    // Show confirmation dialog for logout
                    int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        new Main(); // Navigate to the Main screen
                        dispose();
                    }
                }
        };


        for (int i = 0; i < buttonLabels.length; i++) {
            gbc.gridy = i; // Increment row for each button
            buttonsPanel.add(createMenuButton(buttonLabels[i], buttonActions[i]), gbc);
        }

// Add the panel to the left side of the dashboard
        add(buttonsPanel, BorderLayout.WEST);




        // Initialize the right panel
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        add(rightPanel, BorderLayout.CENTER);

        // Display LibraX information
        displayLibraXInfo();

        setVisible(true);
    }



    private void displayLibraXInfo() {
        // Create a panel for LibraX information
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout()); // Use BorderLayout for flexible alignment
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(80, 360, 20, 20)); // Add padding around the panel

        // Create description label with styled text
        JLabel descriptionLabel = new JLabel(
                "<html>"
                        + "<div style='text-align: justify;'>"
                        + "<h1 style='padding-bottom: 1em;'>Welcome to LibraX</h1>"
                        + "<p>LibraX is a modern Library Management System designed to simplify the way you manage books and library operations.</p>"
                        + "<h2>Features Explained:</h2>"
                        + "<ul style='text-align: left; padding-left: 2em;'>"
                        + "<li><b>Search Books:</b> Easily find books by title, author, or genre in our extensive library collection.</li>"
                        + "<li><b>Return Books:</b> Quickly log your book returns, ensuring your account is always up-to-date.</li>"
                        + "<li><b>History:</b> View a detailed history of the books you have borrowed and returned.</li>"
                        + "<li><b>Edit Profile:</b> Update your personal details such as email, phone number, and location.</li>"
                        + "<li><b>Logout:</b> Securely log out of your account to protect your information.</li>"
                        + "</ul>"
                        + "<p>Start exploring LibraX to make the most of your library experience!</p>"
                        + "</div>"
                        + "</html>"
        );

        // Set font for the description label
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Create a container panel to wrap the label with additional padding
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the label
        wrapperPanel.add(descriptionLabel, BorderLayout.NORTH); // Align content to the top

        // Add the wrapper panel to the main info panel
        infoPanel.add(wrapperPanel, BorderLayout.CENTER);

        // Add the info panel to the right panel
        rightPanel.add(infoPanel, BorderLayout.CENTER);
    }




    private JButton createMenuButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Standard font size
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40)); // Equal size for all buttons (smaller height)
        button.addActionListener(actionListener);

        // Set light blue background
        button.setBackground(new Color(173, 216, 230)); // Light blue color
        button.setForeground(Color.BLACK); // Text color

        // Add rounded corners
        button.setBorder(new RoundedBorder(20)); // Rounded edges
        button.setContentAreaFilled(false); // Disable default background
        button.setOpaque(true); // Ensure background is shown

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(135, 206, 250)); // Brighter blue on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(173, 216, 230)); // Reset to light blue
            }
        });

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }



    // Custom border class for rounded corners
    class RoundedBorder implements Border {
        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }



    private User fetchUserDetails(String username) {
        String jdbcURL = "jdbc:mysql://localhost:3306/librax";
        String dbUser = "root";
        String dbPassword = "";
        String fetchUserQuery = "SELECT username, password, fullName, email, dob, location, phoneNumber FROM users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(fetchUserQuery)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Create a User object to hold user details
                return new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("fullName"),
                        resultSet.getString("email"),
                        resultSet.getString("dob"),
                        resultSet.getString("location"),
                        resultSet.getString("phoneNumber")
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public class User {
        private String username;
        private String password;
        private String fullName;
        private String email;
        private String dob;
        private String location;
        private String phoneNumber;

        // Constructor
        public User(String username, String password, String fullName, String email,
                    String dob, String location, String phoneNumber) {
            this.username = username;
            this.password = password;
            this.fullName = fullName;
            this.email = email;
            this.dob = dob;
            this.location = location;
            this.phoneNumber = phoneNumber;
        }

        // Getters
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public String getDob() { return dob; }
        public String getLocation() { return location; }
        public String getPhoneNumber() { return phoneNumber; }
    }


    private void showEditProfileInterface() {
        rightPanel.removeAll();

        // Fetch user details from the database
        User currentUser = fetchUserDetails(username);
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Failed to load user details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Panel for profile editing
        JPanel editProfilePanel = new JPanel(new GridBagLayout());
        editProfilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        editProfilePanel.add(new JLabel("Username:"), gbc);
        JTextField usernameField = new JTextField(15);
        usernameField.setText(currentUser.getUsername());
        gbc.gridx = 1;
        editProfilePanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        editProfilePanel.add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setText(currentUser.getPassword());
        gbc.gridx = 1;
        editProfilePanel.add(passwordField, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        editProfilePanel.add(new JLabel("Full Name:"), gbc);
        JTextField fullNameField = new JTextField(15);
        fullNameField.setText(currentUser.getFullName());
        gbc.gridx = 1;
        editProfilePanel.add(fullNameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        editProfilePanel.add(new JLabel("Email:"), gbc);
        JTextField emailField = new JTextField(15);
        emailField.setText(currentUser.getEmail());
        gbc.gridx = 1;
        editProfilePanel.add(emailField, gbc);



        // Date of Birth
        gbc.gridx = 0;
        gbc.gridy = 4;
        editProfilePanel.add(new JLabel("Date of Birth (yyyy-mm-dd):"), gbc);
        JTextField dobField = new JTextField(15);
        dobField.setText(currentUser.getDob());
        gbc.gridx = 1;
        editProfilePanel.add(dobField, gbc);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 5;
        editProfilePanel.add(new JLabel("Location:"), gbc);
        JTextField locationField = new JTextField(15);
        locationField.setText(currentUser.getLocation());
        gbc.gridx = 1;
        editProfilePanel.add(locationField, gbc);

        // Cell Number
        gbc.gridx = 0;
        gbc.gridy = 6;
        editProfilePanel.add(new JLabel("Cell Number:"), gbc);
        JTextField cellNumberField = new JTextField(15);
        cellNumberField.setText(currentUser.getPhoneNumber());
        gbc.gridx = 1;
        editProfilePanel.add(cellNumberField, gbc);

        // Submit button to save changes
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> updateProfile(
                usernameField.getText(),
                new String(passwordField.getPassword()),
                fullNameField.getText(),
                emailField.getText(),
                dobField.getText(),
                locationField.getText(),
                cellNumberField.getText()
        ));
        gbc.gridx = 1;
        gbc.gridy = 7;
        editProfilePanel.add(saveButton, gbc);

        rightPanel.add(editProfilePanel, BorderLayout.CENTER);

        rightPanel.revalidate();
        rightPanel.repaint();
    }











    private void updateProfile(String newUsername, String newPassword, String newFullName, String newEmail, String newDob, String newLocation, String newPhoneNumber) {
        String jdbcURL = "jdbc:mysql://localhost:3306/librax";
        String dbUser = "root";
        String dbPassword = "";

        String updateUserQuery = "UPDATE users SET username = ?, fullName = ?, email = ? , password = ?,phoneNumber = ?, dob = ?, location = ? WHERE username = ?";
        String updateBorrowQuery = "UPDATE borrow SET username = ? WHERE username = ?";
        String updateReturnedBookQuery = "UPDATE returned_book SET username = ? WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            // Disable auto-commit to allow transaction management
            connection.setAutoCommit(false);

            try (PreparedStatement userStatement = connection.prepareStatement(updateUserQuery);
                 PreparedStatement borrowStatement = connection.prepareStatement(updateBorrowQuery);
                 PreparedStatement returnedBookStatement = connection.prepareStatement(updateReturnedBookQuery)) {

                // Update the user table
                userStatement.setString(1, newUsername);
                userStatement.setString(2, newFullName);
                userStatement.setString(3, newEmail);
                userStatement.setString(4, newPassword);
                userStatement.setString(5, newPhoneNumber);
                userStatement.setString(6, newDob);
                userStatement.setString(7, newLocation);
                userStatement.setString(8, username);

                int userUpdated = userStatement.executeUpdate();

                // Update the borrow table
                borrowStatement.setString(1, newUsername);
                borrowStatement.setString(2, username);
                int borrowUpdated = borrowStatement.executeUpdate();

                // Update the returned_book table
                returnedBookStatement.setString(1, newUsername);
                returnedBookStatement.setString(2, username);
                int returnedBookUpdated = returnedBookStatement.executeUpdate();

                if (userUpdated > 0 || borrowUpdated > 0 || returnedBookUpdated > 0) {
                    connection.commit(); // Commit transaction
                    JOptionPane.showMessageDialog(this, "Profile updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.username = newUsername; // Update session username
                    welcomeLabel.setText("Welcome, " + newUsername + "!"); // Update welcome message
                    showSearchInterface(); // Go back to the search interface
                } else {
                    connection.rollback(); // Rollback changes
                    JOptionPane.showMessageDialog(this, "Failed to update profile. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                connection.rollback(); // Rollback on error
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                connection.setAutoCommit(true); // Reset auto-commit
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    private void showSearchInterface() {
        rightPanel.removeAll();

        // Main Panel for the search interface
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components

        // Row 1: Search by Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Search by Title:"), gbc);

        gbc.gridx = 1;
        JTextField titleField = new JTextField(15);
        searchPanel.add(titleField, gbc);

        // Row 2: Search by Author
        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPanel.add(new JLabel("Search by Author:"), gbc);

        gbc.gridx = 1;
        JTextField authorField = new JTextField(15);
        searchPanel.add(authorField, gbc);

        // Row 3: Filter by Year
        gbc.gridx = 0;
        gbc.gridy = 2;
        searchPanel.add(new JLabel("Filter by Year:"), gbc);

        gbc.gridx = 1;
        String[] yearRanges = {"All Years", "2000-2005", "2006-2010", "2011-2015", "2016-2020", "2021-2025"};
        JComboBox<String> yearComboBox = new JComboBox<>(yearRanges);
        searchPanel.add(yearComboBox, gbc);

        // Row 4: Select Category
        gbc.gridx = 0;
        gbc.gridy = 3;
        searchPanel.add(new JLabel("Select genre:"), gbc);

        gbc.gridx = 1;
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] categories = {"Poetry", "Fiction", "Non-Fiction", "Science", "Drama","Romance"};
        ButtonGroup categoryGroup = new ButtonGroup();
        for (String category : categories) {
            JRadioButton radioButton = new JRadioButton(category);
            categoryGroup.add(radioButton);
            categoryPanel.add(radioButton);
        }
        searchPanel.add(categoryPanel, gbc);

        // Row 5: Search Button
        gbc.gridx = 1;
        gbc.gridy = 4;
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 12));
        searchButton.setPreferredSize(new Dimension(80, 30));
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> {
            String selectedCategory = null;
            for (Component component : categoryPanel.getComponents()) {
                if (component instanceof JRadioButton && ((JRadioButton) component).isSelected()) {
                    selectedCategory = ((JRadioButton) component).getText();
                    break;
                }
            }
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String yearRange = (String) yearComboBox.getSelectedItem();
            List<String[]> results = searchBooks(title, author, yearRange, selectedCategory);
            displayBooks(results);
        });
        searchPanel.add(searchButton, gbc);

        rightPanel.add(searchPanel, BorderLayout.NORTH);

        // Add a heading for search results
        JLabel resultsHeading = new JLabel("Search Results", SwingConstants.LEFT);
        resultsHeading.setFont(new Font("Arial", Font.BOLD, 14));
        resultsHeading.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));

        // Panel for search results
        booksPanel = new JPanel();
        booksPanel.setLayout(new GridLayout(0, 1, 5, 5)); // Align results to the top
        booksPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        JScrollPane resultsScrollPane = new JScrollPane(booksPanel);
        resultsScrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 200)); // Set smaller height

        // Add heading and results panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.add(resultsHeading, BorderLayout.NORTH);
        resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);

        rightPanel.add(resultsPanel, BorderLayout.CENTER);

        rightPanel.revalidate();
        rightPanel.repaint();
    }



    private List<String[]> searchBooks(String title, String author, String yearRange, String category) {
        List<String[]> results = new ArrayList<>();
        String jdbcURL = "jdbc:mysql://localhost:3306/librax";
        String dbUser = "root";
        String dbPassword = "";
        String query = "SELECT * FROM books WHERE 1=1";

        if (!title.isEmpty()) query += " AND title LIKE ?";
        if (!author.isEmpty()) query += " AND author LIKE ?";
        if (yearRange != null && !yearRange.equals("All Years")) query += " AND year BETWEEN ? AND ?";
        if (category != null) query += " AND category = ?";

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            int paramIndex = 1;
            if (!title.isEmpty()) statement.setString(paramIndex++, "%" + title + "%");
            if (!author.isEmpty()) statement.setString(paramIndex++, "%" + author + "%");
            if (yearRange != null && !yearRange.equals("All Years")) {
                String[] rangeParts = yearRange.split("-");
                statement.setInt(paramIndex++, Integer.parseInt(rangeParts[0]));
                statement.setInt(paramIndex++, Integer.parseInt(rangeParts[1]));
            }
            if (category != null) statement.setString(paramIndex++, category);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String bookTitle = resultSet.getString("title");
                String bookAuthor = resultSet.getString("author");
                String bookYear = String.valueOf(resultSet.getInt("year"));
                String bookCategory = resultSet.getString("category");
                String bookQuantity = String.valueOf(resultSet.getInt("quantity"));
                String bookISBN = resultSet.getString("isbn");
                String bookLibrary = resultSet.getString("library"); // New column

                results.add(new String[]{bookTitle, bookAuthor, bookYear, bookCategory, bookQuantity, bookISBN, bookLibrary});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return results;
    }



    private void displayBooks(List<String[]> books) {
        booksPanel.removeAll();

        if (books.isEmpty()) {
            JLabel noBooksLabel = new JLabel("No books found");
            noBooksLabel.setFont(new Font("Arial", Font.BOLD, 14));
            noBooksLabel.setForeground(Color.RED);
            noBooksLabel.setHorizontalAlignment(SwingConstants.CENTER);
            booksPanel.setLayout(new BorderLayout()); // Center the message
            booksPanel.add(noBooksLabel, BorderLayout.CENTER);
        } else {
            booksPanel.setLayout(new GridLayout(0, 1, 5, 5)); // Compact grid layout with spacing
            String jdbcURL = "jdbc:mysql://localhost:3306/librax";
            String dbUser = "root";
            String dbPassword = "";

            for (String[] book : books) {
                JPanel bookPanel = new JPanel();
                bookPanel.setLayout(new BorderLayout(5, 5)); // Add small spacing
                bookPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Compact padding
                bookPanel.setBackground(new Color(250, 250, 250));

                JLabel bookLabel = new JLabel(String.format(
                        "<html><b>Title:</b> %s<br><b>Author:</b> %s<br><b>Year:</b> %s<br><b>Category:</b> %s<br><b>Quantity:</b> %s<br><b>Library:</b> %s</html>",
                        book[0], book[1], book[2], book[3], book[4], book[6]
                ));
                bookLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Smaller font size for compactness

                JButton borrowButton = new JButton("Borrow");
                borrowButton.setFont(new Font("Arial", Font.PLAIN, 11));
                borrowButton.setPreferredSize(new Dimension(70, 25)); // Compact button size

                boolean alreadyBorrowed = false;
                try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                     PreparedStatement statement = connection.prepareStatement(
                             "SELECT * FROM borrow WHERE username = ? AND isbn = ? AND status='borrowed'"
                     )) {
                    statement.setString(1, username);
                    statement.setString(2, book[5]);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        alreadyBorrowed = true;
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error checking borrow status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                if (alreadyBorrowed) {
                    borrowButton.setEnabled(false);
                    borrowButton.setText("Issued");
                } else if (Integer.parseInt(book[4]) == 0) {
                    borrowButton.setEnabled(false);
                    borrowButton.setText("Unavailable");
                } else {
                    borrowButton.addActionListener(e -> borrowBook(book[5], book[0]));
                }

                bookPanel.add(bookLabel, BorderLayout.CENTER);
                bookPanel.add(borrowButton, BorderLayout.EAST);

                booksPanel.add(bookPanel);
            }
        }

        rightPanel.revalidate();
        rightPanel.repaint();
    }






    private void borrowBook(String isbn, String title) {
        String jdbcURL = "jdbc:mysql://localhost:3306/librax";
        String dbUser = "root";
        String dbPassword = "";
        // String status="borrowed";
        String borrowQuery = "INSERT INTO borrow (username, isbn, title, date, status) VALUES (?, ?, ?, ?,'borrowed')";
        String updateBookQuery = "UPDATE books SET quantity = quantity - 1 WHERE isbn = ?";

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
             PreparedStatement borrowStatement = connection.prepareStatement(borrowQuery);
             PreparedStatement updateBookStatement = connection.prepareStatement(updateBookQuery)) {

            borrowStatement.setString(1, username);
            borrowStatement.setString(2, isbn);
            borrowStatement.setString(3, title);
            borrowStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            borrowStatement.executeUpdate();

            updateBookStatement.setString(1, isbn);
            updateBookStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Successfully borrowed: " + title, "Success", JOptionPane.INFORMATION_MESSAGE);

            showSearchInterface();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error borrowing the book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void displayBorrowHistory() {
        rightPanel.removeAll();

        // Adding the heading
        JLabel headingLabel = new JLabel("Your Borrowing History", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rightPanel.add(headingLabel, BorderLayout.NORTH);

        String jdbcURL = "jdbc:mysql://localhost:3306/librax";
        String dbUser = "root";
        String dbPassword = "";
        String query = "SELECT * FROM borrow WHERE username = ?";

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new GridLayout(0, 1, 5, 5));
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        boolean hasHistory = false; // Track if the user has a borrowing history

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                hasHistory = true; // Mark that the user has a borrowing history

                String isbn = resultSet.getString("isbn");
                String bookTitle = resultSet.getString("title");
                String borrowDate = resultSet.getString("date");

                JPanel bookPanel = new JPanel(new BorderLayout());
                bookPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                bookPanel.setBackground(new Color(250, 250, 250));

                JLabel bookLabel = new JLabel(String.format("ISBN: %s, Title: %s, Borrowed On: %s", isbn, bookTitle, borrowDate));
                bookLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                bookPanel.add(bookLabel, BorderLayout.CENTER);

                historyPanel.add(bookPanel);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching borrow history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!hasHistory) {
            JLabel noHistoryLabel = new JLabel("You have no borrowing history.", SwingConstants.CENTER);
            noHistoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
            noHistoryLabel.setForeground(Color.RED);
            historyPanel.setLayout(new BorderLayout());
            historyPanel.add(noHistoryLabel, BorderLayout.CENTER);
        }

        rightPanel.add(new JScrollPane(historyPanel), BorderLayout.CENTER);

        rightPanel.revalidate();
        rightPanel.repaint();
    }


    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }


    private void showReturnInterface() {
        rightPanel.removeAll();

        // Adding the heading
        JLabel headingLabel = new JLabel("Books to be returned", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rightPanel.add(headingLabel, BorderLayout.NORTH);

        String jdbcURL = "jdbc:mysql://localhost:3306/librax";
        String dbUser = "root";
        String dbPassword = "";
        String query = "SELECT * FROM borrow WHERE username = ? AND status = 'borrowed'";

        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(new GridLayout(0, 1, 5, 5));
        returnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        boolean hasBooks = false; // Track if any books are found
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                hasBooks = true; // Mark that books exist

                String isbn = resultSet.getString("isbn");
                String bookTitle = resultSet.getString("title");
                String borrowDate = resultSet.getString("date");

                // Calculate the return date
                String returnDate = "Invalid date";
                try {
                    LocalDate borrowed = LocalDate.parse(borrowDate, formatter);
                    LocalDate dueDate = borrowed.plusDays(10);
                    returnDate = dueDate.format(formatter);
                } catch (DateTimeParseException e) {
                    System.err.println("Error parsing borrow date: " + borrowDate);
                }

                JPanel bookPanel = new JPanel(new BorderLayout());
                bookPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                bookPanel.setBackground(new Color(250, 250, 250));

                JLabel bookLabel = new JLabel(String.format("ISBN: %s, Title: %s, Borrowed On: %s, Return By: %s",
                        isbn, bookTitle, borrowDate, returnDate));
                bookLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                JButton returnButton = new JButton("Return");
                returnButton.setFont(new Font("Arial", Font.PLAIN, 12));
                returnButton.setPreferredSize(new Dimension(80, 30));
                returnButton.addActionListener(e -> returnBook(isbn, bookTitle));

                bookPanel.add(bookLabel, BorderLayout.CENTER);
                bookPanel.add(returnButton, BorderLayout.EAST);

                returnPanel.add(bookPanel);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching borrowed books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!hasBooks) {
            JLabel noBooksLabel = new JLabel("You have no books to return.", SwingConstants.CENTER);
            noBooksLabel.setFont(new Font("Arial", Font.BOLD, 14));
            noBooksLabel.setForeground(Color.RED);
            returnPanel.setLayout(new BorderLayout());
            returnPanel.add(noBooksLabel, BorderLayout.CENTER);
        }

        rightPanel.add(new JScrollPane(returnPanel), BorderLayout.CENTER);

        rightPanel.revalidate();
        rightPanel.repaint();
    }



    private void returnBook(String isbn, String title) {
        String jdbcURL = "jdbc:mysql://localhost:3306/librax";
        String dbUser = "root";
        String dbPassword = "";
        String borrowQuery = "SELECT * FROM borrow WHERE username = ? AND isbn = ? AND status = 'borrowed'";
        String updateBorrowStatusQuery = "UPDATE borrow SET status = 'returned' WHERE username = ? AND isbn = ?";
        String updateBookQuery = "UPDATE books SET quantity = quantity + 1 WHERE isbn = ?";
        String insertReturnedBookQuery = "INSERT INTO returned_book (username, isbn, title, date, return_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
             PreparedStatement borrowStatement = connection.prepareStatement(borrowQuery);
             PreparedStatement updateBorrowStatusStatement = connection.prepareStatement(updateBorrowStatusQuery);
             PreparedStatement updateBookStatement = connection.prepareStatement(updateBookQuery);
             PreparedStatement insertReturnedBookStatement = connection.prepareStatement(insertReturnedBookQuery)) {

            // Fetch the borrow details
            borrowStatement.setString(1, username);
            borrowStatement.setString(2, isbn);
            ResultSet resultSet = borrowStatement.executeQuery();

            if (resultSet.next()) {
                String borrowDate = resultSet.getString("date");

                // Insert returned book details into returned_book table
                insertReturnedBookStatement.setString(1, username);
                insertReturnedBookStatement.setString(2, isbn);
                insertReturnedBookStatement.setString(3, title);
                insertReturnedBookStatement.setString(4, borrowDate);
                insertReturnedBookStatement.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                insertReturnedBookStatement.executeUpdate();

                // Update borrow status to 'returned'
                updateBorrowStatusStatement.setString(1, username);
                updateBorrowStatusStatement.setString(2, isbn);
                int rowsUpdated = updateBorrowStatusStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    // Update book quantity in books table
                    updateBookStatement.setString(1, isbn);
                    updateBookStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Successfully returned: " + title, "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Refresh the return interface
                    showReturnInterface();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Book status could not be updated.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error returning the book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    public static void main(String[] args) {
        new DashboardScreen("JohnDoe");
    }
}