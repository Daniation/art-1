package com.artmarketplace;

import com.artmarketplace.ui.auth.LoginView;
import com.artmarketplace.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setPrimaryStage(primaryStage);
        SceneManager.switchScene(LoginView.getView(), "login.css");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
