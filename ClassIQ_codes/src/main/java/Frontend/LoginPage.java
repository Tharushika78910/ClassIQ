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
import javafx.scene.layout.CornerRadii;

public class LoginPage {

        public Scene getScene(Stage stage) {

                // Background Image
                Image bgImage = new Image(getClass().getResourceAsStream("/Login.png"));
                ImageView bgImageView = new ImageView(bgImage);
                bgImageView.setPreserveRatio(false);

                /*  teacher panel  */

                VBox teacherPanel = new VBox(12);
                teacherPanel.setAlignment(Pos.TOP_CENTER);
                teacherPanel.setPadding(new Insets(15));
                teacherPanel.setPrefWidth(200);
                teacherPanel.setMaxWidth(200);
                teacherPanel.setBackground(new Background(
                        new BackgroundFill(Color.rgb(162, 184, 172, 0.8), new CornerRadii(15), Insets.EMPTY)
                ));

                Image teacherIcon = new Image(getClass().getResourceAsStream("/Teacher.png"));
                ImageView teacherIconView = new ImageView(teacherIcon);
                teacherIconView.setFitWidth(70);
                teacherIconView.setFitHeight(70);

                Label teacherLabel = new Label("I am a Teacher");
                teacherLabel.setFont(Font.font("KyivType Sans", 14));

                HBox usernameBox = new HBox(8);
                usernameBox.setAlignment(Pos.CENTER_LEFT);
                Label usernameLabel = new Label("Username");
                usernameLabel.setPrefWidth(70);
                TextField teacherUsername = new TextField();
                teacherUsername.setPrefWidth(100);
                usernameBox.getChildren().addAll(usernameLabel, teacherUsername);

                HBox passwordBox = new HBox(8);
                passwordBox.setAlignment(Pos.CENTER_LEFT);
                Label passwordLabel = new Label("Password");
                passwordLabel.setPrefWidth(70);
                PasswordField teacherPassword = new PasswordField();
                teacherPassword.setPrefWidth(100);
                passwordBox.getChildren().addAll(passwordLabel, teacherPassword);

                Hyperlink teacherForgot = new Hyperlink("forgot password?");
                teacherForgot.setTextFill(Color.web("#1976D2"));

                Button teacherLogin = new Button("Log in");
                teacherLogin.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-background-radius: 15;");
                teacherLogin.setPrefWidth(120);

                //  Teacher dashboard navigation (ONLY NEW LOGIC)
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

                /* student panel*/

                VBox studentPanel = new VBox(12);
                studentPanel.setAlignment(Pos.TOP_CENTER);
                studentPanel.setPadding(new Insets(15));
                studentPanel.setPrefWidth(200);
                studentPanel.setMaxWidth(200);
                studentPanel.setBackground(new Background(
                        new BackgroundFill(Color.rgb(162, 184, 172, 0.8), new CornerRadii(15), Insets.EMPTY)
                ));

                Image studentIcon = new Image(getClass().getResourceAsStream("/Student.png"));
                ImageView studentIconView = new ImageView(studentIcon);
                studentIconView.setFitWidth(70);
                studentIconView.setFitHeight(70);

                Label studentLabel = new Label("I am a Student");
                studentLabel.setFont(Font.font("KyivType Sans", 14));

                HBox studentUsernameBox = new HBox(8);
                studentUsernameBox.setAlignment(Pos.CENTER_LEFT);
                Label studentUsernameLabel = new Label("Username");
                studentUsernameLabel.setPrefWidth(70);
                TextField studentUsername = new TextField();
                studentUsername.setPrefWidth(100);
                studentUsernameBox.getChildren().addAll(studentUsernameLabel, studentUsername);

                HBox studentPasswordBox = new HBox(8);
                studentPasswordBox.setAlignment(Pos.CENTER_LEFT);
                Label studentPasswordLabel = new Label("Password");
                studentPasswordLabel.setPrefWidth(70);
                PasswordField studentPassword = new PasswordField();
                studentPassword.setPrefWidth(100);
                studentPasswordBox.getChildren().addAll(studentPasswordLabel, studentPassword);

                Hyperlink studentForgot = new Hyperlink("forgot password?");
                studentForgot.setTextFill(Color.web("#1976D2"));

                Button studentLogin = new Button("Log in");
                studentLogin.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-background-radius: 15;");
                studentLogin.setPrefWidth(120);

                // student dash board navigation
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

                /* center content  */

                Label titleLabel = new Label("Class iQ");
                titleLabel.setFont(Font.font("KyivType Sans", FontWeight.BOLD, 32));
                titleLabel.setTextFill(Color.BLACK);

                HBox panelsBox = new HBox(30);
                panelsBox.setAlignment(Pos.CENTER);
                panelsBox.getChildren().addAll(teacherPanel, studentPanel);

                VBox mainContent = new VBox(30);
                mainContent.setAlignment(Pos.CENTER);
                mainContent.setPadding(new Insets(30, 20, 20, 20));
                mainContent.getChildren().addAll(titleLabel, panelsBox);

                StackPane root = new StackPane();
                root.getChildren().addAll(bgImageView, mainContent);

                bgImageView.fitWidthProperty().bind(root.widthProperty());
                bgImageView.fitHeightProperty().bind(root.heightProperty());

                return new Scene(root, 600, 500);
        }
}
