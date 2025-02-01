import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard {
    private JLabel welcomeLabel;  // Declare welcomeLabel as a class-level member

    private JFrame frame;
    private JPanel contentPanel;
    private JTextField isbnField, titleField, authorField, yearField, quantityField;
    private JCheckBox magxakiCheckbox, kwadwesiCheckbox;
    private JComboBox<String> availabilityDropdown, categoryDropdown;
    private String username;


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AdminDashboard window = new AdminDashboard();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AdminDashboard() {

        frame = new JFrame("Admin Dashboard");
        frame.setSize(800, 500);
        frame.setUndecorated(false); // Keep window borders
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Make full-screen
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Initialize and add the welcome label
        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(new Color(51, 153, 255));
        profilePanel.setPreferredSize(new Dimension(frame.getWidth(), 50)); // Set fixed height for the title bar

        welcomeLabel = new JLabel("Welcome to the Admin Dashboard");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Make the text bigger
        welcomeLabel.setForeground(Color.WHITE);

        profilePanel.add(welcomeLabel);
        frame.add(profilePanel, BorderLayout.NORTH);

        frame.setVisible(true); // Ensure this is at the end of the constructor





        // Left panel with navigation buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(120, 0)); // Reduce width
        leftPanel.setBackground(new Color(200, 200, 200));

// Remove the initial vertical glue
// Adjust spacing for vertical positioning of buttons
        leftPanel.add(Box.createVerticalStrut(50)); // Add some space from the top

// Create buttons with smaller size
        Dimension buttonSize = new Dimension(120, 50); // Smaller buttons
        JButton addBookBtn = new JButton("Add Books");
        JButton removeBookBtn = new JButton("Remove Books");
        JButton searchBookBtn = new JButton("Search Books");
        JButton borrowedBookBtn = new JButton("Borrowed Books");
        JButton viewBooksBtn = new JButton("View Books");
        JButton logoutBtn = new JButton("Logout");

// Apply size to buttons
        JButton[] buttons = {addBookBtn, removeBookBtn, searchBookBtn, borrowedBookBtn, viewBooksBtn, logoutBtn};
        for (JButton btn : buttons) {
            btn.setMaximumSize(buttonSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT); // Center buttons
            leftPanel.add(btn);
            leftPanel.add(Box.createVerticalStrut(5)); // Reduce spacing between buttons
        }

        frame.add(leftPanel, BorderLayout.WEST);



        // Right panel (content area)
        contentPanel = new JPanel(new CardLayout());
        frame.add(contentPanel, BorderLayout.CENTER);

        // Blank panel
        JPanel blankPanel = new JPanel();
        contentPanel.add(blankPanel, "Blank");

        // Add Book Form Panel
        JPanel addBookPanel = createAddBookPanel();
        contentPanel.add(addBookPanel, "AddBooks");

        // Remove Book Panel
        JPanel removeBookPanel = createRemoveBookPanel();
        contentPanel.add(removeBookPanel, "RemoveBooks");

        // Borrowed Books Panel
        JPanel borrowedBooksPanel = createBorrowedBooksPanel();
        contentPanel.add(borrowedBooksPanel, "BorrowedBooks");

        // Search Books Panel
        JPanel searchBooksPanel = createSearchBooksPanel();
        contentPanel.add(searchBooksPanel, "SearchBooks");

        // View Books Panel
        JPanel viewBooksPanel = createViewBooksPanel();
        contentPanel.add(viewBooksPanel, "ViewBooks");



        // Default to blank screen
        switchPanel("Blank");

        // Button Listeners to Switch Views
        addBookBtn.addActionListener(e -> switchPanel("AddBooks"));
        removeBookBtn.addActionListener(e -> switchPanel("RemoveBooks"));
        borrowedBookBtn.addActionListener(e -> switchPanel("BorrowedBooks"));
        searchBookBtn.addActionListener(e -> switchPanel("SearchBooks"));
//        returnedBookBtn.addActionListener(e -> switchPanel("Blank"));
        viewBooksBtn.addActionListener(e -> switchPanel("ViewBooks"));
//        returnedBookBtn.addActionListener(e -> switchPanel("ReturnedBooks"));

        // Logout Button ActionListener
        logoutBtn.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                // Close the current frame and return to the login screen
                frame.dispose();  // Close the current window

                // Optionally, open the login window here (if you have a Login class)
                Main loginWindow = new Main();  // Replace with your login window class
                loginWindow.setVisible(true);
            }
        });

    }


    private void refreshBookTable() {
        // This method will reload all the books and update the View Books panel
        JPanel viewBooksPanel = createViewBooksPanel();  // This will call the method that loads all books again
        contentPanel.add(viewBooksPanel, "ViewBooks");
        switchPanel("ViewBooks");
    }


    // Create Remove Book Panel
    private JPanel createRemoveBookPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form fields for ISBN to remove
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ISBN to Remove:"), gbc);
        gbc.gridx = 1;
        JTextField isbnToRemoveField = new JTextField(15);
        panel.add(isbnToRemoveField, gbc);

        // Remove book button (centered)
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton removeButton = new JButton("Remove Book");
        removeButton.addActionListener(e -> removeBook(isbnToRemoveField.getText()));
        panel.add(removeButton, gbc);

        return panel;
    }

    // Remove Book from database
    private void removeBook(String isbn) {
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter an ISBN.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librax", "root", "")) {
            String sql = "DELETE FROM books WHERE isbn = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, isbn);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Book removed successfully.");
                    refreshBookTable();  // Refresh the books table after removing
                } else {
                    JOptionPane.showMessageDialog(frame, "No book found with the given ISBN.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error removing book: " + e.getMessage());
        }
    }


    private JPanel createSearchBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search Fields
        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        JTextField searchIsbnField = new JTextField(15);
        searchPanel.add(searchIsbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        searchPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        JTextField searchTitleField = new JTextField(15);
        searchPanel.add(searchTitleField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        searchPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        JTextField searchAuthorField = new JTextField(15);
        searchPanel.add(searchAuthorField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        searchPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        JTextField searchCategoryField = new JTextField(15);
        searchPanel.add(searchCategoryField, gbc);

        // Search Button
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton, gbc);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Table to display search results
        String[] columnNames = {"ISBN", "Title", "Author", "Year", "Category", "Quantity", "Library", "Availability"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable searchResultsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(searchResultsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Search Button Action
        searchButton.addActionListener(e -> {
            String isbn = searchIsbnField.getText().trim();
            String title = searchTitleField.getText().trim();
            String author = searchAuthorField.getText().trim();
            String category = searchCategoryField.getText().trim();
            searchBooks(model, isbn, title, author, category);
        });

        return panel;
    }


    private void searchBooks(DefaultTableModel model, String isbn, String title, String author, String category) {
        model.setRowCount(0);  // Clear previous search results

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librax", "root", "")) {
            String sql = "SELECT * FROM books WHERE 1=1";

            if (!isbn.isEmpty()) sql += " AND isbn LIKE ?";
            if (!title.isEmpty()) sql += " AND title LIKE ?";
            if (!author.isEmpty()) sql += " AND author LIKE ?";
            if (!category.isEmpty()) sql += " AND category LIKE ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int index = 1;
                if (!isbn.isEmpty()) stmt.setString(index++, "%" + isbn + "%");
                if (!title.isEmpty()) stmt.setString(index++, "%" + title + "%");
                if (!author.isEmpty()) stmt.setString(index++, "%" + author + "%");
                if (!category.isEmpty()) stmt.setString(index++, "%" + category + "%");

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getString("isbn"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getInt("year"),
                                rs.getString("category"),
                                rs.getInt("quantity"),
                                rs.getString("library"),
                                rs.getString("availability")
                        });
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error searching books: " + e.getMessage());
        }
    }





    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form fields
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        isbnField = new JTextField(15);
        panel.add(isbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(15);
        panel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        authorField = new JTextField(15);
        panel.add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        yearField = new JTextField(15);
        panel.add(yearField, gbc);

        // Category dropdown
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        categoryDropdown = new JComboBox<>(new String[]{"Fiction", "Non-Fiction", "Poetry", "Drama", "Romance", "Science"});
        panel.add(categoryDropdown, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField(15);
        panel.add(quantityField, gbc);

        // Library checkboxes
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Library:"), gbc);

        JPanel libraryPanel = new JPanel();
        magxakiCheckbox = new JCheckBox("Magxaki");
        kwadwesiCheckbox = new JCheckBox("Kwadwesi");

        libraryPanel.add(magxakiCheckbox);
        libraryPanel.add(kwadwesiCheckbox);

        gbc.gridx = 1;
        panel.add(libraryPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Availability:"), gbc);
        gbc.gridx = 1;
        availabilityDropdown = new JComboBox<>(new String[]{"Available", "Unavailable"});
        panel.add(availabilityDropdown, gbc);

        // Add book button (centered)
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Save Book");
        saveButton.addActionListener(e -> addBook());
        panel.add(saveButton, gbc);

        return panel;
    }


    private JPanel createBorrowedBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"Borrowed by", "ISBN", "Title", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // JTable for borrowed books
        JTable borrowedBooksTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load borrowed books data
        loadBorrowedBooks(model);

        return panel;
    }

    private JPanel createViewBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"ISBN", "Title", "Author", "Year", "Category", "Quantity", "Library", "Availability"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // JTable for viewing all books
        JTable booksTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load all books data
        loadAllBooks(model);

        return panel;
    }

    private void loadAllBooks(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librax", "root", "")) {
            String sql = "SELECT * FROM books";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    // Assuming the books table has these columns: isbn, title, author, year, category, quantity, library, availability
                    String isbn = rs.getString("isbn");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int year = rs.getInt("year");
                    String category = rs.getString("category");
                    int quantity = rs.getInt("quantity");
                    String library = rs.getString("library");

                    // Check if the quantity is 0 and update availability
                    String availability = (quantity == 0) ? "Unavailable" : rs.getString("availability");

                    // Add row to table model
                    model.addRow(new Object[]{isbn, title, author, year, category, quantity, library, availability});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading books: " + e.getMessage());
        }
    }


    private void loadBorrowedBooks(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librax", "root", "")) {
            String sql = "SELECT * FROM borrow WHERE status = 'borrowed'";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String isbn = rs.getString("isbn");
                    String title = rs.getString("title");
                    Date date = rs.getDate("date");

                    // Add row to table model
                    model.addRow(new Object[]{username, isbn, title, date});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading borrowed books: " + e.getMessage());
        }
    }

    private void switchPanel(String panelName) {
        CardLayout layout = (CardLayout) contentPanel.getLayout();
        layout.show(contentPanel, panelName);
    }

    private void addBook() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librax", "root", "")) {
            String sql = "INSERT INTO books (isbn, title, author, year, category, quantity, library, availability) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, isbnField.getText());
                stmt.setString(2, titleField.getText());
                stmt.setString(3, authorField.getText());
                stmt.setInt(4, Integer.parseInt(yearField.getText()));
                stmt.setString(5, categoryDropdown.getSelectedItem().toString());
                stmt.setInt(6, Integer.parseInt(quantityField.getText()));
                stmt.setString(7, getLibrarySelection());
                stmt.setString(8, availabilityDropdown.getSelectedItem().toString());

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Book Added Successfully!");
                refreshBookTable();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }


    private String getLibrarySelection() {
        StringBuilder selectedLibraries = new StringBuilder();

        if (magxakiCheckbox.isSelected()) {
            selectedLibraries.append("Magxaki ");
        }
        if (kwadwesiCheckbox.isSelected()) {
            selectedLibraries.append("Kwadwesi");
        }

        return selectedLibraries.toString().trim();
    }
}
