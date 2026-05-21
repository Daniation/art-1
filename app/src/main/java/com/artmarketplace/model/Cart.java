package com.artmarketplace.model;

public class Cart {
    private int id;
    private int buyerId;
    private int artworkId;
    private String artworkTitle;
    private double price;
    private String imagePath;

    public Cart() {}

    public Cart(int id, int buyerId, int artworkId) {
        this.id = id;
        this.buyerId = buyerId;
        this.artworkId = artworkId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBuyerId() { return buyerId; }
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }

    public int getArtworkId() { return artworkId; }
    public void setArtworkId(int artworkId) { this.artworkId = artworkId; }

    public String getArtworkTitle() { return artworkTitle; }
    public void setArtworkTitle(String artworkTitle) { this.artworkTitle = artworkTitle; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
