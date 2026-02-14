package Frontend;

import Frontend.student.StudentDashboard;
import Frontend.teacher.TeacherDashboard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginPage {

    public Scene getScene(Stage stage) {

        // background img
        Image bgImage = new Image(getClass().getResourceAsStream("/Login.png"));
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setPreserveRatio(false);

        // teacher panel
        VBox teacherPanel = createLoginPanel();
        ImageView teacherIconView = createCroppedIcon("/Teacher.png", 90, 90);
        Label teacherLabel = new Label("I am a Teacher");
        teacherLabel.setFont(Font.font("KyivType Sans", FontWeight.BOLD, 18));

        HBox teacherUsernameBox = createInputBox("Username", 180); // increased width
        HBox teacherPasswordBox = createInputBox("Password", 180, true); // increased width

        Hyperlink teacherForgot = new Hyperlink("forgot password?");
        teacherForgot.setTextFill(Color.web("#1976D2"));

        Button teacherLogin = createFancyButton("Log in");
        teacherLogin.setOnAction(e -> {
            TeacherDashboard dashboard = new TeacherDashboard(
                    "Mr. Matti Valovirta",
                    "mattiv@metropolia.com",
                    "/Teacher.png"
            );
            Scene teacherScene = new Scene(dashboard, 1100, 700);
            teacherScene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            teacherScene.getStylesheets().add(getClass().getResource("/css/teacher.css").toExternalForm());
            stage.setTitle("Teacher Dashboard");
            stage.setScene(teacherScene);
        });

        teacherPanel.getChildren().addAll(teacherIconView, teacherLabel, teacherUsernameBox, teacherPasswordBox, teacherForgot, teacherLogin);

        // student panel
        VBox studentPanel = createLoginPanel();
        ImageView studentIconView = createCroppedIcon("/Student.png", 90, 90);
        Label studentLabel = new Label("I am a Student");
        studentLabel.setFont(Font.font("KyivType Sans", FontWeight.BOLD, 18));

        HBox studentUsernameBox = createInputBox("Username", 180); // increased width
        HBox studentPasswordBox = createInputBox("Password", 180, true); // increased width

        Hyperlink studentForgot = new Hyperlink("forgot password?");
        studentForgot.setTextFill(Color.web("#1976D2"));

        Button studentLogin = createFancyButton("Log in");
        studentLogin.setOnAction(e -> {
            StudentDashboard dashboard = new StudentDashboard(
                    "Bao Tran",
                    "bao@student.com",
                    "/Student.png"
            );
            Scene studentScene = new Scene(dashboard, 1100, 700);
            studentScene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            studentScene.getStylesheets().add(getClass().getResource("/css/student.css").toExternalForm());
            stage.setTitle("Student Dashboard");
            stage.setScene(studentScene);
        });

        studentPanel.getChildren().addAll(studentIconView, studentLabel, studentUsernameBox, studentPasswordBox, studentForgot, studentLogin);

        // center content
        HBox panelsBox = new HBox(50, teacherPanel, studentPanel);
        panelsBox.setAlignment(Pos.CENTER);

        VBox mainContent = new VBox(40, panelsBox);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(40, 20, 20, 20));
        mainContent.setTranslateX(80); // shift panels slightly to the right

        // root stack pane
        StackPane root = new StackPane(bgImageView, mainContent);

        // home button
        Button homeBtn = new Button("Home");
        homeBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        homeBtn.setStyle(
                "-fx-background-radius: 12; " +
                        "-fx-background-color: linear-gradient(to bottom, #32CD32, #28a428); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 8 20 8 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 4, 0, 0, 2);"
        );
        StackPane.setAlignment(homeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(homeBtn, new Insets(20, 20, 0, 0));

        homeBtn.setOnAction(e -> {
            HomePage home = new HomePage();
            Scene homeScene = home.getScene(stage);
            stage.setTitle("Class iQ - Home");
            stage.setScene(homeScene);
        });

        root.getChildren().add(homeBtn);

        bgImageView.fitWidthProperty().bind(root.widthProperty());
        bgImageView.fitHeightProperty().bind(root.heightProperty());

        return new Scene(root, 850, 600); // slightly wider to accommodate larger fields
    }

    // panel creation with gradient background and rounded corners
    private VBox createLoginPanel() {
        VBox panel = new VBox(20);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setPadding(new Insets(30));
        panel.setPrefWidth(300);
        panel.setMaxWidth(300);

        LinearGradient panelGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(162, 184, 172, 0.85)),
                new Stop(1, Color.rgb(180, 200, 185, 0.85))
        );
        panel.setBackground(new Background(new BackgroundFill(panelGradient, new CornerRadii(20), Insets.EMPTY)));

        return panel;
    }

    // create input box with label and text field, with focus effects
    private HBox createInputBox(String labelText, double fieldWidth) {
        return createInputBox(labelText, fieldWidth, false);
    }

    private HBox createInputBox(String labelText, double fieldWidth, boolean isPassword) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(labelText);
        label.setPrefWidth(90);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));

        Control input = isPassword ? new PasswordField() : new TextField();
        input.setPrefWidth(fieldWidth);
        input.setStyle(
                "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: #A2B8AC; " +
                        "-fx-padding: 8; " + // slightly bigger padding
                        "-fx-font-size: 14px;" // bigger font
        );

        input.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                input.setStyle(
                        "-fx-background-radius: 10; " +
                                "-fx-border-radius: 10; " +
                                "-fx-border-color: #32CD32; " +
                                "-fx-padding: 8; " +
                                "-fx-font-size: 14px;"
                );
            } else {
                input.setStyle(
                        "-fx-background-radius: 10; " +
                                "-fx-border-radius: 10; " +
                                "-fx-border-color: #A2B8AC; " +
                                "-fx-padding: 8; " +
                                "-fx-font-size: 14px;"
                );
            }
        });

        box.getChildren().addAll(label, input);
        return box;
    }

    // fancy button with gradient, rounded corners, and shadow
    private Button createFancyButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btn.setStyle(
                "-fx-background-radius: 12; " +
                        "-fx-background-color: linear-gradient(to bottom, #32CD32, #28a428); " +
                        "-fx-text-fill: white; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        return btn;
    }

    // icon cropping to remove transparent padding
    private ImageView createCroppedIcon(String path, double width, double height) {
        Image img = new Image(getClass().getResourceAsStream(path));
        Rectangle2D viewport = getVisibleBounds(img);
        ImageView view = new ImageView(img);
        view.setViewport(viewport);
        view.setFitWidth(width);
        view.setFitHeight(height);
        view.setPreserveRatio(true);
        view.setSmooth(true);
        return view;
    }

    private Rectangle2D getVisibleBounds(Image img) {
        PixelReader reader = img.getPixelReader();
        int width = (int) img.getWidth();
        int height = (int) img.getHeight();

        int minX = width, minY = height, maxX = 0, maxY = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y);
                int alpha = (argb >> 24) & 0xff;
                if (alpha != 0) {
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }

        if (minX > maxX || minY > maxY) {
            return new Rectangle2D(0, 0, width, height);
        }

        return new Rectangle2D(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }
}
