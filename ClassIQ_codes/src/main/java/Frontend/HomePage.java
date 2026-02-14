package Frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class HomePage {

    private static final String SAGE_BUTTON = "#9AC4B7";
    private static final String SAGE_BUTTON_HOVER = "#8AB5A8";
    private static final String GOLD_STAR = "#D4A84B";
    private static final String GOLD_STAR_HOVER = "#FFD700";

    public Scene getScene(Stage stage) {

        StackPane root = new StackPane();

        // Background image
        ImageView background = new ImageView(
                new Image(getClass().getResource("/Homepage.png").toExternalForm())
        );
        background.setPreserveRatio(false);
        background.setSmooth(true);
        background.fitWidthProperty().bind(stage.widthProperty());
        background.fitHeightProperty().bind(stage.heightProperty());

        Pane content = createCenterContent(stage);

        root.getChildren().addAll(background, content);

        return new Scene(root, 1000, 650);
    }

    private Pane createCenterContent(Stage stage) {

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(40));

        //vglassmorphism panel
        VBox glassPanel = new VBox(20);
        glassPanel.setAlignment(Pos.CENTER);
        glassPanel.setPadding(new Insets(40));
        glassPanel.setMaxWidth(450);
        glassPanel.setBackground(new Background(
                new BackgroundFill(Color.rgb(255, 255, 255, 0.18), new CornerRadii(20), Insets.EMPTY)
        ));
        glassPanel.setBorder(new Border(new BorderStroke(
                Color.rgb(255, 255, 255, 0.35),
                BorderStrokeStyle.SOLID,
                new CornerRadii(20),
                new BorderWidths(1.5)
        )));

        // logo
        try {
            ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
            logo.setPreserveRatio(true);
            logo.setSmooth(true);
            logo.fitWidthProperty().bind(stage.widthProperty().multiply(0.35));
            glassPanel.getChildren().add(logo);
        } catch (Exception ignored) {}

        //login button
        Button loginBtn = new Button("Login");
        loginBtn.setStyle(buttonStyle(SAGE_BUTTON));
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(buttonStyle(SAGE_BUTTON_HOVER)));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(buttonStyle(SAGE_BUTTON)));
        loginBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            stage.setScene(loginPage.getScene(stage));
        });
        glassPanel.getChildren().add(loginBtn);

        // stars
        HBox stars = new HBox(5);
        stars.setAlignment(Pos.CENTER);
        for (int i = 0; i < 5; i++) {
            Label star = new Label("★");
            star.setFont(Font.font(26));
            star.setTextFill(Color.web(GOLD_STAR));
            star.setOnMouseEntered(e -> star.setTextFill(Color.web(GOLD_STAR_HOVER)));
            star.setOnMouseExited(e -> star.setTextFill(Color.web(GOLD_STAR)));
            stars.getChildren().add(star);
        }
        glassPanel.getChildren().add(stars);

        // footer with copyright and links
        HBox footer = new HBox(8);
        footer.setAlignment(Pos.CENTER);

        Label copyright = new Label("© 2026 | Version 1.0 | ");
        copyright.setFont(Font.font("Segoe UI", 12));
        copyright.setTextFill(Color.BLACK);

        Hyperlink privacy = new Hyperlink("Privacy Policy");
        Hyperlink help = new Hyperlink("Help");
        privacy.setStyle("-fx-text-fill: green; -fx-underline: false;");
        help.setStyle("-fx-text-fill: green; -fx-underline: false;");

        privacy.setOnAction(e -> openWebPage(stage, "/privacy.html"));

        help.setOnAction(e -> openWebPage(stage, "/help.html"));

        footer.getChildren().addAll(copyright, privacy, help);

        layout.setCenter(glassPanel);
        layout.setBottom(footer);

        return layout;
    }

    // webview page loader with the login panel style and a back button
    private void openWebPage(Stage stage, String resourcePath) {

        WebView webView = new WebView();
        webView.getEngine().load(getClass().getResource(resourcePath).toExternalForm());
        webView.setStyle("-fx-background-color: transparent;");
        webView.setPrefHeight(400);
        webView.setPrefWidth(600);

        // glassmorphism panel like loign page but with webview inside
        VBox glassPanel = new VBox(webView);
        glassPanel.setPadding(new Insets(30));
        glassPanel.setAlignment(Pos.CENTER);
        glassPanel.setMaxWidth(650);
        glassPanel.setMaxHeight(500);
        glassPanel.setBackground(new Background(
                new BackgroundFill(Color.rgb(255, 255, 255, 0.18), new CornerRadii(20), Insets.EMPTY)
        ));
        glassPanel.setBorder(new Border(new BorderStroke(
                Color.rgb(255, 255, 255, 0.35),
                BorderStrokeStyle.SOLID,
                new CornerRadii(20),
                new BorderWidths(1.5)
        )));

        // button back
        Button backBtn = new Button("Back");
        backBtn.setOnAction(ev -> stage.setScene(getScene(stage)));
        backBtn.setStyle("-fx-background-color: #9AC4B7; -fx-text-fill: white; -fx-font-weight: bold;");
        backBtn.setPadding(new Insets(10));

        HBox topBar = new HBox(backBtn);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        BorderPane webLayout = new BorderPane();
        webLayout.setTop(topBar);
        webLayout.setCenter(glassPanel);

        // background image
        ImageView background = new ImageView(
                new Image(getClass().getResource("/Homepage.png").toExternalForm())
        );
        background.setPreserveRatio(false);
        background.fitWidthProperty().bind(stage.widthProperty());
        background.fitHeightProperty().bind(stage.heightProperty());
        background.setSmooth(true);

        StackPane root = new StackPane(background, webLayout);

        Scene webScene = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(webScene);
    }

    // button style generator to avoid code repetition for the login button and the back button
    private String buttonStyle(String color) {
        return "-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 16px;" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 12 60 12 60;";
    }
}
