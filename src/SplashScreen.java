import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {
    private JProgressBar progressBar;

    public SplashScreen() {
        setTitle("Loading...");
        setUndecorated(false); // Remove window borders
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make full-screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("LibraX...", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        // Progress Bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.BOLD, 15)); // Smaller font size
        progressBar.setPreferredSize(new Dimension(400, 25)); // Set preferred width and height

        // Panel to hold the title and progress bar
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc); // Title at the top

        gbc.gridy = 1; // Progress bar below the title
        panel.add(progressBar, gbc);

        // Centering the panel in the frame
        add(panel, BorderLayout.CENTER);

        setVisible(true);
        loadProgress();
    }

    private void loadProgress() {
        try {
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(50); // Simulate loading
                progressBar.setValue(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dispose(); // Close splash screen
        openMainClass();
    }

    private void openMainClass() {
        Main.main(new String[]{}); // Open the Main class
    }

    public static void main(String[] args) {
        new SplashScreen();
    }
}
