package wearhouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Wearhousemainpage extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Color FAMIN_DARK_GRAY = new Color(40, 40, 40); // Dark gray for background
    private static final Color FAMIN_BLUE = new Color(0, 123, 255);    // Blue for buttons/highlights
    private static final Color FAMIN_GREEN = new Color(40, 167, 69);   // Green
    private static final String BANNER_IMAGE_PATH = "wearhouse/images/banner1.png"; // Path to banner image

    public Wearhousemainpage() {
        setTitle("FAMIN Warehouse");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(FAMIN_DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Banner
        JLabel bannerLabel = createBannerLabel(BANNER_IMAGE_PATH);
        if (bannerLabel != null) {
            gbc.gridy = 0;
            mainPanel.add(bannerLabel, gbc);
        } else {
            JLabel titleLabel = new JLabel("FAMIN Warehouse", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            titleLabel.setForeground(Color.WHITE);
            gbc.gridy = 0;
            mainPanel.add(titleLabel, gbc);
        }

        // Admin Button
        gbc.gridy = 1;
        JButton adminButton = createStyledButton("Admin Panel", FAMIN_BLUE);
        mainPanel.add(adminButton, gbc);

        // Customer Button
        gbc.gridy = 2;
        JButton customerButton = createStyledButton("Customer Panel", FAMIN_GREEN);
        mainPanel.add(customerButton, gbc);

        add(mainPanel);

        // Button Actions
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new admincontrolpanel().setVisible(true); // ✅ Fixed instantiation
                dispose();
            }
        });

        customerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Homepage().setVisible(true);
                dispose();
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 70));
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel createBannerLabel(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            int panelWidth = getWidth();
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            int scaledHeight = (int) (panelWidth * (double) originalHeight / originalWidth);

            Image scaledImage = image.getScaledInstance(panelWidth, scaledHeight, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(scaledImage);
            JLabel bannerLabel = new JLabel(imageIcon);
            bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return bannerLabel;
        } catch (IOException e) {
            System.err.println("Error loading banner image: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Wearhousemainpage().setVisible(true));
    }
}
