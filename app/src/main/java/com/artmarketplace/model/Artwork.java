package com.artmarketplace.model;

public class Artwork {
    private int id;
    private String title;
    private String description;
    private double price;
    private String imagePath;
    private int sellerId;
    private String sellerName;
    private String status;

    public Artwork() {}

    public Artwork(int id, String title, String description, double price, String imagePath, int sellerId, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
        this.sellerId = sellerId;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
