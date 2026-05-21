package com.artmarketplace.service;

import com.artmarketplace.dao.CartDAO;
import com.artmarketplace.dao.OrderDAO;
import com.artmarketplace.model.Cart;
import com.artmarketplace.model.Order;
import com.artmarketplace.model.OrderItem;
import com.artmarketplace.observer.OrderNotifier;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private OrderNotifier notifier;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.cartDAO = new CartDAO();
        this.notifier = OrderNotifier.getInstance();
    }

    public int placeOrder(int buyerId) {
        List<Cart> cartItems = cartDAO.getCartItems(buyerId);
        if (cartItems.isEmpty()) {
            return -1;
        }

        double total = cartItems.stream().mapToDouble(Cart::getPrice).sum();

        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setTotalAmount(total);

        int orderId = orderDAO.createOrder(order);
        if (orderId == -1) {
            return -1;
        }

        for (Cart item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setArtworkId(item.getArtworkId());
            orderItem.setPrice(item.getPrice());
            orderDAO.addOrderItem(orderItem);

            new ArtworkService().markAsSold(item.getArtworkId());
        }

        cartDAO.clearCart(buyerId);

        notifier.notifyObservers("New order #" + orderId + " has been placed!");

        return orderId;
    }

    public List<Order> getOrdersByBuyer(int buyerId) {
        List<Order> orders = orderDAO.getOrdersByBuyer(buyerId);
        for (Order order : orders) {
            order.setItems(orderDAO.getOrderItems(order.getId()));
        }
        return orders;
    }

    public List<Order> getOrdersBySeller(int sellerId) {
        List<Order> orders = orderDAO.getOrdersBySeller(sellerId);
        for (Order order : orders) {
            order.setItems(orderDAO.getOrderItems(order.getId()));
        }
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderDAO.getAllOrders();
        for (Order order : orders) {
            order.setItems(orderDAO.getOrderItems(order.getId()));
        }
        return orders;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        boolean updated = orderDAO.updateOrderStatus(orderId, status);
        if (updated) {
            notifier.notifyObservers("Order #" + orderId + " status changed to: " + status);
        }
        return updated;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return orderDAO.getOrderItems(orderId);
    }
}
