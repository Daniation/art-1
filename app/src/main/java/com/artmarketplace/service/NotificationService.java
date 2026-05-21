package com.artmarketplace.service;

import com.artmarketplace.observer.OrderNotifier;
import com.artmarketplace.observer.OrderObserver;

public class NotificationService {
    private OrderNotifier notifier;

    public NotificationService() {
        this.notifier = OrderNotifier.getInstance();
    }

    public void registerObserver(String name) {
        OrderObserver observer = new OrderObserver(name);
        notifier.attach(observer);
    }

    public void removeObserver(String name) {
        notifier.detach(new OrderObserver(name));
    }

    public void notifyAll(String message) {
        notifier.notifyObservers(message);
    }
}
