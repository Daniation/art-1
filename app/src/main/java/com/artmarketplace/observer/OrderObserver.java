package com.artmarketplace.observer;

public class OrderObserver implements Observer {
    private String name;

    public OrderObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println("[Notification for " + name + "] " + message);
    }

    public String getName() {
        return name;
    }
}
