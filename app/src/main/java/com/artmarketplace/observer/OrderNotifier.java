package com.artmarketplace.observer;

import java.util.ArrayList;
import java.util.List;

public class OrderNotifier implements Subject {
    private List<Observer> observers;
    private static OrderNotifier instance;

    public OrderNotifier() {
        this.observers = new ArrayList<>();
    }

    public static synchronized OrderNotifier getInstance() {
        if (instance == null) {
            instance = new OrderNotifier();
        }
        return instance;
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
