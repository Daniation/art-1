package com.artmarketplace.service;

import com.artmarketplace.dao.UserDAO;
import com.artmarketplace.factory.UserFactory;
import com.artmarketplace.model.User;

public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) {
        User user = userDAO.login(username, password);
        if (user != null) {
            return UserFactory.createUser(user.getId(), user.getUsername(), user.getPassword(),
                    user.getEmail(), user.getFullName(), user.getRole());
        }
        return null;
    }

    public boolean register(String username, String password, String email, String fullName, String role) {
        if (userDAO.isUsernameTaken(username)) {
            return false;
        }
        User user = new User(0, username, password, email, fullName, role);
        return userDAO.register(user);
    }

    public boolean isUsernameTaken(String username) {
        return userDAO.isUsernameTaken(username);
    }
}
