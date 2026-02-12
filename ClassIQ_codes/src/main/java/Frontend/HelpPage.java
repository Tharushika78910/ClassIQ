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

public class HelpPage {

    public Scene getScene(Stage stage) {
        StackPane root = new StackPane();
        BorderPane mainLayout = new BorderPane();
        root.getChildren().add(createBackground());
        root.getChildren().add(mainLayout);

        // Top bar with back button
        mainLayout.setTop(createTopBar(stage));

        // Center scrollable content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(createHelpContent());
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

        Label title = new Label("Help");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(backBtn, spacer, title);
        return topBar;
    }

    // ================= CENTER CONTENT =================
    private VBox createHelpContent() {
        VBox content = new VBox(25); // spacing between sections
        content.setAlignment(Pos.TOP_LEFT);
        content.setPadding(new Insets(40, 60, 40, 60));
        content.setMaxWidth(700);

        // Main heading
        Label heading = new Label("How Can We Help You?");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        heading.setTextFill(Color.WHITE);

        // Section 1: General Help
        Label generalHeading = new Label("💡 General Help");
        generalHeading.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        generalHeading.setTextFill(Color.WHITE);

        Label generalText = new Label(
                "For guidance on navigating your dashboard, understanding your reports, " +
                        "please explore the platform tutorials and FAQ sections."
        );
        generalText.setFont(Font.font("Arial", 16));
        generalText.setTextFill(Color.LIGHTGRAY);
        generalText.setWrapText(true);

        // Section 2: Technical Support
        Label techHeading = new Label("🛠️ Technical Support");
        techHeading.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        techHeading.setTextFill(Color.WHITE);

        Label techText = new Label(
                "If you encounter technical issues, errors, or bugs, contact our support team at " +
                        "support@classiq.com. Provide a clear description and screenshots if possible."
        );
        techText.setFont(Font.font("Arial", 16));
        techText.setTextFill(Color.LIGHTGRAY);
        techText.setWrapText(true);

        // Section 3: Account Assistance
        Label accountHeading = new Label("🔑 Account Assistance");
        accountHeading.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        accountHeading.setTextFill(Color.WHITE);

        Label accountText = new Label(
                "For password resets, account updates, or access issues, reach out to support@classiq.com. " +
                        "Our team will respond promptly to ensure your account is secure and accessible."
        );
        accountText.setFont(Font.font("Arial", 16));
        accountText.setTextFill(Color.LIGHTGRAY);
        accountText.setWrapText(true);

        // Section 4: Feedback & Suggestions
        Label feedbackHeading = new Label("✉️ Feedback & Suggestions");
        feedbackHeading.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        feedbackHeading.setTextFill(Color.WHITE);

        Label feedbackText = new Label(
                "We value your feedback! Share your ideas or suggestions to improve the platform by emailing us at " +
                        "feedback@classiq.com."
        );
        feedbackText.setFont(Font.font("Arial", 16));
        feedbackText.setTextFill(Color.LIGHTGRAY);
        feedbackText.setWrapText(true);

        // Add all sections to content
        content.getChildren().addAll(
                heading,
                generalHeading, generalText,
                techHeading, techText,
                accountHeading, accountText,
                feedbackHeading, feedbackText
        );

        return content;
    }
}
