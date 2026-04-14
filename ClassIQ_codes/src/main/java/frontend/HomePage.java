package frontend;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomePage {

    private static final Logger LOGGER = Logger.getLogger(HomePage.class.getName());

    private static final String SAGE_BUTTON = "#9AC4B7";
    private static final String SAGE_BUTTON_HOVER = "#8AB5A8";
    private static final String GOLD_STAR = "#D4A84B";
    private static final String GOLD_STAR_HOVER = "#FFD700";

    private static final String ENGLISH_LABEL = "English";
    private static final String SINHALA_LABEL = "සිංහල";
    private static final String ARABIC_LABEL = "العربية";

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

        VBox glassPanel = new VBox(20);
        glassPanel.setAlignment(Pos.CENTER);
        glassPanel.setPadding(new Insets(40));
        glassPanel.setMaxWidth(450);

        glassPanel.setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(255, 255, 255, 0.18),
                        new CornerRadii(20),
                        Insets.EMPTY
                )
        ));

        glassPanel.setBorder(new Border(new BorderStroke(
                Color.rgb(255, 255, 255, 0.35),
                BorderStrokeStyle.SOLID,
                new CornerRadii(20),
                new BorderWidths(1.5)
        )));

        ImageView logo = loadImageView("/logo.png");
        if (logo != null) {
            logo.setPreserveRatio(true);
            logo.setSmooth(true);
            logo.fitWidthProperty().bind(stage.widthProperty().multiply(0.35));
            glassPanel.getChildren().add(logo);
        }

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

        ComboBox<String> languageCombo = new ComboBox<>();
        languageCombo.getItems().addAll(ENGLISH_LABEL, SINHALA_LABEL, ARABIC_LABEL);

        if ("si".equals(currentLocale.getLanguage())) {
            languageCombo.setValue(SINHALA_LABEL);
        } else if ("ar".equals(currentLocale.getLanguage())) {
            languageCombo.setValue(ARABIC_LABEL);
        } else {
            languageCombo.setValue(ENGLISH_LABEL);
        }

        languageCombo.setOnAction(e -> {
            String selected = languageCombo.getValue();
            Locale newLocale;

            if (SINHALA_LABEL.equals(selected)) {
                newLocale = new Locale("si", "LK");
            } else if (ARABIC_LABEL.equals(selected)) {
                newLocale = new Locale("ar", "SA");
            } else {
                newLocale = new Locale("en", "US");
            }

            Session.setCurrentLocale(newLocale);
            stage.setScene(getScene(stage, newLocale));
        });

        glassPanel.getChildren().add(languageCombo);

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

        HBox footer = new HBox(8);
        footer.setAlignment(Pos.CENTER);

        Label copyright = new Label(bundle.getString("copyright"));
        copyright.setFont(Font.font("Segoe UI", 12));
        copyright.setTextFill(Color.BLACK);

        Hyperlink privacy = new Hyperlink(bundle.getString("privacy.policy"));
        Hyperlink help = new Hyperlink(bundle.getString("help"));
        privacy.setStyle("-fx-text-fill: green; -fx-underline: false;");
        help.setStyle("-fx-text-fill: green; -fx-underline: false;");

        privacy.setOnAction(e -> openWebPage(stage, "/privacy.html"));
        help.setOnAction(e -> openWebPage(stage, "/help.html"));

        footer.getChildren().addAll(copyright, privacy, help);

        layout.setCenter(glassPanel);
        layout.setBottom(footer);

        if (isRTL(currentLocale)) {
            applyRTLLayout(glassPanel, footer, languageCombo);
        }

        return layout;
    }

    private void openWebPage(Stage stage, String resourcePath) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);

        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            LOGGER.warning(() -> "Resource not found: " + resourcePath);
            return;
        }

        String htmlContent = "";
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            htmlContent = content.toString();

        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception, () -> "Error reading resource: " + resourcePath);
            return;
        }

        for (String key : bundle.keySet()) {
            if (key.startsWith("html.")) {
                try {
                    String value = bundle.getString(key);
                    htmlContent = htmlContent.replace(key, value);
                } catch (Exception exception) {
                    String finalKey = key;
                    LOGGER.log(Level.WARNING, exception, () -> "Error replacing key: " + finalKey);
                }
            }
        }

        htmlContent = htmlContent.replaceAll("html\\.[a-zA-Z0-9.]+", "");
        htmlContent = htmlContent.replace(".title", "");
        htmlContent = htmlContent.replace(".content", "");

        if (isRTL(currentLocale)) {
            htmlContent = htmlContent.replace("<html lang=\"en\">", "<html lang=\"ar\" dir=\"rtl\">");
        }

        WebView webView = new WebView();
        webView.getEngine().loadContent(htmlContent);

        VBox glassPanel = new VBox(webView);
        glassPanel.setPadding(new Insets(30));
        glassPanel.setAlignment(Pos.CENTER);
        glassPanel.setMaxWidth(750);
        glassPanel.setMaxHeight(540);

        glassPanel.setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(255, 255, 255, 0.18),
                        new CornerRadii(20),
                        Insets.EMPTY
                )
        ));

        glassPanel.setBorder(new Border(new BorderStroke(
                Color.rgb(255, 255, 255, 0.35),
                BorderStrokeStyle.SOLID,
                new CornerRadii(20),
                new BorderWidths(1.5)
        )));

        String backArrow = isRTL(currentLocale) ? " →" : " ←";
        Button backBtn = new Button(bundle.getString("back") + backArrow);
        backBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92);" +
                        "-fx-text-fill: #2E6F62;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10,0,0,2);"
        );
        backBtn.setOnAction(ev -> stage.setScene(getScene(stage, Session.getCurrentLocale())));

        HBox bottomBar = new HBox(backBtn);
        bottomBar.setPadding(new Insets(12));
        bottomBar.setAlignment(isRTL(currentLocale) ? Pos.BOTTOM_RIGHT : Pos.BOTTOM_LEFT);

        BorderPane webLayout = new BorderPane();
        webLayout.setCenter(glassPanel);
        webLayout.setBottom(bottomBar);

        ImageView background = loadImageView("/Homepage.png");
        if (background != null) {
            background.setPreserveRatio(false);
            background.setSmooth(true);
            background.fitWidthProperty().bind(stage.widthProperty());
            background.fitHeightProperty().bind(stage.heightProperty());
        }

        StackPane root = background != null
                ? new StackPane(background, webLayout)
                : new StackPane(webLayout);

        stage.setScene(new Scene(root, Main.APP_WIDTH, Main.APP_HEIGHT));
    }

    private boolean isRTL(Locale locale) {
        return "ar".equals(locale.getLanguage());
    }

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
        } catch (Exception exception) {
            return null;
        }
    }
}