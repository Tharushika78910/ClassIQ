package Frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePage {

    public Scene getScene(Stage stage) {

        // Background image
        Image bgImage = new Image(getClass().getResourceAsStream("/Homepage.jpg"));
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setFitWidth(400);
        bgImageView.setFitHeight(300);
        bgImageView.setPreserveRatio(false);

        // Title
        Label title = new Label("Class iQ");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Subtitle
        Label subtitle = new Label("Academic Performance Platform");

        // Login button
        Button loginButton = new Button("Log in");
        loginButton.setOnAction(e -> {
            // Navigate to LoginPage
            LoginPage loginPage = new LoginPage();
            stage.setScene(loginPage.getScene(stage));
        });

        // Layout container
        VBox layout = new VBox(15); // 15px spacing
        layout.setPadding(new Insets(20)); // padding around edges
        layout.setAlignment(Pos.CENTER); // center everything
        layout.getChildren().addAll(title, subtitle, loginButton);

        // StackPane to place background behind content
        StackPane root = new StackPane();
        bgImageView.fitWidthProperty().bind(root.widthProperty());
        bgImageView.fitHeightProperty().bind(root.heightProperty());
        root.getChildren().addAll(bgImageView, layout);

        return new Scene(root, 400, 300); // width, height
    }
}