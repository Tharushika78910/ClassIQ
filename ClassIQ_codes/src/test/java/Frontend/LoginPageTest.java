package Frontend;

import Backend.model.dao.impl.AppUserDaoImpl;
import Backend.model.dao.impl.UserProfileDaoImpl;
import Frontend.student.StudentDashboard;
import Frontend.teacher.TeacherDashboard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginPageTest {

    private static final String HOME_BG = "/Homepage.png";
    private static final String LOGO = "/logo.png";

    private static final String STUDENT_AVATAR = "/Student.png";
    private static final String TEACHER_AVATAR = "/Teacher.png";

    private final Stage stage;

    public LoginPageTest(Stage stage) {
        this.stage = stage;
    }

    public Parent getView() {

        // ===== Background =====
        ImageView bg = new ImageView();
        try {
            bg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(HOME_BG))));
        } catch (Exception ignored) {}
        bg.setPreserveRatio(false);

        StackPane root = new StackPane(bg);
        bg.fitWidthProperty().bind(root.widthProperty());
        bg.fitHeightProperty().bind(root.heightProperty());

        // ===== Main layout over background =====
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // ===== TOP BAR (Logo center + Back right) =====
        BorderPane topBar = new BorderPane();
        topBar.setPadding(new Insets(10));
        topBar.setPickOnBounds(false);

        // Back button (TOP RIGHT)
        Button backBtn = new Button("← Back");
        backBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.78);" +
                        "-fx-text-fill: #1b5e20;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 16;"
        );
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92);" +
                        "-fx-text-fill: #1b5e20;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 16;"
        ));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.78);" +
                        "-fx-text-fill: #1b5e20;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 16;"
        ));

        backBtn.setOnAction(e -> {
            HomePage home = new HomePage();
            stage.setScene(home.getScene(stage));
        });

        // Logo (TOP CENTER) — no panel behind it
        ImageView logo = new ImageView();
        try {
            logo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(LOGO))));
        } catch (Exception ignored) {}
        logo.setPreserveRatio(true);
        logo.setFitHeight(135);
        logo.setMouseTransparent(true);

        StackPane logoWrap = new StackPane(logo);
        logoWrap.setMouseTransparent(true);

        topBar.setCenter(logoWrap);
        topBar.setRight(backBtn);

        layout.setTop(topBar);

        // ===== CENTER CARDS =====
        VBox teacherCard = buildLoginCard("I am a Teacher", TEACHER_AVATAR, "TEACHER");
        VBox studentCard = buildLoginCard("I am a Student", STUDENT_AVATAR, "STUDENT");

        HBox cards = new HBox(80, teacherCard, studentCard);
        cards.setAlignment(Pos.CENTER);
        cards.setPadding(new Insets(10, 60, 40, 60));

        layout.setCenter(cards);

        root.getChildren().add(layout);
        return root;
    }

    private VBox buildLoginCard(String titleText, String iconPath, String expectedRole) {

        VBox card = new VBox(14);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(26));
        card.setPrefWidth(380);
        card.setMaxWidth(380);

        // green glass style
        card.setStyle(
                "-fx-background-color: rgba(140, 179, 166, 0.78);" +
                        "-fx-background-radius: 26;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 18, 0.25, 0, 6);"
        );

        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        } catch (Exception ignored) {}
        icon.setFitHeight(70);
        icon.setPreserveRatio(true);
        icon.setMouseTransparent(true);

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 800; -fx-text-fill: #2b2b2b;");

        TextField tfUser = new TextField();
        tfUser.setPromptText("Username");
        styleField(tfUser);

        PasswordField tfPass = new PasswordField();
        tfPass.setPromptText("Password");
        styleField(tfPass);

        Hyperlink forgot = new Hyperlink("forgot password?");
        forgot.setStyle("-fx-text-fill: #1a73e8; -fx-underline: false;");
        forgot.setOnAction(e ->
                new Alert(Alert.AlertType.INFORMATION, "Forgot password feature not added yet.").showAndWait()
        );

        Label error = new Label();
        error.setStyle("-fx-text-fill: #B00020; -fx-font-weight: 700;");

        Button loginBtn = new Button("Log in");
        loginBtn.setPrefWidth(260);
        loginBtn.setPrefHeight(40);
        loginBtn.setStyle(
                "-fx-background-color: #2aa542;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: 800;" +
                        "-fx-font-size: 16px;" +
                        "-fx-background-radius: 18;"
        );

        loginBtn.setOnAction(e -> {
            error.setText("");

            String username = tfUser.getText() == null ? "" : tfUser.getText().trim();
            String password = tfPass.getText() == null ? "" : tfPass.getText();

            if (username.isEmpty() || password.isEmpty()) {
                error.setText("Please enter username and password.");
                return;
            }

            AppUserDaoImpl auth = new AppUserDaoImpl();
            var result = auth.login(username, password);

            if (result == null) {
                error.setText("Invalid username or password.");
                return;
            }

            // enforce correct panel
            if (!expectedRole.equalsIgnoreCase(result.role)) {
                error.setText("Please use the " + expectedRole.toLowerCase() + " login panel.");
                return;
            }

            UserProfileDaoImpl profileDao = new UserProfileDaoImpl();

            if ("STUDENT".equalsIgnoreCase(result.role)) {
                var sp = profileDao.findStudentByUserId(result.userId);
                if (sp == null) { error.setText("Student profile not found."); return; }

                StudentDashboard dash = new StudentDashboard(sp.name, sp.email, STUDENT_AVATAR);
                stage.getScene().setRoot(dash);

            } else if ("TEACHER".equalsIgnoreCase(result.role)) {
                var tp = profileDao.findTeacherByUserId(result.userId);
                if (tp == null) { error.setText("Teacher profile not found."); return; }

                TeacherDashboard dash = new TeacherDashboard(tp.name, tp.email, TEACHER_AVATAR);
                stage.getScene().setRoot(dash);
            }
        });

        card.getChildren().addAll(icon, title, tfUser, tfPass, forgot, error, loginBtn);
        return card;
    }

    private void styleField(TextField tf) {
        tf.setPrefWidth(300);
        tf.setPrefHeight(34);
        tf.setStyle(
                "-fx-background-color: rgba(255,255,255,0.96);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: rgba(0,0,0,0.12);" +
                        "-fx-padding: 8 10;"
        );
    }

    public void show() {
        Scene scene = new Scene(getView(), Main.APP_WIDTH, Main.APP_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Class iQ");
        stage.show();
    }
}