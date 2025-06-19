package wearhouse;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Shipment extends JFrame {

    private JLabel deliveredLabel;
    private JLabel inProgressLabel;
    private JButton backButton;

    public Shipment() {
        setTitle("FAMIN - Shipment Tracking");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(34, 40, 49)); // Dark background
        setLayout(new GridBagLayout());

        Font titleFont = new Font("Segoe UI", Font.BOLD, 36);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 22);

        JLabel titleLabel = new JLabel("Shipment Tracking");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(0, 255, 191)); // Brand teal color

        deliveredLabel = new JLabel("Delivered Orders: 0");
        deliveredLabel.setFont(labelFont);
        deliveredLabel.setForeground(Color.WHITE);

        inProgressLabel = new JLabel("In-Progress Orders: 0");
        inProgressLabel.setFont(labelFont);
        inProgressLabel.setForeground(Color.WHITE);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        refreshButton.setBackground(new Color(0, 255, 191));
        refreshButton.setForeground(new Color(34, 40, 49));
        refreshButton.setFocusPainted(false);

        refreshButton.addActionListener(e -> loadShipmentStatus());

        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backButton.setBackground(new Color(60, 90, 153));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        backButton.addActionListener(e -> {
            // Close current window to simulate going back
            dispose();
            // Optionally open main dashboard here if you have one
            // new Dashboard().setVisible(true);
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(deliveredLabel, gbc);

        gbc.gridx = 1;
        add(inProgressLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(refreshButton, gbc);

        gbc.gridx = 1;
        add(backButton, gbc);

        loadShipmentStatus();
    }

    private void loadShipmentStatus() {
        int deliveredCount = 0;
        int inProgressCount = 0;

        File file = new File("wearhouse/database/Shipments.txt");  // Adjust path if needed
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Shipments.txt file not found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String status = parts[4].trim().toLowerCase();
                    if (status.equals("delivered")) {
                        deliveredCount++;
                    } else if (status.equals("in progress")) {
                        inProgressCount++;
                    }
                }
            }

            deliveredLabel.setText("Delivered Orders: " + deliveredCount);
            inProgressLabel.setText("In-Progress Orders: " + inProgressCount);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading shipment file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Shipment().setVisible(true));
    }
}
