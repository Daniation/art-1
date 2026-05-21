package com.artmarketplace.ui.seller;

import com.artmarketplace.model.Artwork;
import com.artmarketplace.service.ArtworkService;
import com.artmarketplace.utils.AlertUtil;
import com.artmarketplace.utils.SceneManager;
import com.artmarketplace.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AddArtworkView {

    public static BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        VBox sidebar = SellerDashboard_Sidebar.createSidebar();
        root.setLeft(sidebar);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));

        Text pageTitle = new Text("Add New Artwork");
        pageTitle.getStyleClass().add("page-title");

        VBox form = new VBox(15);
        form.setMaxWidth(600);
        form.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Artwork Title *");
        titleLabel.getStyleClass().add("form-label");
        TextField titleField = new TextField();
        titleField.setPromptText("Enter artwork title");
        titleField.getStyleClass().add("form-field");

        Label descLabel = new Label("Description *");
        descLabel.getStyleClass().add("form-label");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Describe your artwork...");
        descArea.getStyleClass().add("form-textarea");
        descArea.setPrefRowCount(4);

        Label priceLabel = new Label("Price ($) *");
        priceLabel.getStyleClass().add("form-label");
        TextField priceField = new TextField();
        priceField.setPromptText("0.00");
        priceField.getStyleClass().add("form-field");

        Label imageLabel = new Label("Image *");
        imageLabel.getStyleClass().add("form-label");

        HBox imageRow = new HBox(10);
        imageRow.setAlignment(Pos.CENTER_LEFT);

        TextField imageField = new TextField();
        imageField.setPromptText("No image selected");
        imageField.getStyleClass().add("form-field");
        imageField.setEditable(false);
        HBox.setHgrow(imageField, Priority.ALWAYS);

        Button browseBtn = new Button("Browse...");
        browseBtn.getStyleClass().add("action-btn");
        imageRow.getChildren().addAll(imageField, browseBtn);

        BorderPane previewArea = new BorderPane();
        previewArea.setMinSize(250, 200);
        previewArea.setMaxSize(250, 200);
        previewArea.setStyle("-fx-background-color: #0d1117; -fx-border-color: #30363d; -fx-border-radius: 12; -fx-background-radius: 12;");

        Text noImgText = new Text("No image selected");
        noImgText.setStyle("-fx-fill: #484f58; -fx-font-size: 14px;");
        BorderPane.setAlignment(noImgText, Pos.CENTER);

        ImageView previewImg = new ImageView();
        previewImg.setFitWidth(250);
        previewImg.setFitHeight(200);
        previewImg.setPreserveRatio(true);

        previewArea.setCenter(noImgText);

        browseBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select Artwork Image");
            fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            File selected = fc.showOpenDialog(SceneManager.getPrimaryStage());
            if (selected != null) {
                try {
                    String name = selected.getName();
                    String ext = name.contains(".") ? name.substring(name.lastIndexOf('.')) : ".jpg";
                    String destName = System.currentTimeMillis() + "_" + name;
                    File destDir = new File("artworks");
                    if (!destDir.exists()) destDir.mkdirs();
                    File dest = new File(destDir, destName);
                    Files.copy(selected.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    imageField.setText(name);
                    imageField.setUserData("artworks/" + destName);
                    Image img = new Image(dest.toURI().toString());
                    previewImg.setImage(img);
                    previewArea.setCenter(previewImg);
                } catch (IOException ex) {
                    AlertUtil.showError("Error", "Failed to copy image file!");
                }
            }
        });

        Button submitBtn = new Button("Add Artwork");
        submitBtn.getStyleClass().add("primary-btn");
        submitBtn.setMaxWidth(200);

        form.getChildren().addAll(
            titleLabel, titleField,
            descLabel, descArea,
            priceLabel, priceField,
            imageLabel, imageRow, previewArea,
            submitBtn
        );

        ScrollPane scrollPane = new ScrollPane(form);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setBorder(null);

        mainContent.getChildren().addAll(pageTitle, scrollPane);
        root.setCenter(mainContent);

        submitBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String desc = descArea.getText().trim();
            String priceStr = priceField.getText().trim();
            String imgPath = imageField.getUserData() != null ? (String) imageField.getUserData() : "";

            if (title.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || imgPath.isEmpty()) {
                AlertUtil.showError("Error", "All fields are required! Fill title, description, price and select an image.");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    AlertUtil.showError("Error", "Price must be greater than 0!");
                    return;
                }
            } catch (NumberFormatException ex) {
                AlertUtil.showError("Error", "Invalid price format!");
                return;
            }

            Artwork artwork = new Artwork();
            artwork.setTitle(title);
            artwork.setDescription(desc);
            artwork.setPrice(price);
            artwork.setImagePath(imgPath);
            artwork.setSellerId(SessionManager.getInstance().getCurrentUser().getId());

            ArtworkService service = new ArtworkService();
            if (service.addArtwork(artwork)) {
                AlertUtil.showInfo("Success", "Artwork added successfully!");
                SceneManager.switchScene(SellerDashboard.getView(), "dashboard.css");
            } else {
                AlertUtil.showError("Error", "Failed to add artwork!");
            }
        });

        return root;
    }

    static class SellerDashboard_Sidebar {
        static VBox createSidebar() {
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

            Button dashBtn = new Button("Dashboard");
            dashBtn.getStyleClass().add("sidebar-btn");
            dashBtn.setMaxWidth(Double.MAX_VALUE);
            dashBtn.setOnAction(e -> SceneManager.switchScene(SellerDashboard.getView(), "dashboard.css"));

            Button addBtn = new Button("Add Artwork");
            addBtn.getStyleClass().add("sidebar-btn");
            addBtn.setMaxWidth(Double.MAX_VALUE);
            addBtn.setOnAction(e -> SceneManager.switchScene(AddArtworkView.getView(), "dashboard.css"));

            Button editBtn = new Button("My Artworks");
            editBtn.getStyleClass().add("sidebar-btn");
            editBtn.setMaxWidth(Double.MAX_VALUE);
            editBtn.setOnAction(e -> SceneManager.switchScene(EditArtworkView.getView(), "dashboard.css"));

            Button ordersBtn = new Button("Orders");
            ordersBtn.getStyleClass().add("sidebar-btn");
            ordersBtn.setMaxWidth(Double.MAX_VALUE);
            ordersBtn.setOnAction(e -> SceneManager.switchScene(SellerOrdersView.getView(), "dashboard.css"));

            javafx.scene.shape.Line sep2 = new javafx.scene.shape.Line(0, 0, 180, 0);
            sep2.getStyleClass().add("sidebar-separator");

            Button logoutBtn = new Button("Sign Out");
            logoutBtn.getStyleClass().add("logout-btn");
            logoutBtn.setMaxWidth(Double.MAX_VALUE);
            logoutBtn.setOnAction(e -> {
                SessionManager.getInstance().logout();
                SceneManager.switchScene(com.artmarketplace.ui.auth.LoginView.getView(), "login.css");
            });

            sidebar.getChildren().addAll(logo, userLabel, sep1, dashBtn, addBtn, editBtn, ordersBtn, sep2, logoutBtn);
            return sidebar;
        }
    }
}
