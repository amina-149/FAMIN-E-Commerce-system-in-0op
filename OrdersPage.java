package wearhouse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrdersPage extends JFrame {

    private JTable ordersTable;

    public OrdersPage() {
        setTitle("FAMIN - View Orders");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(new Color(34, 40, 49));
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Orders Placed");
        titleLabel.setForeground(new Color(0, 255, 191));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create the Back button
        JButton backButton = new JButton("⬅ Back");
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new admincontrolpanel().setVisible(true);
            dispose();
        });

        // Add title and back button to a top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(34, 40, 49));
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(backButton, BorderLayout.EAST); // Place the Back button on the right

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Order ID", "Customer Name", "Phone", "Product", "Quantity", "Order Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        ordersTable = new JTable(tableModel);
        ordersTable.setFillsViewportHeight(true);
        ordersTable.setBackground(new Color(57, 62, 70));
        ordersTable.setForeground(Color.WHITE);
        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ordersTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(new EmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        loadOrdersFromFile(tableModel);

        setVisible(true);
    }

    private void loadOrdersFromFile(DefaultTableModel model) {
        String filePath = "wearhouse/Orders.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String orderId = null, customerName = null, phone = null, timestamp = null;
            List<String> products = new ArrayList<>();
            List<String> quantities = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Check for separator to finalize an order
                if (line.startsWith("----------------------------------------")) {
                    if (orderId != null && customerName != null && phone != null && timestamp != null && !products.isEmpty()) {
                        // Add each product as a separate row
                        for (int i = 0; i < products.size(); i++) {
                            model.addRow(new Object[]{orderId, customerName, phone, products.get(i), quantities.get(i), timestamp});
                        }
                    }
                    // Reset for the next order
                    orderId = null;
                    customerName = null;
                    phone = null;
                    timestamp = null;
                    products.clear();
                    quantities.clear();
                    continue;
                }

                // Parse order fields
                if (line.startsWith("Order ID: ")) {
                    orderId = line.substring("Order ID: ".length()).trim();
                } else if (line.startsWith("Customer Name: ")) {
                    customerName = line.substring("Customer Name: ".length()).trim();
                } else if (line.startsWith("Phone: ")) {
                    phone = line.substring("Phone: ".length()).trim();
                } else if (line.startsWith("Timestamp: ")) {
                    timestamp = line.substring("Timestamp: ".length()).trim();
                } else if (!line.startsWith("Tracking ID: ") && !line.startsWith("Address: ") &&
                           !line.startsWith("Payment Method: ") && !line.startsWith("Items:") &&
                           !line.startsWith("Total: ") && !line.startsWith("Order Status: ") &&
                           !line.isEmpty()) {
                    // Parse product line, e.g., "lablis - $25700.00 x 1 = $25700.00"
                    String[] parts = line.split(" - \\$| x | = \\$");
                    if (parts.length >= 3) {
                        String productName = parts[0].trim();
                        String quantity = parts[2].trim();
                        products.add(productName);
                        quantities.add(quantity);
                    }
                }
            }

            // Handle the last order if it exists
            if (orderId != null && customerName != null && phone != null && timestamp != null && !products.isEmpty()) {
                for (int i = 0; i < products.size(); i++) {
                    model.addRow(new Object[]{orderId, customerName, phone, products.get(i), quantities.get(i), timestamp});
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading orders file:\n" + e.getMessage(),
                    "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrdersPage());
    }
}