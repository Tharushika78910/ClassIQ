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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginPage {

    private static final String BG_IMAGE = "/Homepage.png";
    private static final String LOGO = "/logo.png";
    private static final String STUDENT_AVATAR = "/Student.png";
    private static final String TEACHER_AVATAR = "/Teacher.png";

    private final Stage stage;

    public LoginPage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return new Scene(getView(), Main.APP_WIDTH, Main.APP_HEIGHT);
    }

    public Parent getView() {

        StackPane root = new StackPane();

        // ===== Background =====
        ImageView bg = new ImageView();
        try {
            bg.setImage(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(BG_IMAGE))));
        } catch (Exception ignored) {}

        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(root.widthProperty());
        bg.fitHeightProperty().bind(root.heightProperty());

        root.getChildren().add(bg);

        // ===== Main Content =====
        VBox mainBox = new VBox(35);
        mainBox.setAlignment(Pos.CENTER);

        // IMPORTANT: prevent VBox from blocking Back button
        mainBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // ===== Logo (NO WHITE PANEL) =====
        ImageView logo = new ImageView();
        try {
            logo.setImage(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(LOGO))));
        } catch (Exception ignored) {}

        logo.setPreserveRatio(true);
        logo.setSmooth(true);
        logo.setFitWidth(320);
        logo.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 20, 0.3, 0, 4);"
        );

        // ===== Login Cards =====
        HBox cards = new HBox(70);
        cards.setAlignment(Pos.CENTER);

        VBox teacherCard = buildLoginCard("I am a Teacher", TEACHER_AVATAR, "TEACHER");
        VBox studentCard = buildLoginCard("I am a Student", STUDENT_AVATAR, "STUDENT");

        cards.getChildren().addAll(teacherCard, studentCard);

        mainBox.getChildren().addAll(logo, cards);

        root.getChildren().add(mainBox);

        // ===== Back Button (TOP RIGHT) =====
        Button backBtn = new Button("← Back");

        String backNormal =
                "-fx-background-color: rgba(255,255,255,0.92);" +
                        "-fx-text-fill: #2E6F62;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10,0,0,2);";

        String backHover =
                "-fx-background-color: #9AC4B7;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;";

        backBtn.setStyle(backNormal);
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(backHover));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(backNormal));

        StackPane.setAlignment(backBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(backBtn, new Insets(22));

        backBtn.setOnAction(e -> {
            HomePage home = new HomePage();
            stage.setScene(home.getScene(stage));
        });

        // ADD LAST so it stays on top
        root.getChildren().add(backBtn);

        return root;
    }

    private VBox buildLoginCard(String titleText, String avatarPath, String roleType) {

        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(28));
        card.setPrefWidth(340);

        card.setStyle(
                "-fx-background-color: rgba(156,196,183,0.80);" +
                        "-fx-background-radius: 28;"
        );

        ImageView avatar = new ImageView();
        try {
            avatar.setImage(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(avatarPath))));
        } catch (Exception ignored) {}

        avatar.setFitWidth(90);
        avatar.setFitHeight(90);
        avatar.setPreserveRatio(true);

        Label title = new Label(titleText);
        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f1f1f;"
        );

        TextField tfUser = new TextField();
        tfUser.setPromptText("Username");
        tfUser.setPrefWidth(240);

        PasswordField tfPass = new PasswordField();
        tfPass.setPromptText("Password");
        tfPass.setPrefWidth(240);

        Hyperlink forgot = new Hyperlink("forgot password?");
        forgot.setStyle("-fx-text-fill: #2F6DAA; -fx-font-size: 13px;");
        forgot.setOnAction(e ->
                new Alert(Alert.AlertType.INFORMATION,
                        "Forgot password feature not implemented yet.")
                        .showAndWait());

        Label error = new Label();
        error.setTextFill(Color.DARKRED);
        error.setStyle("-fx-font-size: 13px;");

        Button loginBtn = new Button("Log in");
        loginBtn.setPrefWidth(240);
        loginBtn.setStyle(
                "-fx-background-color: #28A745;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 14;" +
                        "-fx-font-size: 15px;" +
                        "-fx-padding: 10 0 10 0;"
        );

        loginBtn.setOnAction(e -> {

            error.setText("");

            String username = tfUser.getText() == null ? "" : tfUser.getText().trim();
            String password = tfPass.getText() == null ? "" : tfPass.getText();

            if (username.isEmpty() || password.isEmpty()) {
                error.setText("Enter username and password.");
                return;
            }

            AppUserDaoImpl auth = new AppUserDaoImpl();
            AppUserDaoImpl.LoginResult result = auth.login(username, password);

            if (result == null) {
                error.setText("Invalid credentials.");
                return;
            }

            if (!roleType.equalsIgnoreCase(result.role)) {
                error.setText("You are not registered as " + roleType + ".");
                return;
            }

            UserProfileDaoImpl profileDao = new UserProfileDaoImpl();

            if ("STUDENT".equalsIgnoreCase(result.role)) {

                var sp = profileDao.findStudentByUserId(result.userId);
                if (sp == null) {
                    error.setText("Student profile not found.");
                    return;
                }

                StudentDashboard dash =
                        new StudentDashboard(sp.name, sp.email, STUDENT_AVATAR);

                stage.getScene().setRoot(dash);

            } else if ("TEACHER".equalsIgnoreCase(result.role)) {

                var tp = profileDao.findTeacherByUserId(result.userId);
                if (tp == null) {
                    error.setText("Teacher profile not found.");
                    return;
                }

                TeacherDashboard dash =
                        new TeacherDashboard(tp.name, tp.email, TEACHER_AVATAR);

                stage.getScene().setRoot(dash);
            }
        });

        card.getChildren().addAll(
                avatar, title, tfUser, tfPass, forgot, error, loginBtn
        );

        return card;
    }
}
