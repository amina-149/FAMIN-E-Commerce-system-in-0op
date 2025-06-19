import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Homepage extends JFrame {
    private JPanel productGrid;
    private List<Product> allProducts;
    private JPanel heroPanel;
    private JScrollPane productScrollPane;
    private Cart cart;

    // Colors for consistency and aesthetics
    private final Color TEAL_ACCENT = new Color(0, 150, 136);
    private final Color TEAL_DARKER = new Color(0, 120, 110);
    private final Color LIGHT_GREY_BG = new Color(245, 245, 245);
    private final Color HERO_BG_COLOR = new Color(230, 245, 245);
    private final Color DARK_GREY_TEXT = new Color(33, 33, 33);
    private final Color MEDIUM_GREY_TEXT = new Color(97, 97, 97);
    private final Color LIGHT_BORDER_GREY = new Color(224, 224, 224);

    public Homepage() {
        setTitle("FAMIN - Home");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        // Initialize cart
        cart = new Cart();

        // --- Header Panel (North) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_GREY_BG);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel logo = new JLabel("📦 FAMIN");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logo.setForeground(TEAL_ACCENT);
        headerPanel.add(logo, BorderLayout.WEST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setBackground(LIGHT_GREY_BG);
        String[] navItems = {"Home", "Shop", "Men", "Women", "My Cart", "Admin"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            navButton.setBackground(Color.WHITE);
            navButton.setForeground(TEAL_ACCENT);
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            navButton.setFocusPainted(false);
            navButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            navButton.addActionListener(e -> {
                switch (item) {
                    case "Home":
                        showHeroSection();
                        break;
                    case "Shop":
                        displayProducts(allProducts);
                        break;
                    case "Men":
                        filterAndDisplayProducts("Men");
                        break;
                    case "Women":
                        filterAndDisplayProducts("Women");
                        break;
                    case "My Cart":
                        new ShoppingCartFrame(cart).setVisible(true);
                        break;
                    case "Admin":
                        JOptionPane.showMessageDialog(this, "Admin page not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        break;
                }
            });
            navPanel.add(navButton);
        }
        headerPanel.add(navPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Hero Section (Initially in Center) ---
        heroPanel = new JPanel();
        heroPanel.setBackground(HERO_BG_COLOR);
        heroPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));

        JLabel heroTitle = new JLabel("Welcome to FAMIN");
        heroTitle.setFont(new Font("Segoe UI", Font.BOLD, 48));
        heroTitle.setForeground(DARK_GREY_TEXT);
        heroTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heroSubtitle = new JLabel("Discover the latest in fashion for Men and Women");
        heroSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        heroSubtitle.setForeground(MEDIUM_GREY_TEXT);
        heroSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton shopButton = new JButton("Shop Now");
        shopButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        shopButton.setBackground(TEAL_ACCENT);
        shopButton.setForeground(Color.WHITE);
        shopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shopButton.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        shopButton.setFocusPainted(false);
        shopButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        shopButton.setRolloverEnabled(true);

        shopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                shopButton.setBackground(TEAL_DARKER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                shopButton.setBackground(TEAL_ACCENT);
            }
        });

        shopButton.addActionListener(e -> displayProducts(allProducts));

        heroPanel.add(Box.createVerticalGlue());
        heroPanel.add(heroTitle);
        heroPanel.add(Box.createVerticalStrut(15));
        heroPanel.add(heroSubtitle);
        heroPanel.add(Box.createVerticalStrut(30));
        heroPanel.add(shopButton);
        heroPanel.add(Box.createVerticalGlue());
        add(heroPanel, BorderLayout.CENTER);

        // --- Product Grid Container ---
        JPanel productGridContainer = new JPanel();
        productGridContainer.setLayout(new BoxLayout(productGridContainer, BoxLayout.Y_AXIS));
        productGridContainer.setBackground(Color.WHITE);

        productGrid = new JPanel();
        productGrid.setBackground(Color.WHITE);
        productGrid.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel productGridWrapper = new JPanel(new GridBagLayout());
        productGridWrapper.setBackground(Color.WHITE);
        productGridWrapper.add(productGrid);

        productScrollPane = new JScrollPane(productGridWrapper);
        productScrollPane.setBackground(Color.WHITE);
        productScrollPane.setBorder(BorderFactory.createEmptyBorder());
        productScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // --- Footer Panel (South) ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(LIGHT_GREY_BG);
        footerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        JLabel footerText = new JLabel("© 2025 FAMIN | Contact: support@famin.com | Follow us on social media");
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footerText.setForeground(MEDIUM_GREY_TEXT);
        footerPanel.add(footerText);
        add(footerPanel, BorderLayout.SOUTH);

        loadAllProducts();

        setVisible(true);
    }

    // --- Helper methods for product loading and display ---

    private void loadAllProducts() {
        File file = new File("wearhouse/products.ser");
        System.out.println("Checking products.ser at: " + file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("products.ser not found. Creating sample products.");
            allProducts = new ArrayList<>();
            allProducts.add(new Product("1", "Lablis", "Women", "Ed Edition", 25700.00, 10, ""));
            allProducts.add(new Product("2", "T-Shirt", "Men", "Casual", 1500.00, 20, ""));
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(allProducts);
                System.out.println("Sample products saved to products.ser.");
            } catch (IOException e) {
                System.err.println("Failed to save sample products: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    allProducts = (List<Product>) obj;
                    if (allProducts == null) {
                        allProducts = new ArrayList<>();
                        System.out.println("Loaded null list, initialized empty list.");
                    } else {
                        System.out.println("Loaded " + allProducts.size() + " products successfully.");
                    }
                } else {
                    System.out.println("Invalid product data format, initialized empty list.");
                    allProducts = new ArrayList<>();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error reading products from file: " + e.getMessage());
                allProducts = new ArrayList<>();
                e.printStackTrace();
            }
        }
        System.out.println("allProducts size after loading: " + (allProducts != null ? allProducts.size() : "null"));
    }
    
    private void displayProducts(List<Product> productsToDisplay) {
        Component currentCenter = ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (currentCenter != null) {
            remove(currentCenter);
        }

        productGrid.removeAll();
        productGrid.revalidate();
        productGrid.repaint();

        if (productsToDisplay == null || productsToDisplay.isEmpty()) {
            productGrid.setLayout(new FlowLayout(FlowLayout.CENTER));
            JLabel noProductsLabel = new JLabel("No products to display in this category.", SwingConstants.CENTER);
            noProductsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            noProductsLabel.setForeground(MEDIUM_GREY_TEXT);
            productGrid.add(noProductsLabel);
        } else {
            productGrid.setLayout(new GridLayout(0, 4, 20, 20));
            for (Product product : productsToDisplay) {
                JPanel productCard = createProductCard(product);
                productGrid.add(productCard);
            }
        }

        add(productScrollPane, BorderLayout.CENTER);
        productScrollPane.getVerticalScrollBar().setValue(0);
        revalidate();
        repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel productCard = new JPanel(new BorderLayout(10, 10));
        productCard.setBackground(Color.WHITE);

        Border defaultBorder = BorderFactory.createLineBorder(LIGHT_BORDER_GREY, 1);
        productCard.setBorder(defaultBorder);
        productCard.setPreferredSize(new Dimension(200, 350));
        productCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        productCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                productCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.DARK_GRAY),
                        new EmptyBorder(1, 1, 1, 1)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                productCard.setBorder(defaultBorder);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(Homepage.this,
                        "Product Details:\n" +
                                "Name: " + product.getName() + "\n" +
                                "Category: " + product.getCategory() + " - " + product.getSubcategory() + "\n" +
                                "Price: $" + String.format("%.2f", product.getPrice()) + "\n" +
                                "Quantity in Stock: " + product.getQuantity(),
                        "Product Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel imageLabel = new JLabel("No Image Available", SwingConstants.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            File imageFile = new File(product.getImagePath());
            if (imageFile.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(product.getImagePath());
                    if (icon.getImageLoadStatus() == MediaTracker.COMPLETE && icon.getIconWidth() > 0) {
                        Image img = icon.getImage().getScaledInstance(150, 180, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(img));
                        imageLabel.setText("");
                    } else {
                        imageLabel.setText("Image Load Failed");
                        System.err.println("Failed to load image for product: " + product.getName() + " at path: " + product.getImagePath());
                    }
                } catch (Exception ex) {
                    imageLabel.setText("Invalid Image Path");
                    System.err.println("Error loading image for product " + product.getName() + " from path " + product.getImagePath() + ": " + ex.getMessage());
                }
            } else {
                imageLabel.setText("Image Not Found");
                System.err.println("Image file not found for product: " + product.getName() + " at path: " + product.getImagePath());
            }
        }
        productCard.add(imageLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(DARK_GREY_TEXT);

        JLabel categoryLabel = new JLabel(product.getCategory() + " - " + product.getSubcategory());
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        categoryLabel.setForeground(MEDIUM_GREY_TEXT);

        JLabel priceLabel = new JLabel("$" + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(TEAL_ACCENT);

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addToCartButton.setBackground(TEAL_ACCENT);
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFocusPainted(false);
        addToCartButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addToCartButton.addActionListener(e -> {
            if (product.getQuantity() > 0) {
                cart.addProduct(product, 1);
                JOptionPane.showMessageDialog(Homepage.this, product.getName() + " added to cart!", "Cart", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(Homepage.this, "Sorry, " + product.getName() + " is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
            }
        });

        infoPanel.add(nameLabel);
        infoPanel.add(categoryLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(addToCartButton);
        productCard.add(infoPanel, BorderLayout.SOUTH);

        return productCard;
    }

    private void filterAndDisplayProducts(String category) {
        if (allProducts == null) {
            JOptionPane.showMessageDialog(this, "Products not loaded yet. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Product> filteredProducts = allProducts.stream()
                .filter(p -> p.getCategory() != null && p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        displayProducts(filteredProducts);
    }

    private void showHeroSection() {
        Component currentCenter = ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (currentCenter != null) {
            remove(currentCenter);
        }
        add(heroPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        File dbDir = new File("wearhouse");
        if (!dbDir.exists()) {
            if (!dbDir.mkdirs()) {
                System.err.println("Failed to create directory: " + dbDir.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Failed to create storage directory!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("Created directory: " + dbDir.getAbsolutePath());
        }
        SwingUtilities.invokeLater(Homepage::new);
    }

    // --- Inner Cart Class ---
    public class Cart implements Serializable {
        private static final long serialVersionUID = 1L;
        private List<Product> items;
        private List<Integer> quantities;

        public Cart() {
            items = new ArrayList<>();
            quantities = new ArrayList<>();
        }

        public void addProduct(Product product, int quantity) {
            int index = items.indexOf(product);
            if (index >= 0) {
                quantities.set(index, quantities.get(index) + quantity);
            } else {
                items.add(product);
                quantities.add(quantity);
            }
        }

        public List<Product> getItems() {
            return new ArrayList<>(items);
        }

        public List<Integer> getQuantities() {
            return new ArrayList<>(quantities);
        }

        public double getTotalPrice() {
            double total = 0;
            for (int i = 0; i < items.size(); i++) {
                total += items.get(i).getPrice() * quantities.get(i);
            }
            return total;
        }
    }

    // --- Inner ShoppingCartFrame Class ---
    public class ShoppingCartFrame extends JFrame {
        public ShoppingCartFrame(Cart cart) {
            setTitle("My Cart");
            setSize(600, 400);
            setLocationRelativeTo(Homepage.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel cartPanel = new JPanel();
            cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
            cartPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            cartPanel.setBackground(Color.WHITE);

            if (cart.getItems().isEmpty()) {
                JLabel emptyLabel = new JLabel("Your cart is empty.", SwingConstants.CENTER);
                emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                emptyLabel.setForeground(MEDIUM_GREY_TEXT);
                cartPanel.add(emptyLabel);
            } else {
                for (int i = 0; i < cart.getItems().size(); i++) {
                    Product p = cart.getItems().get(i);
                    int qty = cart.getQuantities().get(i);
                    JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
                    itemPanel.setBackground(Color.WHITE);
                    itemPanel.setBorder(BorderFactory.createLineBorder(LIGHT_BORDER_GREY, 1));

                    JLabel itemLabel = new JLabel(p.getName() + " - $" + String.format("%.2f", p.getPrice()) + " x " + qty);
                    itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    itemLabel.setForeground(DARK_GREY_TEXT);
                    itemPanel.add(itemLabel, BorderLayout.CENTER);

                    JButton removeButton = new JButton("Remove");
                    removeButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    removeButton.setBackground(Color.RED);
                    removeButton.setForeground(Color.WHITE);
                    removeButton.setFocusPainted(false);
                    removeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    final int index = i;
                    removeButton.addActionListener(e -> {
                        cart.getItems().remove(index);
                        cart.getQuantities().remove(index);
                        JOptionPane.showMessageDialog(this, p.getName() + " removed from cart!", "Cart Updated", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        new ShoppingCartFrame(cart).setVisible(true);
                    });
                    itemPanel.add(removeButton, BorderLayout.EAST);

                    cartPanel.add(itemPanel);
                    cartPanel.add(Box.createVerticalStrut(10));
                }

                JLabel totalLabel = new JLabel("Total: $" + String.format("%.2f", cart.getTotalPrice()));
                totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                totalLabel.setForeground(TEAL_ACCENT);
                totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                cartPanel.add(Box.createVerticalStrut(20));
                cartPanel.add(totalLabel);

                JButton buyNowButton = new JButton("Buy Now");
                buyNowButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
                buyNowButton.setBackground(TEAL_ACCENT);
                buyNowButton.setForeground(Color.WHITE);
                buyNowButton.setFocusPainted(false);
                buyNowButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                buyNowButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                buyNowButton.addActionListener(e -> placeOrder(cart));
                cartPanel.add(Box.createVerticalStrut(10));
                cartPanel.add(buyNowButton);
            }

            JScrollPane cartScrollPane = new JScrollPane(cartPanel);
            cartScrollPane.setBackground(Color.WHITE);
            cartScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(cartScrollPane);

            setVisible(true);
        }

        private void placeOrder(Cart cart) {
            if (cart.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Collect customer details
            JTextField nameField = new JTextField(20);
            JTextField addressField = new JTextField(20);
            JTextField phoneField = new JTextField(20);
            JPanel customerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            customerPanel.add(new JLabel("Name:"));
            customerPanel.add(nameField);
            customerPanel.add(new JLabel("Address:"));
            customerPanel.add(addressField);
            customerPanel.add(new JLabel("Phone:"));
            customerPanel.add(phoneField);

            int customerResult = JOptionPane.showConfirmDialog(this, customerPanel, "Enter Customer Details",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (customerResult != JOptionPane.OK_OPTION || nameField.getText().isEmpty() ||
                addressField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all customer details!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String customerName = nameField.getText();
            String customerAddress = addressField.getText();
            String customerPhone = phoneField.getText();

            // Payment method selection
            Object[] paymentOptions = {"Cash", "Online Payment"};
            int paymentChoice = JOptionPane.showOptionDialog(this,
                    "Select payment method:",
                    "Payment Option",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    paymentOptions,
                    paymentOptions[0]);

            if (paymentChoice == JOptionPane.CLOSED_OPTION) return;

            String paymentMethod = (paymentChoice == 0) ? "Cash" : "Online Payment";

            // Generate order IDs and timestamp
            String orderId = "ORD" + (int)(Math.random() * 1000000);
            String trackingId = "TRK" + (int)(Math.random() * 1000000);
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

            // Create order details
            StringBuilder orderDetails = new StringBuilder();
            orderDetails.append("Order ID: ").append(orderId).append("\n");
            orderDetails.append("Tracking ID: ").append(trackingId).append("\n");
            orderDetails.append("Timestamp: ").append(timestamp).append("\n");
            orderDetails.append("Customer Name: ").append(customerName).append("\n");
            orderDetails.append("Address: ").append(customerAddress).append("\n");
            orderDetails.append("Phone: ").append(customerPhone).append("\n");
            orderDetails.append("Payment Method: ").append(paymentMethod).append("\n\n");
            orderDetails.append("Items:\n");
            for (int i = 0; i < cart.getItems().size(); i++) {
                Product p = cart.getItems().get(i);
                int qty = cart.getQuantities().get(i);
                orderDetails.append(p.getName()).append(" - $").append(String.format("%.2f", p.getPrice()))
                        .append(" x ").append(qty).append(" = $").append(String.format("%.2f", p.getPrice() * qty)).append("\n");
            }
            orderDetails.append("\nTotal: $").append(String.format("%.2f", cart.getTotalPrice()));
            orderDetails.append("\n\nOrder Status: Placed");

            // Save to Orders.txt
            try {
                saveOrder(orderId, trackingId, timestamp, customerName, customerAddress, customerPhone, paymentMethod, cart, orderDetails.toString());
                saveSales(cart, orderId, timestamp);
                JOptionPane.showMessageDialog(this, orderDetails.toString(), "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Order saving failed: " + e.getMessage());
                e.printStackTrace();
                return;
            }

            // Clear cart after order
            cart.getItems().clear();
            cart.getQuantities().clear();
            dispose();
        }

        private void saveOrder(String orderId, String trackingId, String timestamp, String customerName,
                              String customerAddress, String customerPhone, String paymentMethod, Cart cart, String orderDetails) throws IOException {
            File dir = new File("wearhouse");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("wearhouse/Orders.txt");
            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                if (file.length() > 0) {
                    bw.newLine();
                    bw.write("----------------------------------------");
                    bw.newLine();
                }
                bw.write(orderDetails);
                bw.newLine();
            }
        }

        private void saveSales(Cart cart, String orderId, String timestamp) throws IOException {
            List<Sale> sales = new ArrayList<>();
            for (int i = 0; i < cart.getItems().size(); i++) {
                Product p = cart.getItems().get(i);
                int qty = cart.getQuantities().get(i);
                String saleId = orderId + "-" + i;
                sales.add(new Sale(saleId, p.getName(), qty, String.format("%.2f", p.getPrice() * qty), timestamp.split(" ")[0]));
            }
            File file = new File("wearhouse/Sales.ser");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(sales);
            }
        }
    }

    // --- Inner Product Class ---
    public static class Product implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;
        private String name;
        private String category;
        private String subcategory;
        private double price;
        private int quantity;
        private String imagePath;

        public Product(String id, String name, String category, String subcategory, double price, int quantity, String imagePath) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.subcategory = subcategory;
            this.price = price;
            this.quantity = quantity;
            this.imagePath = imagePath;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public String getSubcategory() { return subcategory; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public String getImagePath() { return imagePath; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return id != null ? id.equals(product.id) : product.id == null;
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    // --- Inner Order Class ---
    public static class Order {
        private String orderId;
        private String trackingId;
        private String timestamp;
        private String customerName;
        private String customerAddress;
        private String customerPhone;
        private String paymentMethod;
        private List<Product> items;
        private List<Integer> quantities;
        private double totalPrice;

        public Order(String orderId, String trackingId, String timestamp, String customerName,
                     String customerAddress, String customerPhone, String paymentMethod,
                     List<Product> items, List<Integer> quantities, double totalPrice) {
            this.orderId = orderId;
            this.trackingId = trackingId;
            this.timestamp = timestamp;
            this.customerName = customerName;
            this.customerAddress = customerAddress;
            this.customerPhone = customerPhone;
            this.paymentMethod = paymentMethod;
            this.items = new ArrayList<>(items != null ? items : new ArrayList<>());
            this.quantities = new ArrayList<>(quantities != null ? quantities : new ArrayList<>());
            this.totalPrice = totalPrice;
        }
    }

    // --- Inner Sale Class ---
    private static class Sale implements Serializable {
        private static final long serialVersionUID = 1L;
        private String saleId;
        private String productName;
        private int quantitySold;
        private String saleAmount;
        private String saleDate;

        public Sale(String saleId, String productName, int quantitySold, String saleAmount, String saleDate) {
            this.saleId = saleId;
            this.productName = productName;
            this.quantitySold = quantitySold;
            this.saleAmount = saleAmount;
            this.saleDate = saleDate;
        }
    }
}