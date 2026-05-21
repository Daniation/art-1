package com.artmarketplace.ui.seller;

import com.artmarketplace.model.Artwork;
import com.artmarketplace.service.ArtworkService;
import com.artmarketplace.utils.AlertUtil;
import com.artmarketplace.utils.SceneManager;
import com.artmarketplace.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class EditArtworkView {

    public static BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        VBox sidebar = AddArtworkView.SellerDashboard_Sidebar.createSidebar();
        root.setLeft(sidebar);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));

        Text pageTitle = new Text("My Artworks");
        pageTitle.getStyleClass().add("page-title");

        VBox artworksList = new VBox(15);
        ScrollPane scrollPane = new ScrollPane(artworksList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        mainContent.getChildren().addAll(pageTitle, scrollPane);
        root.setCenter(mainContent);

        ArtworkService artworkService = new ArtworkService();
        int sellerId = SessionManager.getInstance().getCurrentUser().getId();

        List<Artwork> artworks = artworkService.getArtworksBySeller(sellerId);

        if (artworks.isEmpty()) {
            Text emptyText = new Text("You haven't added any artworks yet.");
            emptyText.getStyleClass().add("empty-text");
            artworksList.getChildren().add(emptyText);
        } else {
            for (Artwork artwork : artworks) {
                VBox card = new VBox(10);
                card.getStyleClass().add("artwork-card");
                card.setPadding(new Insets(20));

                HBox content = new HBox(20);
                content.setAlignment(Pos.CENTER_LEFT);

                StackPane imgBox = new StackPane();
                imgBox.setMinSize(100, 100);
                imgBox.setMaxSize(100, 100);
                imgBox.setStyle("-fx-background-color: #0d1117; -fx-border-color: #30363d; -fx-border-radius: 10; -fx-background-radius: 10;");

                ImageView imgView = new ImageView();
                imgView.setFitWidth(100);
                imgView.setFitHeight(100);
                imgView.setPreserveRatio(true);

                boolean hasImg = false;
                try {
                    String path = artwork.getImagePath();
                    if (path != null && !path.isEmpty()) {
                        File imgFile = new File(path);
                        if (imgFile.exists()) {
                            imgView.setImage(new Image(imgFile.toURI().toString()));
                            hasImg = true;
                        }
                    }
                } catch (Exception e) {
                    hasImg = false;
                }
                if (!hasImg) {
                    Text noImg = new Text("No Img");
                    noImg.setStyle("-fx-fill: #484f58; -fx-font-size: 12px;");
                    imgBox.getChildren().add(noImg);
                }
                imgBox.getChildren().add(imgView);

                VBox infoBox = new VBox(5);
                HBox.setHgrow(infoBox, Priority.ALWAYS);

                Text titleText = new Text(artwork.getTitle());
                titleText.getStyleClass().add("artwork-title");

                Text descText = new Text(artwork.getDescription() != null ? artwork.getDescription() : "");
                descText.getStyleClass().add("artwork-desc");
                descText.setWrappingWidth(250);

                Text priceText = new Text("$" + String.format("%.2f", artwork.getPrice()) + " | " + artwork.getStatus());
                priceText.getStyleClass().add("artwork-price");

                infoBox.getChildren().addAll(titleText, descText, priceText);

                Button editBtn = new Button("Edit");
                editBtn.getStyleClass().add("action-btn");

                Button deleteBtn = new Button("Delete");
                deleteBtn.getStyleClass().add("remove-btn");

                content.getChildren().addAll(imgBox, infoBox, editBtn, deleteBtn);
                card.getChildren().add(content);

                int artId = artwork.getId();

                editBtn.setOnAction(e -> showEditDialog(artwork, () -> {
                    SceneManager.switchScene(EditArtworkView.getView(), "dashboard.css");
                }));

                deleteBtn.setOnAction(e -> {
                    if (AlertUtil.showConfirm("Delete", "Delete \"" + artwork.getTitle() + "\"?")) {
                        if (artworkService.deleteArtwork(artId, sellerId)) {
                            AlertUtil.showInfo("Deleted", "Artwork deleted!");
                            SceneManager.switchScene(EditArtworkView.getView(), "dashboard.css");
                        } else {
                            AlertUtil.showError("Error", "Failed to delete artwork!");
                        }
                    }
                });

                artworksList.getChildren().add(card);
            }
        }

        return root;
    }

    private static void showEditDialog(Artwork artwork, Runnable onSave) {
        Stage dialog = new Stage();
        dialog.setTitle("Edit Artwork");
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        VBox dialogContent = new VBox(15);
        dialogContent.setPadding(new Insets(20));
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setStyle("-fx-background-color: #161b22;");

        Label titleLabel = new Label("Title");
        titleLabel.getStyleClass().add("form-label");
        TextField titleField = new TextField(artwork.getTitle());
        titleField.getStyleClass().add("form-field");
        titleField.setPromptText("Title");

        Label descLabel = new Label("Description");
        descLabel.getStyleClass().add("form-label");
        TextArea descArea = new TextArea(artwork.getDescription());
        descArea.getStyleClass().add("form-textarea");
        descArea.setPromptText("Description");
        descArea.setPrefRowCount(3);

        Label priceLabel = new Label("Price ($)");
        priceLabel.getStyleClass().add("form-label");
        TextField priceField = new TextField(String.valueOf(artwork.getPrice()));
        priceField.getStyleClass().add("form-field");
        priceField.setPromptText("Price");

        Label imgLabel = new Label("Image (optional)");
        imgLabel.getStyleClass().add("form-label");

        HBox imgBox = new HBox(10);
        imgBox.setAlignment(Pos.CENTER_LEFT);

        TextField imgField = new TextField();
        imgField.setEditable(false);
        imgField.getStyleClass().add("form-field");
        imgField.setPromptText("Current: " + new File(artwork.getImagePath()).getName());
        HBox.setHgrow(imgField, Priority.ALWAYS);

        Button browseBtn = new Button("Change");
        browseBtn.getStyleClass().add("action-btn");

        StackPane previewContainer = new StackPane();
        previewContainer.setMinSize(220, 200);
        previewContainer.setMaxSize(220, 200);
        previewContainer.setStyle("-fx-background-color: #0d1117; -fx-border-color: #30363d; -fx-border-radius: 10; -fx-background-radius: 10;");

        ImageView preview = new ImageView();
        preview.setFitWidth(220);
        preview.setFitHeight(200);
        preview.setPreserveRatio(true);

        boolean hasPreviewImg = false;
        try {
            String path = artwork.getImagePath();
            if (path != null && !path.isEmpty()) {
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    preview.setImage(new Image(imgFile.toURI().toString()));
                    hasPreviewImg = true;
                }
            }
        } catch (Exception e) {
            hasPreviewImg = false;
        }
        if (!hasPreviewImg) {
            Text noPrev = new Text("No image");
            noPrev.setStyle("-fx-fill: #484f58; -fx-font-size: 14px;");
            previewContainer.getChildren().add(noPrev);
        }
        previewContainer.getChildren().add(preview);

        imgBox.getChildren().addAll(imgField, browseBtn);

        Button saveBtn = new Button("Save Changes");
        saveBtn.getStyleClass().add("primary-btn");

        dialogContent.getChildren().addAll(
            titleLabel, titleField,
            descLabel, descArea,
            priceLabel, priceField,
            imgLabel, imgBox, previewContainer,
            saveBtn
        );

        Scene scene = new Scene(dialogContent, 450, 550);
        scene.getStylesheets().add(
            SceneManager.class.getResource("/css/dashboard.css").toExternalForm()
        );
        dialog.setScene(scene);

        browseBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select Image");
            fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
            );
            File selected = fc.showOpenDialog(dialog);
            if (selected != null) {
                try {
                    String ext = selected.getName().substring(selected.getName().lastIndexOf('.'));
                    String destName = System.currentTimeMillis() + ext;
                    File dest = new File("artworks", destName);
                    if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();
                    Files.copy(selected.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    artwork.setImagePath("artworks/" + destName);
                    imgField.setText(selected.getName());
                    Image img = new Image(dest.toURI().toString());
                    preview.setImage(img);
                    previewContainer.getChildren().clear();
                    previewContainer.getChildren().add(preview);
                } catch (IOException ex) {
                    AlertUtil.showError("Error", "Failed to copy image!");
                }
            }
        });

        saveBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String priceStr = priceField.getText().trim();

            if (title.isEmpty() || priceStr.isEmpty()) {
                AlertUtil.showError("Error", "Title and price are required!");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    AlertUtil.showError("Error", "Price must be greater than 0!");
                    return;
                }

                artwork.setTitle(title);
                artwork.setDescription(descArea.getText().trim());
                artwork.setPrice(price);

                ArtworkService service = new ArtworkService();
                if (service.updateArtwork(artwork)) {
                    AlertUtil.showInfo("Success", "Artwork updated!");
                    dialog.close();
                    onSave.run();
                } else {
                    AlertUtil.showError("Error", "Failed to update artwork!");
                }
            } catch (NumberFormatException ex) {
                AlertUtil.showError("Error", "Invalid price!");
            }
        });

        dialog.showAndWait();
    }
}
