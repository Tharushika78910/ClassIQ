package Frontend;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HomePage {

    public Scene getScene(Stage stage) {

        // ================= ROOT LAYOUT =================
        StackPane root = new StackPane();
        BorderPane mainLayout = new BorderPane();

        // Background
        root.getChildren().add(createBackground());
        root.getChildren().add(mainLayout);

        // Top Section
        mainLayout.setTop(createTopBar(stage));

        // Center Section
        mainLayout.setCenter(createCenterPanel());

        // Bottom Section
        mainLayout.setBottom(createBottomSection(stage));

        return new Scene(root, 900, 600);
    }

    // ================= BACKGROUND =================
    private Region createBackground() {
        Region background = new Region();

        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.rgb(200, 220, 200)),
                new Stop(0.5, Color.rgb(180, 200, 180)),
                new Stop(1.0, Color.rgb(240, 250, 240))
        );

        background.setBackground(
                new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY))
        );

        return background;
    }

    // ================= TOP BAR =================
    private HBox createTopBar(Stage stage) {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER_LEFT);

        LinearGradient topGradient = new LinearGradient(
                0, 0, 1, 0,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.rgb(162, 184, 172)),
                new Stop(1.0, Color.rgb(180, 200, 180))
        );
        topBar.setBackground(new Background(new BackgroundFill(topGradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // ================= "FHS" LABEL =================
        Label fhsLabel = new Label("FHS");
        fhsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        fhsLabel.setTextFill(Color.BLACK);

        // Spacer for Login button alignment
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ================= LOGIN BUTTON =================
        Button loginBtn = new Button("Login");
        loginBtn.setStyle(
                "-fx-background-color: #D3D3D3;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );
        loginBtn.setPadding(new Insets(8, 25, 8, 25));
        loginBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            stage.setScene(loginPage.getScene(stage));
        });

        // Add FHS label and Login button to top bar
        topBar.getChildren().addAll(fhsLabel, spacer, loginBtn);

        return topBar;
    }

    // ================= CENTER PANEL =================
    private VBox createCenterPanel() {
        VBox centerPanel = new VBox(10);
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setPadding(new Insets(20));

        VBox overlay = new VBox(8);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(20, 40, 20, 40));
        overlay.setMaxWidth(650);

        overlay.setBackground(
                new Background(new BackgroundFill(
                        Color.rgb(85, 107, 47, 0.85),
                        new CornerRadii(12),
                        Insets.EMPTY))
        );

        Label platform = new Label("Ferguson High School");
        platform.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        platform.setTextFill(Color.BLACK);

        Label slogan = new Label("\"Academic Performance Platform.\"");
        slogan.setFont(Font.font("Arial", FontPosture.ITALIC, 15));
        slogan.setTextFill(Color.rgb(162, 184, 172));

        overlay.getChildren().addAll(platform, slogan);
        centerPanel.getChildren().add(overlay);

        return centerPanel;
    }

    // ================= BOTTOM SECTION =================
    private VBox createBottomSection(Stage stage) {
        VBox bottomContainer = new VBox();

        // Feature Cards
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(20));

        VBox gradebook = createFeatureCard("📊", "Gradebook", "Track progress in real-time");
        VBox insights = createFeatureCard("📈", "Insights", "AI-powered analytics");
        VBox reports = createFeatureCard("📋", "Reports", "Comprehensive dashboards");
        VBox records = createFeatureCard("🔒", "Records", "Secure data management");

        navBar.getChildren().addAll(gradebook, insights, reports, records);

        // Footer
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setBackground(
                new Background(new BackgroundFill(
                        Color.rgb(220, 220, 220, 0.9),
                        CornerRadii.EMPTY,
                        Insets.EMPTY))
        );

        Label copyright =
                new Label("© 2026 | Version 1.0");

        Hyperlink privacy = new Hyperlink("Privacy");
        Hyperlink help = new Hyperlink("Help");

        privacy.setOnAction(e -> {
            PrivacyPage privacyPage = new PrivacyPage();
            stage.setScene(privacyPage.getScene(stage));
        });

        help.setOnAction(e -> {
            HelpPage helpPage = new HelpPage();
            stage.setScene(helpPage.getScene(stage));
        });

        footer.getChildren().addAll(copyright, privacy, help);
        bottomContainer.getChildren().addAll(navBar, footer);

        return bottomContainer;
    }

    // ================= FEATURE CARD =================
    private VBox createFeatureCard(String emoji, String title, String description) {
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12));
        card.setPrefWidth(150);
        card.setPrefHeight(100);

        card.setBackground(
                new Background(new BackgroundFill(
                        Color.WHITE,
                        new CornerRadii(10),
                        Insets.EMPTY))
        );

        card.setBorder(new Border(new BorderStroke(
                Color.rgb(162, 184, 172),
                BorderStrokeStyle.SOLID,
                new CornerRadii(10),
                new BorderWidths(1))));

        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font(22));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        titleLabel.setTextFill(Color.rgb(85, 107, 47));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font(11));
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(130);
        descLabel.setAlignment(Pos.CENTER);

        card.getChildren().addAll(emojiLabel, titleLabel, descLabel);

        return card;
    }

    // ================= DIALOG =================
    private void showDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
