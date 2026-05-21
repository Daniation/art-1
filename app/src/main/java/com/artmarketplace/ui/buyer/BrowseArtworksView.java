package com.artmarketplace.ui.buyer;

import com.artmarketplace.dao.CartDAO;
import com.artmarketplace.model.Artwork;
import com.artmarketplace.model.Cart;
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
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class BrowseArtworksView {

    public static BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        VBox sidebar = BuyerDashboard_Browse.createSidebar();
        root.setLeft(sidebar);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));

        Text pageTitle = new Text("Browse Artworks");
        pageTitle.getStyleClass().add("page-title");

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setMaxWidth(600);

        TextField searchField = new TextField();
        searchField.setPromptText("Search artworks by title or description...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefHeight(40);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("action-btn");

        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("action-btn");

        searchBar.getChildren().addAll(searchField, searchBtn, clearBtn);

        VBox artworksList = new VBox(15);
        artworksList.setPadding(new Insets(10, 0, 0, 0));

        ScrollPane scrollPane = new ScrollPane(artworksList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        ArtworkService artworkService = new ArtworkService();

        Runnable loadArtworks = () -> {
            artworksList.getChildren().clear();
            List<Artwork> artworks;
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                artworks = artworkService.getAllAvailableArtworks();
            } else {
                artworks = artworkService.searchArtworks(keyword);
            }

            if (artworks.isEmpty()) {
                Text emptyText = new Text("No artworks found.");
                emptyText.getStyleClass().add("empty-text");
                artworksList.getChildren().add(emptyText);
                return;
            }

            for (Artwork artwork : artworks) {
                artworksList.getChildren().add(createArtworkCard(artwork));
            }
        };

        searchBtn.setOnAction(e -> loadArtworks.run());
        clearBtn.setOnAction(e -> {
            searchField.clear();
            loadArtworks.run();
        });

        mainContent.getChildren().addAll(pageTitle, searchBar, scrollPane);
        root.setCenter(mainContent);

        loadArtworks.run();

        return root;
    }

    private static VBox createArtworkCard(Artwork artwork) {
        VBox card = new VBox(10);
        card.getStyleClass().add("artwork-card");
        card.setPadding(new Insets(20));

        HBox content = new HBox(25);
        content.setAlignment(Pos.CENTER_LEFT);

        StackPane imgContainer = new StackPane();
        imgContainer.setMinSize(130, 130);
        imgContainer.setMaxSize(130, 130);
        imgContainer.setStyle("-fx-background-color: #0d1117; -fx-border-radius: 10; -fx-background-radius: 10;");

        ImageView imgView = new ImageView();
        imgView.setFitWidth(130);
        imgView.setFitHeight(130);
        imgView.setPreserveRatio(true);
        boolean imgLoaded = false;
        try {
            String path = artwork.getImagePath();
            if (path != null && !path.isEmpty()) {
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    imgView.setImage(new Image(imgFile.toURI().toString()));
                    imgLoaded = true;
                }
            }
        } catch (Exception e) {
            imgLoaded = false;
        }
        if (!imgLoaded) {
            Text noImg = new Text("No Image");
            noImg.setStyle("-fx-fill: #484f58; -fx-font-size: 13px;");
            imgContainer.getChildren().add(noImg);
        }
        imgContainer.getChildren().add(imgView);

        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Text titleText = new Text(artwork.getTitle());
        titleText.getStyleClass().add("artwork-title");

        Text descText = new Text(artwork.getDescription() != null ? artwork.getDescription() : "No description");
        descText.getStyleClass().add("artwork-desc");
        descText.setWrappingWidth(350);

        Text sellerText = new Text("By: " + artwork.getSellerName());
        sellerText.getStyleClass().add("artwork-seller");

        Text priceText = new Text("$" + String.format("%.2f", artwork.getPrice()));
        priceText.getStyleClass().add("artwork-price");

        infoBox.getChildren().addAll(titleText, descText, sellerText, priceText);

        VBox actionBox = new VBox(10);
        actionBox.setAlignment(Pos.CENTER);

        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.getStyleClass().add("primary-btn");
        addToCartBtn.setOnAction(e -> {
            CartDAO cartDAO = new CartDAO();
            int buyerId = SessionManager.getInstance().getCurrentUser().getId();
            if (cartDAO.isInCart(buyerId, artwork.getId())) {
                AlertUtil.showInfo("Already in Cart", "This artwork is already in your cart!");
            } else {
                Cart cartItem = new Cart();
                cartItem.setBuyerId(buyerId);
                cartItem.setArtworkId(artwork.getId());
                if (cartDAO.addToCart(cartItem)) {
                    AlertUtil.showInfo("Added to Cart", "\"" + artwork.getTitle() + "\" added to your cart!");
                }
            }
        });

        actionBox.getChildren().add(addToCartBtn);

        content.getChildren().addAll(imgContainer, infoBox, actionBox);
        card.getChildren().add(content);

        return card;
    }

    static class BuyerDashboard_Browse {
        static VBox createSidebar() {
            return BuyerDashboard_Helper.createSidebar();
        }
    }

    static class BuyerDashboard_Helper {
        static VBox createSidebar() {
            VBox sidebar = new VBox(10);
            sidebar.setPadding(new Insets(30, 20, 30, 20));
            sidebar.getStyleClass().add("sidebar");
            sidebar.setMinWidth(220);
            sidebar.setMaxWidth(220);

            Text logo = new Text("ART Marketplace");
            logo.getStyleClass().add("sidebar-logo");

            Label userLabel = new Label("Buyer: " + SessionManager.getInstance().getCurrentUser().getFullName());
            userLabel.getStyleClass().add("sidebar-user");

            javafx.scene.shape.Line separator1 = new javafx.scene.shape.Line(0, 0, 180, 0);
            separator1.getStyleClass().add("sidebar-separator");

            Button dashboardBtn = new Button("Dashboard");
            dashboardBtn.getStyleClass().add("sidebar-btn");
            dashboardBtn.setMaxWidth(Double.MAX_VALUE);
            dashboardBtn.setOnAction(e -> SceneManager.switchScene(BuyerDashboard.getView(), "dashboard.css"));

            Button browseBtn = new Button("Browse Artworks");
            browseBtn.getStyleClass().add("sidebar-btn");
            browseBtn.setMaxWidth(Double.MAX_VALUE);
            browseBtn.setOnAction(e -> SceneManager.switchScene(BrowseArtworksView.getView(), "dashboard.css"));

            Button cartBtn = new Button("My Cart");
            cartBtn.getStyleClass().add("sidebar-btn");
            cartBtn.setMaxWidth(Double.MAX_VALUE);
            cartBtn.setOnAction(e -> SceneManager.switchScene(CartView.getView(), "dashboard.css"));

            Button ordersBtn = new Button("My Orders");
            ordersBtn.getStyleClass().add("sidebar-btn");
            ordersBtn.setMaxWidth(Double.MAX_VALUE);
            ordersBtn.setOnAction(e -> SceneManager.switchScene(OrdersView.getView(), "dashboard.css"));

            javafx.scene.shape.Line separator2 = new javafx.scene.shape.Line(0, 0, 180, 0);
            separator2.getStyleClass().add("sidebar-separator");

            Button logoutBtn = new Button("Sign Out");
            logoutBtn.getStyleClass().add("logout-btn");
            logoutBtn.setMaxWidth(Double.MAX_VALUE);
            logoutBtn.setOnAction(e -> {
                SessionManager.getInstance().logout();
                SceneManager.switchScene(com.artmarketplace.ui.auth.LoginView.getView(), "login.css");
            });

            sidebar.getChildren().addAll(logo, userLabel, separator1, dashboardBtn, browseBtn, cartBtn, ordersBtn, separator2, logoutBtn);
            return sidebar;
        }
    }
}
