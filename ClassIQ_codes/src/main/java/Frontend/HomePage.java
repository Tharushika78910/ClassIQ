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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HomePage {

    public Scene getScene(Stage stage) {

        // Background Image
        Image bgImage = new Image(getClass().getResourceAsStream("/Homepage.jpg"));
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setPreserveRatio(false);

        // Top Section Title and Login Button
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // "Class iQ" title in top left
        Label appTitle = new Label("Class iQ");
        appTitle.setFont(Font.font("KyivType Sans", FontWeight.BOLD, FontPosture.ITALIC, 36));
        appTitle.setTextFill(Color.BLACK);

        // Spacer to push button to right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // "Log in" button in top right
        Button loginButton = new Button("Log in");
        loginButton.setStyle("-fx-background-color: #A2B8AC; -fx-text-fill: black; -fx-background-radius: 10; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 12;");
        loginButton.setPadding(new Insets(8, 20, 8, 20));
        loginButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            stage.setScene(loginPage.getScene(stage));
        });

        topBar.getChildren().addAll(appTitle, spacer, loginButton);

        // Central Overlay Panel (BorderPane center)
        // Semi-transparent dark olive green background, rounded corners
        VBox overlayPanel = new VBox(5);
        overlayPanel.setAlignment(Pos.CENTER);
        overlayPanel.setPadding(new Insets(15, 40, 15, 40));
        overlayPanel.setMaxWidth(650);  // Set maximum width to reduce panel size
        overlayPanel.setPrefWidth(200); // Set preferred width
        overlayPanel.setBackground(new Background(
                new BackgroundFill(Color.rgb(85, 107, 47, 0.85), new CornerRadii(10), Insets.EMPTY)
        ));

        // "CLASSIQ — Academic Performance Platform"
        Label platformLabel = new Label("CLASSIQ — Academic Performance Platform");
        platformLabel.setFont(Font.font("KyivType Sans", FontWeight.BOLD, 20));
        platformLabel.setTextFill(Color.BLACK);

        // "Where Academic Data Becomes Insight"
        Label sloganLabel = new Label("\"Where Academic Data Becomes Insight.\"");
        sloganLabel.setFont(Font.font("KyivType Sans", FontPosture.ITALIC, 18));
        sloganLabel.setTextFill(Color.rgb(162, 184, 172));

        overlayPanel.getChildren().addAll(platformLabel, sloganLabel);

        // Footer Navigation Bar (BorderPane bottom)
        // Light gray background
        HBox navBar = new HBox(30);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(15));
        navBar.setBackground(new Background(
                new BackgroundFill(Color.rgb(220, 220, 220, 0.9), null, Insets.EMPTY)
        ));

        // Checkmarks: ✓ Gradebook, ✓ Insights, ✓ Reports, ✓ Records
        Label gradebook = new Label("✓ Gradebook");
        Label insights = new Label("✓ Insights");
        Label reports = new Label("✓ Reports");
        Label records = new Label("✓ Records");

        gradebook.setFont(Font.font("KyivType Sans", 14));
        insights.setFont(Font.font("KyivType Sans", 14));
        reports.setFont(Font.font("KyivType Sans", 14));
        records.setFont(Font.font("KyivType Sans", 14));

        navBar.getChildren().addAll(gradebook, insights, reports, records);

        //  Copyright Bar (below navigation)
        // Light gray background
        HBox copyrightBar = new HBox(10);
        copyrightBar.setAlignment(Pos.CENTER);
        copyrightBar.setPadding(new Insets(10));
        copyrightBar.setBackground(new Background(
                new BackgroundFill(Color.rgb(220, 220, 220, 0.9), null, Insets.EMPTY)
        ));

        // "© ClassIQ Systems 2025 | Version 1.0 | Privacy | Help"
        Label copyright = new Label("© ClassIQ Systems 2025 | Version 1.0");
        copyright.setFont(Font.font("KyivType Sans", 12));

        Hyperlink privacy = new Hyperlink("Privacy");
        privacy.setFont(Font.font("KyivType Sans", 12));
        privacy.setTextFill(Color.BLACK);

        Hyperlink help = new Hyperlink("Help");
        help.setFont(Font.font("KyivType Sans", 12));
        help.setTextFill(Color.BLACK);

        copyrightBar.getChildren().addAll(copyright, privacy, help);

        // Main Layout Structure using BorderPane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);           // Top bar with title and login button
        mainLayout.setCenter(overlayPanel);  // Central overlay panel
        mainLayout.setBottom(new VBox(0, navBar, copyrightBar));  // Footer with nav and copyright

        // Root StackPane
        StackPane root = new StackPane();
        root.getChildren().addAll(bgImageView, mainLayout);

        // Bind background image to root size
        bgImageView.fitWidthProperty().bind(root.widthProperty());
        bgImageView.fitHeightProperty().bind(root.heightProperty());

        return new Scene(root, 800, 600);
    }
}