package com.artmarketplace.ui.admin;

import com.artmarketplace.dao.UserDAO;
import com.artmarketplace.model.User;
import com.artmarketplace.utils.AlertUtil;
import com.artmarketplace.utils.SceneManager;
import com.artmarketplace.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import java.util.List;

public class ManageUsersView {

    public static BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        VBox sidebar = AdminDashboard_Sidebar.createSidebar();
        root.setLeft(sidebar);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));

        Text pageTitle = new Text("Manage Users");
        pageTitle.getStyleClass().add("page-title");

        VBox usersList = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(usersList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        mainContent.getChildren().addAll(pageTitle, scrollPane);
        root.setCenter(mainContent);

        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();

        HBox header = new HBox(15);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.getStyleClass().add("list-header");

        Label idH = new Label("ID");
        idH.getStyleClass().add("header-text");
        idH.setMinWidth(50);

        Label userH = new Label("Username");
        userH.getStyleClass().add("header-text");
        userH.setMinWidth(120);

        Label nameH = new Label("Full Name");
        nameH.getStyleClass().add("header-text");
        nameH.setMinWidth(150);

        Label emailH = new Label("Email");
        emailH.getStyleClass().add("header-text");
        emailH.setMinWidth(180);

        Label roleH = new Label("Role");
        roleH.getStyleClass().add("header-text");
        roleH.setMinWidth(80);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(idH, userH, nameH, emailH, roleH, spacer);
        usersList.getChildren().add(header);

        for (User user : users) {
            if ("admin".equals(user.getRole()) && user.getId() == SessionManager.getInstance().getCurrentUser().getId()) {
                continue;
            }

            HBox row = new HBox(15);
            row.setPadding(new Insets(12, 20, 12, 20));
            row.getStyleClass().add("list-row");
            row.setAlignment(Pos.CENTER_LEFT);

            Label idText = new Label(String.valueOf(user.getId()));
            idText.setMinWidth(50);
            Label userText = new Label(user.getUsername());
            userText.setMinWidth(120);
            Label nameText = new Label(user.getFullName());
            nameText.setMinWidth(150);
            Label emailText = new Label(user.getEmail());
            emailText.setMinWidth(180);
            Label roleText = new Label(user.getRole());
            roleText.setMinWidth(80);
            roleText.getStyleClass().add("role-" + user.getRole());

            Region spacer2 = new Region();
            HBox.setHgrow(spacer2, Priority.ALWAYS);

            Button deleteBtn = new Button("Delete");
            deleteBtn.getStyleClass().add("remove-btn");

            int userId = user.getId();
            deleteBtn.setOnAction(e -> {
                if (AlertUtil.showConfirm("Delete User", "Delete user \"" + user.getUsername() + "\"?")) {
                    if (userDAO.deleteUser(userId)) {
                        AlertUtil.showInfo("Deleted", "User removed!");
                        SceneManager.switchScene(ManageUsersView.getView(), "dashboard.css");
                    } else {
                        AlertUtil.showError("Error", "Cannot delete admin users!");
                    }
                }
            });

            row.getChildren().addAll(idText, userText, nameText, emailText, roleText, spacer2, deleteBtn);
            usersList.getChildren().add(row);
        }

        return root;
    }

    static class AdminDashboard_Sidebar {
        static VBox createSidebar() {
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

            Button dashBtn = new Button("Dashboard");
            dashBtn.getStyleClass().add("sidebar-btn");
            dashBtn.setMaxWidth(Double.MAX_VALUE);
            dashBtn.setOnAction(e -> SceneManager.switchScene(AdminDashboard.getView(), "dashboard.css"));

            Button usersBtn = new Button("Manage Users");
            usersBtn.getStyleClass().add("sidebar-btn");
            usersBtn.setMaxWidth(Double.MAX_VALUE);
            usersBtn.setOnAction(e -> SceneManager.switchScene(ManageUsersView.getView(), "dashboard.css"));

            Button ordersBtn = new Button("Manage Orders");
            ordersBtn.getStyleClass().add("sidebar-btn");
            ordersBtn.setMaxWidth(Double.MAX_VALUE);
            ordersBtn.setOnAction(e -> SceneManager.switchScene(ManageOrdersView.getView(), "dashboard.css"));

            javafx.scene.shape.Line sep2 = new javafx.scene.shape.Line(0, 0, 180, 0);
            sep2.getStyleClass().add("sidebar-separator");

            Button logoutBtn = new Button("Sign Out");
            logoutBtn.getStyleClass().add("logout-btn");
            logoutBtn.setMaxWidth(Double.MAX_VALUE);
            logoutBtn.setOnAction(e -> {
                SessionManager.getInstance().logout();
                SceneManager.switchScene(com.artmarketplace.ui.auth.LoginView.getView(), "login.css");
            });

            sidebar.getChildren().addAll(logo, userLabel, sep1, dashBtn, usersBtn, ordersBtn, sep2, logoutBtn);
            return sidebar;
        }
    }
}
