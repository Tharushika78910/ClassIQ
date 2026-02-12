package Frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PrivacyPage {

    public Scene getScene(Stage stage) {
        StackPane root = new StackPane();
        BorderPane mainLayout = new BorderPane();
        root.getChildren().add(createBackground());
        root.getChildren().add(mainLayout);

        mainLayout.setTop(createTopBar(stage));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(createPrivacyContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        mainLayout.setCenter(scrollPane);

        return new Scene(root, 900, 600);
    }

    // ================= BACKGROUND =================
    private Region createBackground() {
        Region background = new Region();
        background.setBackground(new Background(new BackgroundFill(
                Color.rgb(30, 30, 30), // Dark gray background
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        return background;
    }

    // ================= TOP BAR =================
    private HBox createTopBar(Stage stage) {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER_LEFT);

        topBar.setBackground(new Background(new BackgroundFill(
                Color.rgb(50, 50, 50), // Slightly lighter dark for top bar
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        javafx.scene.control.Button backBtn = new javafx.scene.control.Button("← Back");
        backBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backBtn.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: white; -fx-background-radius: 8;");
        backBtn.setPadding(new Insets(8, 15, 8, 15));
        backBtn.setOnAction(e -> {
            HomePage homePage = new HomePage();
            stage.setScene(homePage.getScene(stage));
        });

        Label title = new Label("Privacy Policy");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(backBtn, spacer, title);
        return topBar;
    }

    // ================= CENTER CONTENT =================
    private VBox createPrivacyContent() {
        VBox content = new VBox(25); // spacing between sections
        content.setAlignment(Pos.TOP_LEFT);
        content.setPadding(new Insets(40, 60, 40, 60));
        content.setMaxWidth(700);

        // Main heading
        Label heading = new Label("Your Privacy Matters");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        heading.setTextFill(Color.WHITE);

        // Section 1: Data Collection
        Label dataCollectionHeading = new Label("📂 1. Data Collection");
        dataCollectionHeading.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        dataCollectionHeading.setTextFill(Color.WHITE);

        Label dataCollectionText = new Label(
                "We collect academic data such as grades, attendance, and course interactions to provide personalized insights. " +
                        "This data is only used to improve your learning experience."
        );
        dataCollectionText.setFont(Font.font("Arial", 16));
        dataCollectionText.setTextFill(Color.LIGHTGRAY);
        dataCollectionText.setWrapText(true);

        // Section 2: Data Security
        Label securityHeading = new Label("🛡️ 2. Data Security");
        securityHeading.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        securityHeading.setTextFill(Color.WHITE);

        Label securityText = new Label(
                "All data is securely stored and encrypted. We implement strict access controls to ensure your information remains safe."
        );
        securityText.setFont(Font.font("Arial", 16));
        securityText.setTextFill(Color.LIGHTGRAY);
        securityText.setWrapText(true);

        // Section 3: User Rights
        Label rightsHeading = new Label("👤 3. User Rights");
        rightsHeading.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        rightsHeading.setTextFill(Color.WHITE);

        Label rightsText = new Label(
                "You have full access to view, update, or delete your information. Requests can be made by contacting support@classiq.com."
        );
        rightsText.setFont(Font.font("Arial", 16));
        rightsText.setTextFill(Color.LIGHTGRAY);
        rightsText.setWrapText(true);

        // Section 4: Policy Updates
        Label updatesHeading = new Label("🔄 4. Policy Updates");
        updatesHeading.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        updatesHeading.setTextFill(Color.WHITE);

        Label updatesText = new Label(
                "This policy may be updated periodically. Users will be notified of significant changes."
        );
        updatesText.setFont(Font.font("Arial", 16));
        updatesText.setTextFill(Color.LIGHTGRAY);
        updatesText.setWrapText(true);

        // Add all sections to the content VBox
        content.getChildren().addAll(
                heading,
                dataCollectionHeading, dataCollectionText,
                securityHeading, securityText,
                rightsHeading, rightsText,
                updatesHeading, updatesText
        );

        return content;
    }
}
