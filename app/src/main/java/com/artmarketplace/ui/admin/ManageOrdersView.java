package com.artmarketplace.ui.admin;

import com.artmarketplace.model.Order;
import com.artmarketplace.model.OrderItem;
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

public class ManageOrdersView {

    public static BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        VBox sidebar = ManageUsersView.AdminDashboard_Sidebar.createSidebar();
        root.setLeft(sidebar);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));

        Text pageTitle = new Text("Manage All Orders");
        pageTitle.getStyleClass().add("page-title");

        VBox ordersList = new VBox(15);
        ScrollPane scrollPane = new ScrollPane(ordersList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        mainContent.getChildren().addAll(pageTitle, scrollPane);
        root.setCenter(mainContent);

        OrderService orderService = new OrderService();
        List<Order> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            Text emptyText = new Text("No orders in the system.");
            emptyText.getStyleClass().add("empty-text");
            ordersList.getChildren().add(emptyText);
        } else {
            for (Order order : orders) {
                VBox orderCard = new VBox(10);
                orderCard.getStyleClass().add("order-card");
                orderCard.setPadding(new Insets(20));

                HBox header = new HBox(15);
                header.setAlignment(Pos.CENTER_LEFT);

                Text orderIdText = new Text("Order #" + order.getId());
                orderIdText.getStyleClass().add("order-id");

                Text buyerText = new Text("Buyer: " + order.getBuyerName());
                buyerText.getStyleClass().add("order-date");

                Text dateText = new Text(order.getOrderDate().toString().replace("T", " "));
                dateText.getStyleClass().add("order-date");

                Text statusText = new Text(order.getStatus());
                statusText.getStyleClass().add("status-" + order.getStatus().toLowerCase());

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Text totalText = new Text("$" + String.format("%.2f", order.getTotalAmount()));
                totalText.getStyleClass().add("order-total");

                header.getChildren().addAll(orderIdText, buyerText, dateText, statusText, spacer, totalText);

                VBox itemsBox = new VBox(5);
                itemsBox.setPadding(new Insets(10, 0, 10, 20));
                if (order.getItems() != null) {
                    for (OrderItem item : order.getItems()) {
                        Text itemText = new Text(item.getArtworkTitle() + " - $" + String.format("%.2f", item.getPrice()));
                        itemText.getStyleClass().add("order-item-text");
                        itemsBox.getChildren().add(itemText);
                    }
                }

                HBox actions = new HBox(10);
                actions.setPadding(new Insets(5, 0, 0, 0));
                String currentStatus = order.getStatus();

                if ("Pending".equals(currentStatus)) {
                    Button shipBtn = new Button("Mark as Shipped");
                    shipBtn.getStyleClass().add("primary-btn");
                    shipBtn.setOnAction(e -> updateStatus(orderService, order.getId(), "Shipped"));
                    actions.getChildren().add(shipBtn);

                    Button cancelBtn = new Button("Cancel Order");
                    cancelBtn.getStyleClass().add("remove-btn");
                    cancelBtn.setOnAction(e -> updateStatus(orderService, order.getId(), "Cancelled"));
                    actions.getChildren().add(cancelBtn);
                }

                if ("Shipped".equals(currentStatus)) {
                    Button deliverBtn = new Button("Mark as Delivered");
                    deliverBtn.getStyleClass().add("primary-btn");
                    deliverBtn.setOnAction(e -> updateStatus(orderService, order.getId(), "Delivered"));
                    actions.getChildren().add(deliverBtn);
                }

                if ("Delivered".equals(currentStatus) || "Cancelled".equals(currentStatus)) {
                    Text finalText = new Text("Order completed");
                    finalText.getStyleClass().add("order-date");
                    actions.getChildren().add(finalText);
                }

                orderCard.getChildren().addAll(header, itemsBox, actions);
                ordersList.getChildren().add(orderCard);
            }
        }

        return root;
    }

    private static void updateStatus(OrderService service, int orderId, String newStatus) {
        if (AlertUtil.showConfirm("Update Status", "Change order to " + newStatus + "?")) {
            if (service.updateOrderStatus(orderId, newStatus)) {
                AlertUtil.showInfo("Updated", "Order #" + orderId + " is now " + newStatus);
                SceneManager.switchScene(ManageOrdersView.getView(), "dashboard.css");
            }
        }
    }
}
