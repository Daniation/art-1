package com.artmarketplace.utils;

import com.artmarketplace.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isBuyer() {
        return currentUser != null && "buyer".equals(currentUser.getRole());
    }

    public boolean isSeller() {
        return currentUser != null && "seller".equals(currentUser.getRole());
    }

    public boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }

    public void logout() {
        currentUser = null;
    }
}
