package com.artmarketplace.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    private static Scene mainScene;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(Parent root, String cssPath) {
        if (mainScene == null) {
            mainScene = new Scene(root, 1200, 800);
            if (cssPath != null && !cssPath.isEmpty()) {
                mainScene.getStylesheets().add(
                    SceneManager.class.getResource("/css/" + cssPath).toExternalForm()
                );
            }
            primaryStage.setScene(mainScene);
        } else {
            mainScene.setRoot(root);
            mainScene.getStylesheets().clear();
            if (cssPath != null && !cssPath.isEmpty()) {
                mainScene.getStylesheets().add(
                    SceneManager.class.getResource("/css/" + cssPath).toExternalForm()
                );
            }
        }
        primaryStage.setTitle("ART Marketplace");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
