package com.artmarketplace.observer;

import com.artmarketplace.utils.AlertUtil;
import javafx.application.Platform;
import java.util.Objects;

public class OrderObserver implements Observer {
    private String name;

    public OrderObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println("[Notification for " + name + "] " + message);
        Platform.runLater(() -> AlertUtil.showInfo("Order Notification", message));
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderObserver that = (OrderObserver) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
