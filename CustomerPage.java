package wearhouse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class CustomerPage extends JFrame {
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JButton btnBack;

    public CustomerPage() {
        setTitle("Customer Order Records - FAMIN");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set background color to match the app's theme
        getContentPane().setBackground(new Color(34, 40, 49));

        // Columns for the order table
        String[] columnNames = {"Order ID", "Customer Name", "Product Ordered", "Quantity", "Total Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        orderTable = new JTable(tableModel);
        orderTable.setBackground(new Color(57, 62, 70));
        orderTable.setForeground(Color.WHITE);
        orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        orderTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Create and style the Back button
        btnBack = new JButton("⬅ Back to Dashboard");
        btnBack.setBackground(Color.DARK_GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            try {
                // Temporarily disable navigation to AdminControlPanel due to unresolved compilation error
                // new AdminControlPanel().setVisible(true); // Uncomment this once AdminControlPanel is fixed
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error navigating back: " + ex.getMessage(), 
                    "Navigation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(34, 40, 49));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnBack, BorderLayout.SOUTH);

        add(panel);

        loadOrderData();

        setVisible(true);
    }

    private void loadOrderData() {
        String filePath = "wearhouse/orders.txt"; // Path to orders.txt
        File file = new File(filePath);
        System.out.println("Attempting to read file: " + file.getAbsolutePath());

        // Check if file exists
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, 
                "orders.txt file not found at: " + file.getAbsolutePath() + 
                "\nPlease ensure the file exists in the correct directory.", 
                "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            tableModel.setRowCount(0); // Clear existing rows

            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",");
                // Trim spaces for clean data
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }

                // Validate data length and content
                if (data.length == 5) {
                    try {
                        // Validate Quantity and Total Price are numeric
                        Integer.parseInt(data[3]);   // Quantity
                        Double.parseDouble(data[4]); // Total Price
                        tableModel.addRow(data);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid data format in line: " + line);
                        JOptionPane.showMessageDialog(this, 
                            "Invalid data format in orders.txt for line: " + line + 
                            "\nQuantity and Total Price must be numeric.", 
                            "Data Error", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    System.err.println("Incorrect number of fields in line: " + line);
                    JOptionPane.showMessageDialog(this, 
                        "Incorrect number of fields in orders.txt for line: " + line + 
                        "\nExpected 5 fields (Order ID, Customer Name, Product Ordered, Quantity, Total Price).", 
                        "Data Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, 
                "orders.txt file not found at: " + file.getAbsolutePath(), 
                "File Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error reading orders.txt: " + e.getMessage(), 
                "IO Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new CustomerPage();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error starting CustomerPage: " + e.getMessage(), 
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}