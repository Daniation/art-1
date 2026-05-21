package com.artmarketplace.ui.buyer;

import com.artmarketplace.model.User;
import com.artmarketplace.ui.auth.LoginView;
import com.artmarketplace.utils.SceneManager;
import com.artmarketplace.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class BuyerDashboard {

    public static BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        VBox sidebar = createSidebar();
        root.setLeft(sidebar);

        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40));
        mainContent.setAlignment(Pos.TOP_CENTER);

        User user = SessionManager.getInstance().getCurrentUser();

        Text welcomeText = new Text("Welcome, " + user.getFullName() + "!");
        welcomeText.getStyleClass().add("welcome-text");

        Text roleText = new Text("Buyer Dashboard");
        roleText.getStyleClass().add("role-text");

        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);

        VBox browseCard = createStatCard("Browse Artworks", "Explore our collection of unique artworks from talented artists", "Browse Now", e -> {
            SceneManager.switchScene(BrowseArtworksView.getView(), "dashboard.css");
        });

        VBox cartCard = createStatCard("My Cart", "View and manage items in your shopping cart", "View Cart", e -> {
            SceneManager.switchScene(CartView.getView(), "dashboard.css");
        });

        VBox ordersCard = createStatCard("My Orders", "Track the status of your placed orders", "View Orders", e -> {
            SceneManager.switchScene(OrdersView.getView(), "dashboard.css");
        });

        statsBox.getChildren().addAll(browseCard, cartCard, ordersCard);

        mainContent.getChildren().addAll(welcomeText, roleText, statsBox);
        root.setCenter(mainContent);

        return root;
    }

    private static VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.getStyleClass().add("sidebar");
        sidebar.setMinWidth(220);
        sidebar.setMaxWidth(220);

        Text logo = new Text("ART Marketplace");
        logo.getStyleClass().add("sidebar-logo");

        Label userLabel = new Label("Buyer: " + SessionManager.getInstance().getCurrentUser().getFullName());
        userLabel.getStyleClass().add("sidebar-user");

        Button browseBtn = createSidebarButton("Browse Artworks");
        browseBtn.setOnAction(e -> SceneManager.switchScene(BrowseArtworksView.getView(), "dashboard.css"));

        Button cartBtn = createSidebarButton("My Cart");
        cartBtn.setOnAction(e -> SceneManager.switchScene(CartView.getView(), "dashboard.css"));

        Button ordersBtn = createSidebarButton("My Orders");
        ordersBtn.setOnAction(e -> SceneManager.switchScene(OrdersView.getView(), "dashboard.css"));

        Button dashboardBtn = createSidebarButton("Dashboard");
        dashboardBtn.setOnAction(e -> SceneManager.switchScene(BuyerDashboard.getView(), "dashboard.css"));

        Button logoutBtn = new Button("Sign Out");
        logoutBtn.getStyleClass().add("logout-btn");
        logoutBtn.setOnAction(e -> {
            SessionManager.getInstance().logout();
            SceneManager.switchScene(LoginView.getView(), "login.css");
        });

        sidebar.getChildren().addAll(logo, userLabel, new Separator(), dashboardBtn, browseBtn, cartBtn, ordersBtn, new Separator(), logoutBtn);
        return sidebar;
    }

    private static Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("sidebar-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private static VBox createStatCard(String title, String description, String btnText, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        VBox card = new VBox(15);
        card.getStyleClass().add("stat-card");
        card.setPadding(new Insets(25));
        card.setMinWidth(280);
        card.setMaxWidth(280);
        card.setAlignment(Pos.CENTER);

        Text cardTitle = new Text(title);
        cardTitle.getStyleClass().add("card-title");

        Text cardDesc = new Text(description);
        cardDesc.getStyleClass().add("card-desc");
        cardDesc.setWrappingWidth(230);

        Button cardBtn = new Button(btnText);
        cardBtn.getStyleClass().add("card-btn");
        cardBtn.setOnAction(handler);

        card.getChildren().addAll(cardTitle, cardDesc, cardBtn);
        return card;
    }

    private static class Separator extends javafx.scene.shape.Line {
        public Separator() {
            super(0, 0, 180, 0);
            getStyleClass().add("sidebar-separator");
        }
    }
}
