package com.artmarketplace.ui.buyer;

import com.artmarketplace.model.Order;
import com.artmarketplace.model.OrderItem;
import com.artmarketplace.service.OrderService;
import com.artmarketplace.utils.SceneManager;
import com.artmarketplace.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.List;

public class OrdersView {

    public static BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        VBox sidebar = BrowseArtworksView.BuyerDashboard_Helper.createSidebar();
        root.setLeft(sidebar);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));

        Text pageTitle = new Text("My Orders");
        pageTitle.getStyleClass().add("page-title");

        VBox ordersList = new VBox(15);
        ScrollPane scrollPane = new ScrollPane(ordersList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        mainContent.getChildren().addAll(pageTitle, scrollPane);
        root.setCenter(mainContent);

        OrderService orderService = new OrderService();
        int buyerId = SessionManager.getInstance().getCurrentUser().getId();

        List<Order> orders = orderService.getOrdersByBuyer(buyerId);

        if (orders.isEmpty()) {
            Text emptyText = new Text("No orders yet. Start shopping!");
            emptyText.getStyleClass().add("empty-text");
            ordersList.getChildren().add(emptyText);
        } else {
            for (Order order : orders) {
                VBox orderCard = new VBox(10);
                orderCard.getStyleClass().add("order-card");
                orderCard.setPadding(new Insets(20));

                HBox header = new HBox(20);
                header.setAlignment(Pos.CENTER_LEFT);

                Text orderIdText = new Text("Order #" + order.getId());
                orderIdText.getStyleClass().add("order-id");

                Text dateText = new Text(order.getOrderDate().toString().replace("T", " "));
                dateText.getStyleClass().add("order-date");

                Text statusText = new Text(order.getStatus());
                statusText.getStyleClass().add("status-" + order.getStatus().toLowerCase());

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Text totalText = new Text("$" + String.format("%.2f", order.getTotalAmount()));
                totalText.getStyleClass().add("order-total");

                header.getChildren().addAll(orderIdText, dateText, statusText, spacer, totalText);

                VBox itemsBox = new VBox(5);
                itemsBox.setPadding(new Insets(10, 0, 0, 20));
                if (order.getItems() != null) {
                    for (OrderItem item : order.getItems()) {
                        Text itemText = new Text(item.getArtworkTitle() + " - $" + String.format("%.2f", item.getPrice()));
                        itemText.getStyleClass().add("order-item-text");
                        itemsBox.getChildren().add(itemText);
                    }
                }

                orderCard.getChildren().addAll(header, itemsBox);
                ordersList.getChildren().add(orderCard);
            }
        }

        return root;
    }
}
