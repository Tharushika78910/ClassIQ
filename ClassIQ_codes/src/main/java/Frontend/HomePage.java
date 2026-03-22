package Frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Objects;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class HomePage {

    private static final String SAGE_BUTTON = "#9AC4B7";
    private static final String SAGE_BUTTON_HOVER = "#8AB5A8";
    private static final String GOLD_STAR = "#D4A84B";
    private static final String GOLD_STAR_HOVER = "#FFD700";
    
    // Current locale for localization
    private Locale currentLocale = new Locale("en", "US");

    public HomePage() {
        // Default constructor - uses English
    }
    
    public HomePage(Locale locale) {
        this.currentLocale = locale;
    }

    public Scene getScene(Stage stage) {
        return getScene(stage, currentLocale);
    }
    
    public Scene getScene(Stage stage, Locale locale) {
        this.currentLocale = locale;
        
        StackPane root = new StackPane();

        // Background image
        ImageView background = loadImageView("/Homepage.png");
        if (background != null) {
            background.setPreserveRatio(false);
            background.setSmooth(true);
            background.fitWidthProperty().bind(stage.widthProperty());
            background.fitHeightProperty().bind(stage.heightProperty());
            root.getChildren().add(background);
        }

        Pane content = createCenterContent(stage);
        root.getChildren().add(content);

        return new Scene(root, Main.APP_WIDTH, Main.APP_HEIGHT);
    }

    private Pane createCenterContent(Stage stage) {

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(40));

        // Glass panel
        VBox glassPanel = new VBox(20);
        glassPanel.setAlignment(Pos.CENTER);
        glassPanel.setPadding(new Insets(40));
        glassPanel.setMaxWidth(450);

        glassPanel.setBackground(new Background(
                new BackgroundFill(Color.rgb(255, 255, 255, 0.18),
                        new CornerRadii(20),
                        Insets.EMPTY)
        ));

        glassPanel.setBorder(new Border(new BorderStroke(
                Color.rgb(255, 255, 255, 0.35),
                BorderStrokeStyle.SOLID,
                new CornerRadii(20),
                new BorderWidths(1.5)
        )));

        // Logo
        ImageView logo = loadImageView("/logo.png");
        if (logo != null) {
            logo.setPreserveRatio(true);
            logo.setSmooth(true);
            logo.fitWidthProperty().bind(stage.widthProperty().multiply(0.35));
            glassPanel.getChildren().add(logo);
        }

        // Login button
        ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);
        Button loginBtn = new Button(bundle.getString("login"));
        loginBtn.setStyle(buttonStyle(SAGE_BUTTON));
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(buttonStyle(SAGE_BUTTON_HOVER)));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(buttonStyle(SAGE_BUTTON)));

        loginBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage, currentLocale);
            stage.setScene(loginPage.getScene());
        });

        glassPanel.getChildren().add(loginBtn);

        // Language selection dropdown
        ComboBox<String> languageCombo = new ComboBox<>();
        languageCombo.getItems().addAll("English", "සිංහල", "العربية");
        
        // Set current language
        if (currentLocale.getLanguage().equals("si")) {
            languageCombo.setValue("සිංහල");
        } else if (currentLocale.getLanguage().equals("ar")) {
            languageCombo.setValue("العربية");
        } else {
            languageCombo.setValue("English");
        }
        
        // Handle language change
        languageCombo.setOnAction(e -> {
            String selected = languageCombo.getValue();
            Locale newLocale;
            if (selected.equals("සිංහල")) {
                newLocale = new Locale("si", "LK");
            } else if (selected.equals("العربية")) {
                newLocale = new Locale("ar", "SA");
            } else {
                newLocale = new Locale("en", "US");
            }
            // Refresh the scene to apply new language
            stage.setScene(getScene(stage, newLocale));
        });
        
        glassPanel.getChildren().add(languageCombo);

        // Stars
        HBox stars = new HBox(5);
        stars.setAlignment(Pos.CENTER);

        for (int i = 0; i < 5; i++) {
            Label star = new Label("★");
            star.setFont(Font.font(26));
            star.setTextFill(Color.web(GOLD_STAR));

            star.setOnMouseEntered(ev -> star.setTextFill(Color.web(GOLD_STAR_HOVER)));
            star.setOnMouseExited(ev -> star.setTextFill(Color.web(GOLD_STAR)));

            stars.getChildren().add(star);
        }
        glassPanel.getChildren().add(stars);

        // Footer
        HBox footer = new HBox(8);
        footer.setAlignment(Pos.CENTER);

        Label copyright = new Label(bundle.getString("copyright"));
        copyright.setFont(Font.font("Segoe UI", 12));
        copyright.setTextFill(Color.BLACK);

        Hyperlink privacy = new Hyperlink(bundle.getString("privacy.policy"));
        Hyperlink help = new Hyperlink(bundle.getString("help"));
        privacy.setStyle("-fx-text-fill: green; -fx-underline: false;");
        help.setStyle("-fx-text-fill: green; -fx-underline: false;");

        // open web pages inside app
        privacy.setOnAction(e -> openWebPage(stage, "/privacy.html"));
        help.setOnAction(e -> openWebPage(stage, "/help.html"));

        footer.getChildren().addAll(copyright, privacy, help);

        layout.setCenter(glassPanel);
        layout.setBottom(footer);
        
        // Apply RTL layout if needed (after footer is created)
        if (isRTL(currentLocale)) {
            applyRTLLayout(glassPanel, footer, languageCombo);
        }

        return layout;
    }

    private void openWebPage(Stage stage, String resourcePath) {
        
        ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);

        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            System.err.println("Resource not found: " + resourcePath);
            return;
        }

        // Read HTML content from resource file
        String htmlContent = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            htmlContent = content.toString();
        } catch (IOException e) {
            System.err.println("Error reading resource: " + resourcePath);
            return;
        }

        // Replace placeholder keys with localized text using automatic loop
        for (String key : bundle.keySet()) {
            if (key.startsWith("html.")) {
                htmlContent = htmlContent.replace(key, bundle.getString(key));
            }
        }

        // WebView
        WebView webView = new WebView();
        webView.getEngine().loadContent(htmlContent);

        VBox glassPanel = new VBox(webView);
        glassPanel.setPadding(new Insets(30));
        glassPanel.setAlignment(Pos.CENTER);
        glassPanel.setMaxWidth(750);
        glassPanel.setMaxHeight(540);

        glassPanel.setBackground(new Background(
                new BackgroundFill(Color.rgb(255, 255, 255, 0.18),
                        new CornerRadii(20),
                        Insets.EMPTY)
        ));

        glassPanel.setBorder(new Border(new BorderStroke(
                Color.rgb(255, 255, 255, 0.35),
                BorderStrokeStyle.SOLID,
                new CornerRadii(20),
                new BorderWidths(1.5)
        )));

        // Back button
        Button backBtn = new Button(bundle.getString("back") + " ←");
        backBtn.setStyle(buttonStyle(SAGE_BUTTON));
        backBtn.setPadding(new Insets(10, 18, 10, 18));
        backBtn.setOnAction(ev -> stage.setScene(getScene(stage)));

        HBox topBar = new HBox(backBtn);
        topBar.setPadding(new Insets(12));
        topBar.setAlignment(Pos.TOP_LEFT);

        BorderPane webLayout = new BorderPane();
        webLayout.setTop(topBar);
        webLayout.setCenter(glassPanel);

        // Background image
        ImageView background = loadImageView("/Homepage.png");
        if (background != null) {
            background.setPreserveRatio(false);
            background.setSmooth(true);
            background.fitWidthProperty().bind(stage.widthProperty());
            background.fitHeightProperty().bind(stage.heightProperty());
        }

        StackPane root = (background != null)
                ? new StackPane(background, webLayout)
                : new StackPane(webLayout);

        stage.setScene(new Scene(root, Main.APP_WIDTH, Main.APP_HEIGHT));
    }
    
    // Helper method to check if locale is RTL
    private boolean isRTL(Locale locale) {
        return locale.getLanguage().equals("ar");
    }
    
    // Helper method to apply RTL layout
    private void applyRTLLayout(VBox glassPanel, HBox footer, ComboBox<String> languageCombo) {
        glassPanel.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        footer.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        languageCombo.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
    }

    private String buttonStyle(String color) {
        return "-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 16px;" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 12 60 12 60;";
    }

    private ImageView loadImageView(String path) {
        try {
            return new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
        } catch (Exception e) {
            return null;
        }
    }
}
