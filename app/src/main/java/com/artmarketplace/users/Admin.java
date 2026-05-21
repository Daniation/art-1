package com.artmarketplace.users;

import com.artmarketplace.model.User;

public class Admin extends User {

    public Admin(int id, String username, String password, String email, String fullName) {
        super(id, username, password, email, fullName, "admin");
    }

    public void manageUsers() {
        System.out.println("Admin " + fullName + " is managing users.");
    }

    public void manageOrders() {
        System.out.println("Admin " + fullName + " is managing orders.");
    }

    public void updateOrderStatus(int orderId, String status) {
        System.out.println("Admin updated order " + orderId + " to " + status);
    }
}
