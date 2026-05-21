package com.artmarketplace.users;

import com.artmarketplace.model.User;

public class Buyer extends User {

    public Buyer(int id, String username, String password, String email, String fullName) {
        super(id, username, password, email, fullName, "buyer");
    }

    public void browseArtworks() {
        System.out.println("Buyer " + fullName + " is browsing artworks.");
    }

    public void addToCart(int artworkId) {
        System.out.println("Artwork " + artworkId + " added to cart.");
    }

    public void placeOrder() {
        System.out.println("Order placed by " + fullName);
    }
}
