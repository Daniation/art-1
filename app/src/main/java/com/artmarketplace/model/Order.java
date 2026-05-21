package com.artmarketplace.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private int buyerId;
    private String buyerName;
    private LocalDateTime orderDate;
    private String status;
    private double totalAmount;
    private List<OrderItem> items;

    public Order() {}

    public Order(int id, int buyerId, LocalDateTime orderDate, String status, double totalAmount) {
        this.id = id;
        this.buyerId = buyerId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBuyerId() { return buyerId; }
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
