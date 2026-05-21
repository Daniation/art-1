package com.artmarketplace.ui.buyer;

import com.artmarketplace.dao.CartDAO;
import com.artmarketplace.model.Cart;
import com.artmarketplace.service.OrderService;
import com.artmarketplace.utils.AlertUtil;
import com.artmarketplace.utils.SceneManager;
import com.artmarketplace.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.List;

public class CartView {

    public static BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        VBox sidebar = BrowseArtworksView.BuyerDashboard_Helper.createSidebar();
        root.setLeft(sidebar);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));

        Text pageTitle = new Text("My Cart");
        pageTitle.getStyleClass().add("page-title");

        VBox cartItems = new VBox(15);
        ScrollPane scrollPane = new ScrollPane(cartItems);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        HBox bottomBar = new HBox(20);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(15, 0, 0, 0));

        Text totalText = new Text("Total: $0.00");
        totalText.getStyleClass().add("total-text");

        Button placeOrderBtn = new Button("Place Order");
        placeOrderBtn.getStyleClass().add("primary-btn");

        Button clearCartBtn = new Button("Clear Cart");
        clearCartBtn.getStyleClass().add("action-btn");

        bottomBar.getChildren().addAll(clearCartBtn, totalText, placeOrderBtn);

        mainContent.getChildren().addAll(pageTitle, scrollPane, bottomBar);
        root.setCenter(mainContent);

        CartDAO cartDAO = new CartDAO();
        OrderService orderService = new OrderService();
        int buyerId = SessionManager.getInstance().getCurrentUser().getId();

        Runnable loadCart = new Runnable() {
            @Override
            public void run() {
                cartItems.getChildren().clear();
                List<Cart> items = cartDAO.getCartItems(buyerId);

                if (items.isEmpty()) {
                    Text emptyText = new Text("Your cart is empty. Browse artworks to add items.");
                    emptyText.getStyleClass().add("empty-text");
                    cartItems.getChildren().add(emptyText);
                    totalText.setText("Total: $0.00");
                    placeOrderBtn.setDisable(true);
                    return;
                }

                placeOrderBtn.setDisable(false);
                double total = 0;

                for (Cart item : items) {
                    total += item.getPrice();
                    HBox itemRow = new HBox(15);
                    itemRow.setAlignment(Pos.CENTER_LEFT);
                    itemRow.getStyleClass().add("cart-item");
                    itemRow.setPadding(new Insets(15));

                    VBox info = new VBox(5);
                    Text titleText = new Text(item.getArtworkTitle());
                    titleText.getStyleClass().add("artwork-title");
                    Text priceText = new Text("$" + String.format("%.2f", item.getPrice()));
                    priceText.getStyleClass().add("artwork-price");

                    info.getChildren().addAll(titleText, priceText);
                    HBox.setHgrow(info, Priority.ALWAYS);

                    Button removeBtn = new Button("Remove");
                    removeBtn.getStyleClass().add("remove-btn");
                    removeBtn.setOnAction(e -> {
                        if (AlertUtil.showConfirm("Remove", "Remove this item from cart?")) {
                            cartDAO.removeFromCart(item.getId());
                            this.run();
                        }
                    });

                    itemRow.getChildren().addAll(info, removeBtn);
                    cartItems.getChildren().add(itemRow);
                }

                totalText.setText("Total: $" + String.format("%.2f", total));
            }
        };

        clearCartBtn.setOnAction(e -> {
            if (AlertUtil.showConfirm("Clear Cart", "Clear all items from cart?")) {
                cartDAO.clearCart(buyerId);
                loadCart.run();
            }
        });

        placeOrderBtn.setOnAction(e -> {
            if (AlertUtil.showConfirm("Place Order", "Are you sure you want to place this order?")) {
                int orderId = orderService.placeOrder(buyerId);
                if (orderId > 0) {
                    AlertUtil.showInfo("Order Placed", "Order #" + orderId + " placed successfully!");
                    loadCart.run();
                } else {
                    AlertUtil.showError("Error", "Failed to place order!");
                }
            }
        });

        loadCart.run();

        return root;
    }
}
