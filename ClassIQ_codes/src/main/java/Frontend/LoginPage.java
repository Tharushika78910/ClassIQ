package Frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.CornerRadii;

public class LoginPage {
        public Scene getScene(Stage stage) {

        // Background Image
        Image bgImage = new Image(getClass().getResourceAsStream("/Login.png"));
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setPreserveRatio(true);
        bgImageView.setFitWidth(800);
        bgImageView.setFitHeight(600);

        // Teacher Panel
        VBox teacherPanel = new VBox(10);
        teacherPanel.setAlignment(Pos.TOP_CENTER);
        teacherPanel.setPadding(new Insets(20));
        teacherPanel.setPrefWidth(250);
        teacherPanel.setBackground(new Background(
                new BackgroundFill(Color.rgb(144, 238, 144, 0.8), new CornerRadii(15), Insets.EMPTY)
        ));

        // Teacher icon
        Image teacherIcon = new Image(getClass().getResourceAsStream("/Teacher.png"));
        ImageView teacherIconView = new ImageView(teacherIcon);
        teacherIconView.setFitWidth(80);
        teacherIconView.setFitHeight(80);

        Label teacherLabel = new Label("I am a Teacher");
        teacherLabel.setFont(Font.font("Arial", 16));

        TextField teacherUsername = new TextField();
        teacherUsername.setPromptText("Username");

        PasswordField teacherPassword = new PasswordField();
        teacherPassword.setPromptText("Password");

        Hyperlink teacherForgot = new Hyperlink("forgot password?");
        teacherForgot.setTextFill(Color.web("#1976D2"));
        teacherForgot.setUnderline(true);

        Button teacherLogin = new Button("Log in");
        teacherLogin.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-background-radius: 15;");
        teacherLogin.setPrefWidth(100);

        teacherPanel.getChildren().addAll(
                teacherIconView, teacherLabel, teacherUsername, teacherPassword, teacherForgot, teacherLogin
        );

        // Student Panel
        VBox studentPanel = new VBox(10);
        studentPanel.setAlignment(Pos.TOP_CENTER);
        studentPanel.setPadding(new Insets(20));
        studentPanel.setPrefWidth(250);
        studentPanel.setBackground(new Background(
                new BackgroundFill(Color.rgb(144, 238, 144, 0.8), new CornerRadii(15), Insets.EMPTY)
        ));

        Image studentIcon = new Image(getClass().getResourceAsStream("/Student.jpg"));
        ImageView studentIconView = new ImageView(studentIcon);
        studentIconView.setFitWidth(80);
        studentIconView.setFitHeight(80);

        Label studentLabel = new Label("I am a Student");
        studentLabel.setFont(Font.font("Arial", 16));

        TextField studentUsername = new TextField();
        studentUsername.setPromptText("Username");

        PasswordField studentPassword = new PasswordField();
        studentPassword.setPromptText("Password");

        Hyperlink studentForgot = new Hyperlink("forgot password?");
        studentForgot.setTextFill(Color.web("#1976D2"));
        studentForgot.setUnderline(true);

        Button studentLogin = new Button("Log in");
        studentLogin.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-background-radius: 15;");
        studentLogin.setPrefWidth(100);

        studentPanel.getChildren().addAll(
                studentIconView, studentLabel, studentUsername, studentPassword, studentForgot, studentLogin
        );

        // HBox to hold both panels
        HBox panelsBox = new HBox(20);
        panelsBox.setAlignment(Pos.CENTER);
        panelsBox.getChildren().addAll(teacherPanel, studentPanel);

        // Root StackPane
        StackPane root = new StackPane();
        root.getChildren().addAll(bgImageView, panelsBox);

        Scene scene = new Scene(root, 800, 600);
        return scene;
    }


}
