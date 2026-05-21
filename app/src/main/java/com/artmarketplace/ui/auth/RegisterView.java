package com.artmarketplace.ui.auth;

import com.artmarketplace.service.AuthService;
import com.artmarketplace.utils.AlertUtil;
import com.artmarketplace.utils.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class RegisterView {

    public static VBox getView() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setId("login-root");

        Text title = new Text("Create Account");
        title.setId("login-title");

        Text subtitle = new Text("Join ART Marketplace today");
        subtitle.setId("login-subtitle");

        VBox formBox = new VBox(12);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(400);
        formBox.setId("login-form");

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        fullNameField.getStyleClass().add("login-field");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("login-field");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("login-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("login-field");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.getStyleClass().add("login-field");

        Label roleLabel = new Label("Account Type");
        roleLabel.getStyleClass().add("role-label");

        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton buyerRadio = new RadioButton("Buyer");
        buyerRadio.setToggleGroup(roleGroup);
        buyerRadio.setSelected(true);
        buyerRadio.getStyleClass().add("role-radio");

        RadioButton sellerRadio = new RadioButton("Seller");
        sellerRadio.setToggleGroup(roleGroup);
        sellerRadio.getStyleClass().add("role-radio");

        HBox roleBox = new HBox(20, buyerRadio, sellerRadio);
        roleBox.setAlignment(Pos.CENTER);

        Button registerBtn = new Button("Create Account");
        registerBtn.getStyleClass().add("login-btn");

        Hyperlink loginLink = new Hyperlink("Already have an account? Sign in");
        loginLink.getStyleClass().add("register-link");

        formBox.getChildren().addAll(
            fullNameField, emailField, usernameField, passwordField, confirmPasswordField,
            roleLabel, roleBox, registerBtn, loginLink
        );

        root.getChildren().addAll(title, subtitle, formBox);

        registerBtn.setOnAction(e -> {
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confirm = confirmPasswordField.getText().trim();

            if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                AlertUtil.showError("Error", "Please fill in all fields!");
                return;
            }

            if (!password.equals(confirm)) {
                AlertUtil.showError("Error", "Passwords do not match!");
                return;
            }

            if (password.length() < 6) {
                AlertUtil.showError("Error", "Password must be at least 6 characters!");
                return;
            }

            String role = buyerRadio.isSelected() ? "buyer" : "seller";

            AuthService authService = new AuthService();
            boolean success = authService.register(username, password, email, fullName, role);

            if (success) {
                AlertUtil.showInfo("Success", "Account created successfully! Please sign in.");
                SceneManager.switchScene(LoginView.getView(), "login.css");
            } else {
                AlertUtil.showError("Error", "Username or email already exists!");
            }
        });

        loginLink.setOnAction(e -> {
            SceneManager.switchScene(LoginView.getView(), "login.css");
        });

        return root;
    }
}
