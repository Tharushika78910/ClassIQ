package Frontend;

import Frontend.student.StudentDashboard;
import Frontend.teacher.TeacherDashboard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginPage {

    public Scene getScene(Stage stage) {

        // ================= BACKGROUND IMAGE =================
        Image bgImage = new Image(getClass().getResourceAsStream("/Login.png"));
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setPreserveRatio(false);

        // ================= TEACHER PANEL =================
        VBox teacherPanel = new VBox(18); // more spacing
        teacherPanel.setAlignment(Pos.TOP_CENTER);
        teacherPanel.setPadding(new Insets(30));
        teacherPanel.setPrefWidth(280); // wider panel
        teacherPanel.setMaxWidth(280);
        teacherPanel.setBackground(new Background(
                new BackgroundFill(Color.rgb(162, 184, 172, 0.8), new CornerRadii(15), Insets.EMPTY)
        ));

        Image teacherIcon = new Image(getClass().getResourceAsStream("/Teacher.png"));
        ImageView teacherIconView = new ImageView(teacherIcon);
        teacherIconView.setFitWidth(90);
        teacherIconView.setFitHeight(90);

        Label teacherLabel = new Label("I am a Teacher");
        teacherLabel.setFont(Font.font("KyivType Sans", FontWeight.BOLD, 16));

        HBox usernameBox = new HBox(10);
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        Label usernameLabel = new Label("Username");
        usernameLabel.setPrefWidth(80);
        TextField teacherUsername = new TextField();
        teacherUsername.setPrefWidth(140);
        usernameBox.getChildren().addAll(usernameLabel, teacherUsername);

        HBox passwordBox = new HBox(10);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        Label passwordLabel = new Label("Password");
        passwordLabel.setPrefWidth(80);
        PasswordField teacherPassword = new PasswordField();
        teacherPassword.setPrefWidth(140);
        passwordBox.getChildren().addAll(passwordLabel, teacherPassword);

        Hyperlink teacherForgot = new Hyperlink("forgot password?");
        teacherForgot.setTextFill(Color.web("#1976D2"));

        Button teacherLogin = new Button("Log in");
        teacherLogin.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-background-radius: 15;");
        teacherLogin.setPrefWidth(180);

        // Teacher dashboard navigation
        teacherLogin.setOnAction(e -> {
            TeacherDashboard dashboard = new TeacherDashboard(
                    "Mr. Matti Valovirta",
                    "mattiv@metropolia.com",
                    "/Teacher.png"
            );
            Scene teacherScene = new Scene(dashboard, 1100, 700);
            teacherScene.getStylesheets().add(
                    getClass().getResource("/css/app.css").toExternalForm()
            );
            teacherScene.getStylesheets().add(
                    getClass().getResource("/css/teacher.css").toExternalForm()
            );
            stage.setTitle("Teacher Dashboard");
            stage.setScene(teacherScene);
        });

        teacherPanel.getChildren().addAll(
                teacherIconView, teacherLabel, usernameBox, passwordBox, teacherForgot, teacherLogin
        );

        // ================= STUDENT PANEL =================
        VBox studentPanel = new VBox(18);
        studentPanel.setAlignment(Pos.TOP_CENTER);
        studentPanel.setPadding(new Insets(30));
        studentPanel.setPrefWidth(280);
        studentPanel.setMaxWidth(280);
        studentPanel.setBackground(new Background(
                new BackgroundFill(Color.rgb(162, 184, 172, 0.8), new CornerRadii(15), Insets.EMPTY)
        ));

        Image studentIcon = new Image(getClass().getResourceAsStream("/Student.png"));
        ImageView studentIconView = new ImageView(studentIcon);
        studentIconView.setFitWidth(90);
        studentIconView.setFitHeight(90);

        Label studentLabel = new Label("I am a Student");
        studentLabel.setFont(Font.font("KyivType Sans", FontWeight.BOLD, 16));

        HBox studentUsernameBox = new HBox(10);
        studentUsernameBox.setAlignment(Pos.CENTER_LEFT);
        Label studentUsernameLabel = new Label("Username");
        studentUsernameLabel.setPrefWidth(80);
        TextField studentUsername = new TextField();
        studentUsername.setPrefWidth(140);
        studentUsernameBox.getChildren().addAll(studentUsernameLabel, studentUsername);

        HBox studentPasswordBox = new HBox(10);
        studentPasswordBox.setAlignment(Pos.CENTER_LEFT);
        Label studentPasswordLabel = new Label("Password");
        studentPasswordLabel.setPrefWidth(80);
        PasswordField studentPassword = new PasswordField();
        studentPassword.setPrefWidth(140);
        studentPasswordBox.getChildren().addAll(studentPasswordLabel, studentPassword);

        Hyperlink studentForgot = new Hyperlink("forgot password?");
        studentForgot.setTextFill(Color.web("#1976D2"));

        Button studentLogin = new Button("Log in");
        studentLogin.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-background-radius: 15;");
        studentLogin.setPrefWidth(180);

        // Student dashboard navigation
        studentLogin.setOnAction(e -> {
            StudentDashboard dashboard = new StudentDashboard(
                    "Bao Tran",
                    "bao@student.com",
                    "/Student.png"
            );
            Scene studentScene = new Scene(dashboard, 1100, 700);
            studentScene.getStylesheets().add(
                    getClass().getResource("/css/app.css").toExternalForm()
            );
            studentScene.getStylesheets().add(
                    getClass().getResource("/css/student.css").toExternalForm()
            );
            stage.setTitle("Student Dashboard");
            stage.setScene(studentScene);
        });

        studentPanel.getChildren().addAll(
                studentIconView, studentLabel, studentUsernameBox,
                studentPasswordBox, studentForgot, studentLogin
        );

        // ================= CENTER CONTENT =================
        HBox panelsBox = new HBox(40); // more spacing between panels
        panelsBox.setAlignment(Pos.CENTER);
        panelsBox.getChildren().addAll(teacherPanel, studentPanel);

        VBox mainContent = new VBox(40);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(30, 20, 20, 20));
        mainContent.getChildren().addAll(panelsBox);

        // Shift login panels slightly to the right
        mainContent.setTranslateX(50);

        StackPane root = new StackPane();
        root.getChildren().addAll(bgImageView, mainContent);

        bgImageView.fitWidthProperty().bind(root.widthProperty());
        bgImageView.fitHeightProperty().bind(root.heightProperty());

        return new Scene(root, 750, 550); // slightly bigger window
    }
}
