package com.artmarketplace.users;

import com.artmarketplace.model.User;

public class Seller extends User {

    public Seller(int id, String username, String password, String email, String fullName) {
        super(id, username, password, email, fullName, "seller");
    }

    public void addArtwork() {
        System.out.println("Seller " + fullName + " added a new artwork.");
    }

    public void editArtwork(int artworkId) {
        System.out.println("Seller " + fullName + " edited artwork " + artworkId);
    }

    public void deleteArtwork(int artworkId) {
        System.out.println("Seller " + fullName + " deleted artwork " + artworkId);
    }

    public void updateOrderStatus(int orderId, String status) {
        System.out.println("Order " + orderId + " status updated to " + status);
    }
}
