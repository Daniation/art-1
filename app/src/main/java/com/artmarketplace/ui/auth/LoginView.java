package com.artmarketplace.ui.auth;

import com.artmarketplace.model.User;
import com.artmarketplace.service.AuthService;
import com.artmarketplace.ui.admin.AdminDashboard;
import com.artmarketplace.ui.buyer.BuyerDashboard;
import com.artmarketplace.ui.seller.SellerDashboard;
import com.artmarketplace.utils.AlertUtil;
import com.artmarketplace.utils.SceneManager;
import com.artmarketplace.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginView {

    public static VBox getView() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60, 40, 40, 40));
        root.setId("login-root");

        Text title = new Text("ART Marketplace");
        title.setId("login-title");

        Text subtitle = new Text("Sign in to your account");
        subtitle.setId("login-subtitle");

        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(400);
        formBox.setId("login-form");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("login-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("login-field");

        Button loginBtn = new Button("Sign In");
        loginBtn.getStyleClass().add("login-btn");

        Hyperlink registerLink = new Hyperlink("Don't have an account? Register");
        registerLink.getStyleClass().add("register-link");

        formBox.getChildren().addAll(usernameField, passwordField, loginBtn, registerLink);

        root.getChildren().addAll(title, subtitle, formBox);

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                AlertUtil.showError("Error", "Please fill in all fields!");
                return;
            }

            AuthService authService = new AuthService();
            User user = authService.login(username, password);

            if (user != null) {
                SessionManager.getInstance().setCurrentUser(user);
                AlertUtil.showInfo("Welcome", "Welcome back, " + user.getFullName() + "!");

                switch (user.getRole()) {
                    case "buyer":
                        SceneManager.switchScene(BuyerDashboard.getView(), "dashboard.css");
                        break;
                    case "seller":
                        SceneManager.switchScene(SellerDashboard.getView(), "dashboard.css");
                        break;
                    case "admin":
                        SceneManager.switchScene(AdminDashboard.getView(), "dashboard.css");
                        break;
                }
            } else {
                AlertUtil.showError("Login Failed", "Invalid username or password!");
            }
        });

        registerLink.setOnAction(e -> {
            SceneManager.switchScene(RegisterView.getView(), "login.css");
        });

        return root;
    }
}
