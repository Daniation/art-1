package com.artmarketplace.ui.seller;

import com.artmarketplace.dao.SellerDAO;
import com.artmarketplace.model.User;
import com.artmarketplace.ui.auth.LoginView;
import com.artmarketplace.utils.SceneManager;
import com.artmarketplace.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class SellerDashboard {

    public static BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        VBox sidebar = createSidebar();
        root.setLeft(sidebar);

        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40));
        mainContent.setAlignment(Pos.TOP_CENTER);

        User user = SessionManager.getInstance().getCurrentUser();
        SellerDAO sellerDAO = new SellerDAO();

        Text welcomeText = new Text("Welcome, " + user.getFullName() + "!");
        welcomeText.getStyleClass().add("welcome-text");

        Text roleText = new Text("Seller Dashboard");
        roleText.getStyleClass().add("role-text");

        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);

        VBox totalArtCard = createStatCard(
            String.valueOf(sellerDAO.getTotalArtworks(user.getId())),
            "Total Artworks"
        );
        VBox availCard = createStatCard(
            String.valueOf(sellerDAO.getAvailableArtworks(user.getId())),
            "Available"
        );
        VBox soldCard = createStatCard(
            String.valueOf(sellerDAO.getSoldArtworks(user.getId())),
            "Sold"
        );
        VBox earningsCard = createStatCard(
            "$" + String.format("%.2f", sellerDAO.getTotalEarnings(user.getId())),
            "Total Earnings"
        );
        VBox ordersCard = createStatCard(
            String.valueOf(sellerDAO.getTotalOrders(user.getId())),
            "Total Orders"
        );

        statsRow.getChildren().addAll(totalArtCard, availCard, soldCard, earningsCard, ordersCard);

        VBox actionCards = new VBox(20);
        actionCards.setAlignment(Pos.CENTER);
        actionCards.setPadding(new Insets(20, 0, 0, 0));

        HBox actionRow = new HBox(20);
        actionRow.setAlignment(Pos.CENTER);

        VBox addCard = createActionCard("Add New Artwork", "List a new piece of art for sale", "Add Artwork", e -> {
            SceneManager.switchScene(AddArtworkView.getView(), "dashboard.css");
        });

        VBox myArtsCard = createActionCard("My Artworks", "View and manage your artworks", "Manage", e -> {
            SceneManager.switchScene(com.artmarketplace.ui.seller.EditArtworkView.getView(), "dashboard.css");
        });

        VBox ordersCard2 = createActionCard("Orders", "View orders for your artworks", "View Orders", e -> {
            SceneManager.switchScene(SellerOrdersView.getView(), "dashboard.css");
        });

        actionRow.getChildren().addAll(addCard, myArtsCard, ordersCard2);
        actionCards.getChildren().add(actionRow);

        mainContent.getChildren().addAll(welcomeText, roleText, statsRow, actionCards);
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

        Label userLabel = new Label("Seller: " + SessionManager.getInstance().getCurrentUser().getFullName());
        userLabel.getStyleClass().add("sidebar-user");

        javafx.scene.shape.Line sep1 = new javafx.scene.shape.Line(0, 0, 180, 0);
        sep1.getStyleClass().add("sidebar-separator");

        Button dashBtn = createSidebarButton("Dashboard");
        dashBtn.setOnAction(e -> SceneManager.switchScene(SellerDashboard.getView(), "dashboard.css"));

        Button addBtn = createSidebarButton("Add Artwork");
        addBtn.setOnAction(e -> SceneManager.switchScene(AddArtworkView.getView(), "dashboard.css"));

        Button editBtn = createSidebarButton("My Artworks");
        editBtn.setOnAction(e -> SceneManager.switchScene(EditArtworkView.getView(), "dashboard.css"));

        Button ordersBtn = createSidebarButton("Orders");
        ordersBtn.setOnAction(e -> SceneManager.switchScene(SellerOrdersView.getView(), "dashboard.css"));

        javafx.scene.shape.Line sep2 = new javafx.scene.shape.Line(0, 0, 180, 0);
        sep2.getStyleClass().add("sidebar-separator");

        Button logoutBtn = new Button("Sign Out");
        logoutBtn.getStyleClass().add("logout-btn");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> {
            SessionManager.getInstance().logout();
            SceneManager.switchScene(LoginView.getView(), "login.css");
        });

        sidebar.getChildren().addAll(logo, userLabel, sep1, dashBtn, addBtn, editBtn, ordersBtn, sep2, logoutBtn);
        return sidebar;
    }

    private static Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("sidebar-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private static VBox createStatCard(String value, String label) {
        VBox card = new VBox(5);
        card.getStyleClass().add("stat-card");
        card.setPadding(new Insets(20));
        card.setMinWidth(160);
        card.setMaxWidth(160);
        card.setAlignment(Pos.CENTER);

        Text valText = new Text(value);
        valText.getStyleClass().add("stat-value");

        Text lblText = new Text(label);
        lblText.getStyleClass().add("stat-label");

        card.getChildren().addAll(valText, lblText);
        return card;
    }

    private static VBox createActionCard(String title, String desc, String btnText, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        VBox card = new VBox(15);
        card.getStyleClass().add("stat-card");
        card.setPadding(new Insets(25));
        card.setMinWidth(250);
        card.setMaxWidth(250);
        card.setAlignment(Pos.CENTER);

        Text cardTitle = new Text(title);
        cardTitle.getStyleClass().add("card-title");

        Text cardDesc = new Text(desc);
        cardDesc.getStyleClass().add("card-desc");
        cardDesc.setWrappingWidth(220);

        Button cardBtn = new Button(btnText);
        cardBtn.getStyleClass().add("card-btn");
        cardBtn.setOnAction(handler);

        card.getChildren().addAll(cardTitle, cardDesc, cardBtn);
        return card;
    }
}
