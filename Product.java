package wearhouse;

import java.io.Serializable;

public class Product implements Serializable {
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
    public void setQuantity(int quantity) { this.quantity = quantity; }
}