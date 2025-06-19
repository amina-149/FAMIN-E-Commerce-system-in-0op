package wearhouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class admincontrolpanel extends JFrame {

    private JPanel contentPane;
    private JPanel sidePanel;
    private JPanel dashboardPanel;
    private boolean isSidePanelVisible = true;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public admincontrolpanel() {
        setTitle("FAMIN Admin Dashboard - CEO Amina");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(245, 255, 250));
        setContentPane(contentPane);

        sidePanel = new JPanel();
        sidePanel.setBackground(new Color(70, 130, 180));
        sidePanel.setPreferredSize(new Dimension(220, screenSize.height));
        sidePanel.setLayout(null);

        JLabel sideTitle = new JLabel("FAMIN Admin");
        sideTitle.setForeground(Color.WHITE);
        sideTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sideTitle.setBounds(40, 20, 150, 40);
        sidePanel.add(sideTitle);

        // Buttons with updated order and new Manage Customers button at 180 y-position
        JButton btnProducts = createSideButton("Manage Products", 100);
        JButton btnOrders = createSideButton("View Orders", 150);
// New button
        JButton btnSales = createSideButton("Monthly Sales", 230);          // shifted down
        JButton btnLogout = createSideButton("Logout", 300);
        btnLogout.setBackground(new Color(220, 20, 60));
        JButton btnBack = createSideButton("Back", 350);
        btnBack.setBackground(Color.DARK_GRAY);

        sidePanel.add(btnProducts);
        sidePanel.add(btnOrders);
       
        sidePanel.add(btnSales);
        sidePanel.add(btnLogout);
        sidePanel.add(btnBack);

        contentPane.add(sidePanel, BorderLayout.WEST);

        JPanel header = new JPanel(null);
        header.setBackground(new Color(60, 179, 113));
        header.setPreferredSize(new Dimension(screenSize.width - 220, 80));

        JLabel titleLabel = new JLabel("Welcome Amina - CEO of FAMIN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(70, 20, 400, 40);
        header.add(titleLabel);

        JButton toggleNavBtn = new JButton("☰");
        toggleNavBtn.setBounds(10, 20, 50, 40);
        toggleNavBtn.setFocusPainted(false);
        toggleNavBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        header.add(toggleNavBtn);

        JTextField searchField = new JTextField(" Search Inventory...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(screenSize.width - 220 - 270, 20, 250, 35);
        header.add(searchField);

        contentPane.add(header, BorderLayout.NORTH);

        dashboardPanel = new JPanel(null);
        dashboardPanel.setBackground(Color.WHITE);

        JLabel dashboardText = new JLabel("This is your FAMIN Admin Control Panel.");
        dashboardText.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        dashboardText.setBounds(30, 30, 500, 30);
        dashboardPanel.add(dashboardText);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(30, 80, 500, 300);
        ImageIcon imageIcon = new ImageIcon("src/wearhouse/images/adminpanel.jpg");
        Image scaledImage = imageIcon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
        dashboardPanel.add(imageLabel);

        contentPane.add(dashboardPanel, BorderLayout.CENTER);

        btnProducts.addActionListener(e -> {
            new ProductManagementPage().setVisible(true);
            dispose();
        });

        btnOrders.addActionListener(e -> {
            new OrdersPage().setVisible(true);
            dispose();
        });

       

        btnSales.addActionListener(e -> {
            new SalesReportPage().setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        btnBack.addActionListener(e -> {
            new Wearhousemainpage().setVisible(true);
            dispose();
        });

        toggleNavBtn.addActionListener(e -> toggleSidePanel());

        pack();
        setVisible(true);
    }

    private JButton createSideButton(String text, int y) {
        JButton button = new JButton(text);
        button.setBounds(20, y, 180, 40);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void toggleSidePanel() {
        isSidePanelVisible = !isSidePanelVisible;
        sidePanel.setVisible(isSidePanelVisible);
        contentPane.revalidate();
        contentPane.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new admincontrolpanel());
    }
}
