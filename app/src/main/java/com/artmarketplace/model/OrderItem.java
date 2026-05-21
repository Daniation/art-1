package com.artmarketplace.model;

public class OrderItem {
    private int id;
    private int orderId;
    private int artworkId;
    private String artworkTitle;
    private double price;

    public OrderItem() {}

    public OrderItem(int id, int orderId, int artworkId, double price) {
        this.id = id;
        this.orderId = orderId;
        this.artworkId = artworkId;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getArtworkId() { return artworkId; }
    public void setArtworkId(int artworkId) { this.artworkId = artworkId; }

    public String getArtworkTitle() { return artworkTitle; }
    public void setArtworkTitle(String artworkTitle) { this.artworkTitle = artworkTitle; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
