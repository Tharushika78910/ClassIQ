package Frontend;

import Backend.model.dao.impl.AppUserDaoImpl;
import Backend.model.dao.impl.UserProfileDaoImpl;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.entity.Student;
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

    private static final String BG_IMAGE = "/Login.png";

    // logo image
    private static final String LOGO = "/Logoline.png";

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

        ImageView bg = createBackgroundView();
        root.getChildren().add(bg);

        VBox mainBox = new VBox(25);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        mainBox.setBackground(Background.EMPTY);
        mainBox.setStyle("-fx-background-color: transparent;");
        mainBox.setTranslateY(-25);

        ImageView logo = new ImageView();
        try {
            logo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(LOGO))));
        } catch (Exception ignored) {}

        logo.setPreserveRatio(true);
        logo.setSmooth(true);
        logo.setFitWidth(350);
        VBox.setMargin(logo, new Insets(20, 0, 0, 0));

        HBox cards = new HBox(50);
        cards.setAlignment(Pos.CENTER);
        cards.setBackground(Background.EMPTY);
        cards.setStyle("-fx-background-color: transparent;");
        cards.setTranslateY(-60); //login panel move it slightly more upward

        VBox teacherCard = buildLoginCard("I am a Teacher", TEACHER_AVATAR, "TEACHER");
        VBox studentCard = buildLoginCard("I am a Student", STUDENT_AVATAR, "STUDENT");

        cards.getChildren().addAll(teacherCard, studentCard);

        mainBox.getChildren().addAll(logo, cards);
        root.getChildren().add(mainBox);

        String pillNormal =
                "-fx-background-color: rgba(255,255,255,0.92);" +
                        "-fx-text-fill: #2E6F62;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10,0,0,2);";

        String pillHover =
                "-fx-background-color: #9AC4B7;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;";

        Button btnBack = new Button("← Back");
        btnBack.setStyle(pillNormal);
        btnBack.setOnMouseEntered(e -> btnBack.setStyle(pillHover));
        btnBack.setOnMouseExited(e -> btnBack.setStyle(pillNormal));

        btnBack.setOnAction(e -> {
            HomePage home = new HomePage();
            stage.setScene(home.getScene(stage));
        });

        StackPane.setAlignment(btnBack, Pos.BOTTOM_LEFT);
        StackPane.setMargin(btnBack, new Insets(0, 0, 25, 25));
        root.getChildren().add(btnBack);

        return root;
    }

    private VBox buildLoginCard(String titleText, String avatarPath, String roleType) {

        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(28));
        card.setPrefWidth(300);

        card.setStyle(
                "-fx-background-color: rgba(156,196,183,0.85);" +
                        "-fx-background-radius: 20;"
        );

        ImageView avatar = new ImageView();
        try {
            avatar.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(avatarPath))));
        } catch (Exception ignored) {}

        avatar.setFitWidth(90);
        avatar.setPreserveRatio(true);

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField tfUser = new TextField();
        tfUser.setPromptText("Username");
        tfUser.setPrefWidth(240);

        PasswordField tfPass = new PasswordField();
        tfPass.setPromptText("Password");
        tfPass.setPrefWidth(240);

        Hyperlink forgot = new Hyperlink("forgot password?");
        forgot.setStyle("-fx-text-fill: #2F6DAA; -fx-font-size: 13px;");
        forgot.setOnAction(e -> showForgotPasswordDialog());

        Label error = new Label();
        error.setTextFill(Color.DARKRED);

        Button loginBtn = new Button("Log in");
        loginBtn.setPrefWidth(240);

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

                StudentDaoImpl studentDao = new StudentDaoImpl();
                Student fullStudent;

                try {
                    fullStudent = studentDao.findById(sp.studentId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    error.setText("Failed to load student.");
                    return;
                }

                if (fullStudent == null) {
                    error.setText("Student not found.");
                    return;
                }

                Session.setRole(Session.Role.STUDENT);
                Session.setUserId(result.userId);
                Session.setCurrentStudent(fullStudent);

                String fullName = fullStudent.getFirstName() + " " + fullStudent.getLastName();

                StudentDashboard dash = new StudentDashboard(fullName, fullStudent.getEmail(), STUDENT_AVATAR);
                stage.getScene().setRoot(new StackPane(createBackgroundView(), dash));

            } else {

                var tp = profileDao.findTeacherByUserId(result.userId);
                if (tp == null) {
                    error.setText("Teacher profile not found.");
                    return;
                }

                Session.setRole(Session.Role.TEACHER);
                Session.setUserId(result.userId);
                Session.setTeacherId(tp.teacherId);
                Session.setTeacherSubject(tp.subject);

                TeacherDashboard dash = new TeacherDashboard(tp.name, tp.email, TEACHER_AVATAR);
                stage.getScene().setRoot(new StackPane(createBackgroundView(), dash));
            }
        });

        card.getChildren().addAll(
                avatar, title,
                tfUser, tfPass,
                forgot, error, loginBtn
        );

        return card;
    }

    private void showForgotPasswordDialog() {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Reset Password");

        ButtonType resetBtn = new ButtonType("Reset", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(resetBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField username = new TextField();
        username.setPromptText("Username");

        TextField email = new TextField();
        email.setPromptText("Registered Email");

        PasswordField newPass = new PasswordField();
        newPass.setPromptText("New Password");

        PasswordField confirm = new PasswordField();
        confirm.setPromptText("Confirm Password");

        Label msg = new Label();
        msg.setTextFill(Color.RED);

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(email, 1, 1);
        grid.add(new Label("New Password:"), 0, 2);
        grid.add(newPass, 1, 2);
        grid.add(new Label("Confirm:"), 0, 3);
        grid.add(confirm, 1, 3);
        grid.add(msg, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Button resetButton = (Button) dialog.getDialogPane().lookupButton(resetBtn);
        resetButton.addEventFilter(javafx.event.ActionEvent.ACTION, ev -> {

            msg.setText("");

            String u = username.getText() == null ? "" : username.getText().trim();
            String e = email.getText() == null ? "" : email.getText().trim();
            String p1 = newPass.getText() == null ? "" : newPass.getText();
            String p2 = confirm.getText() == null ? "" : confirm.getText();

            if (u.isEmpty() || e.isEmpty() || p1.isEmpty() || p2.isEmpty()) {
                msg.setText("Please fill all fields.");
                ev.consume();
                return;
            }

            if (!p1.equals(p2)) {
                msg.setText("Passwords do not match.");
                ev.consume();
                return;
            }

            AppUserDaoImpl dao = new AppUserDaoImpl();
            Integer userId = dao.findUserIdByUsernameAndEmail(u, e);

            if (userId == null) {
                msg.setText("Invalid username/email.");
                ev.consume();
                return;
            }

            boolean updated = dao.updatePassword(userId, p1);

            if (!updated) {
                msg.setText("Reset failed.");
                ev.consume();
                return;
            }

            new Alert(Alert.AlertType.INFORMATION, "Password reset successful.").showAndWait();
        });

        dialog.showAndWait();
    }

    private ImageView createBackgroundView() {
        ImageView bg = new ImageView();
        try {
            bg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(BG_IMAGE))));
        } catch (Exception ignored) {}
        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setSmooth(true);
        return bg;
    }
}