package com.artmarketplace.factory;

import com.artmarketplace.model.User;
import com.artmarketplace.users.Admin;
import com.artmarketplace.users.Buyer;
import com.artmarketplace.users.Seller;

public class UserFactory {

    public static User createUser(int id, String username, String password, String email, String fullName, String role) {
        switch (role.toLowerCase()) {
            case "buyer":
                return new Buyer(id, username, password, email, fullName);
            case "seller":
                return new Seller(id, username, password, email, fullName);
            case "admin":
                return new Admin(id, username, password, email, fullName);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
