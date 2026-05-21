package com.artmarketplace.ui.admin;

import com.artmarketplace.dao.UserDAO;
import com.artmarketplace.model.User;
import com.artmarketplace.ui.auth.LoginView;
import com.artmarketplace.utils.SceneManager;
import com.artmarketplace.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class AdminDashboard {

    public static BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        VBox sidebar = createSidebar();
        root.setLeft(sidebar);

        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40));
        mainContent.setAlignment(Pos.TOP_CENTER);

        User user = SessionManager.getInstance().getCurrentUser();
        UserDAO userDAO = new UserDAO();

        Text welcomeText = new Text("Welcome, " + user.getFullName() + "!");
        welcomeText.getStyleClass().add("welcome-text");

        Text roleText = new Text("Admin Dashboard");
        roleText.getStyleClass().add("role-text");

        long userCount = userDAO.getAllUsers().stream().count();
        long buyerCount = userDAO.getAllUsers().stream().filter(u -> "buyer".equals(u.getRole())).count();
        long sellerCount = userDAO.getAllUsers().stream().filter(u -> "seller".equals(u.getRole())).count();

        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);

        statsRow.getChildren().addAll(
            createStatCard(String.valueOf(userCount), "Total Users"),
            createStatCard(String.valueOf(buyerCount), "Buyers"),
            createStatCard(String.valueOf(sellerCount), "Sellers")
        );

        HBox actionCards = new HBox(20);
        actionCards.setAlignment(Pos.CENTER);
        actionCards.setPadding(new Insets(20, 0, 0, 0));

        VBox usersCard = createActionCard("Manage Users", "View, add or remove system users", "Manage Users", e -> {
            SceneManager.switchScene(ManageUsersView.getView(), "dashboard.css");
        });

        VBox ordersCard = createActionCard("Manage Orders", "View and update all orders", "Manage Orders", e -> {
            SceneManager.switchScene(ManageOrdersView.getView(), "dashboard.css");
        });

        actionCards.getChildren().addAll(usersCard, ordersCard);

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

        Label userLabel = new Label("Admin: " + SessionManager.getInstance().getCurrentUser().getFullName());
        userLabel.getStyleClass().add("sidebar-user");

        javafx.scene.shape.Line sep1 = new javafx.scene.shape.Line(0, 0, 180, 0);
        sep1.getStyleClass().add("sidebar-separator");

        Button dashBtn = createSidebarButton("Dashboard");
        dashBtn.setOnAction(e -> SceneManager.switchScene(AdminDashboard.getView(), "dashboard.css"));

        Button usersBtn = createSidebarButton("Manage Users");
        usersBtn.setOnAction(e -> SceneManager.switchScene(ManageUsersView.getView(), "dashboard.css"));

        Button ordersBtn = createSidebarButton("Manage Orders");
        ordersBtn.setOnAction(e -> SceneManager.switchScene(ManageOrdersView.getView(), "dashboard.css"));

        javafx.scene.shape.Line sep2 = new javafx.scene.shape.Line(0, 0, 180, 0);
        sep2.getStyleClass().add("sidebar-separator");

        Button logoutBtn = new Button("Sign Out");
        logoutBtn.getStyleClass().add("logout-btn");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> {
            SessionManager.getInstance().logout();
            SceneManager.switchScene(LoginView.getView(), "login.css");
        });

        sidebar.getChildren().addAll(logo, userLabel, sep1, dashBtn, usersBtn, ordersBtn, sep2, logoutBtn);
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
        card.setMinWidth(180);
        card.setMaxWidth(180);
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
        card.setMinWidth(300);
        card.setMaxWidth(300);
        card.setAlignment(Pos.CENTER);

        Text cardTitle = new Text(title);
        cardTitle.getStyleClass().add("card-title");

        Text cardDesc = new Text(desc);
        cardDesc.getStyleClass().add("card-desc");
        cardDesc.setWrappingWidth(260);

        Button cardBtn = new Button(btnText);
        cardBtn.getStyleClass().add("card-btn");
        cardBtn.setOnAction(handler);

        card.getChildren().addAll(cardTitle, cardDesc, cardBtn);
        return card;
    }
}
