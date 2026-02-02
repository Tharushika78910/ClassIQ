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
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.CornerRadii;

public class LoginPage {
        public Scene getScene(Stage stage) {

        // Background Image
        Image bgImage = new Image(getClass().getResourceAsStream("/Login.png"));
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setPreserveRatio(false);

        // Teacher Panel
        VBox teacherPanel = new VBox(12);
        teacherPanel.setAlignment(Pos.TOP_CENTER);
        teacherPanel.setPadding(new Insets(15));
        teacherPanel.setPrefWidth(200);
        teacherPanel.setMaxWidth(200);
        teacherPanel.setBackground(new Background(
                new BackgroundFill(Color.rgb(162, 184, 172, 0.8), new CornerRadii(15), Insets.EMPTY)
        ));

        // Teacher icon
        Image teacherIcon = new Image(getClass().getResourceAsStream("/Teacher.png"));
        ImageView teacherIconView = new ImageView(teacherIcon);
        teacherIconView.setFitWidth(70);
        teacherIconView.setFitHeight(70);

        Label teacherLabel = new Label("I am a Teacher");
        teacherLabel.setFont(Font.font("KyivType Sans", 14));

        // Username field with label
        HBox usernameBox = new HBox(8);
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        Label usernameLabel = new Label("Username");
        usernameLabel.setPrefWidth(70);
        TextField teacherUsername = new TextField();
        teacherUsername.setPrefWidth(100);
        usernameBox.getChildren().addAll(usernameLabel, teacherUsername);

        // Password field with label
        HBox passwordBox = new HBox(8);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        Label passwordLabel = new Label("Password");
        passwordLabel.setPrefWidth(70);
        PasswordField teacherPassword = new PasswordField();
        teacherPassword.setPrefWidth(100);
        passwordBox.getChildren().addAll(passwordLabel, teacherPassword);

        Hyperlink teacherForgot = new Hyperlink("forgot password?");
        teacherForgot.setTextFill(Color.web("#1976D2"));
        teacherForgot.setUnderline(true);
        teacherForgot.setAlignment(Pos.CENTER_RIGHT);

        Button teacherLogin = new Button("Log in");
        teacherLogin.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-background-radius: 15;");
        teacherLogin.setPrefWidth(120);

        teacherPanel.getChildren().addAll(
                teacherIconView, teacherLabel, usernameBox, passwordBox, teacherForgot, teacherLogin
        );

        // Student Panel
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

        // Username field with label
        HBox studentUsernameBox = new HBox(8);
        studentUsernameBox.setAlignment(Pos.CENTER_LEFT);
        Label studentUsernameLabel = new Label("Username");
        studentUsernameLabel.setPrefWidth(70);
        TextField studentUsername = new TextField();
        studentUsername.setPrefWidth(100);
        studentUsernameBox.getChildren().addAll(studentUsernameLabel, studentUsername);

        // Password field with label
        HBox studentPasswordBox = new HBox(8);
        studentPasswordBox.setAlignment(Pos.CENTER_LEFT);
        Label studentPasswordLabel = new Label("Password");
        studentPasswordLabel.setPrefWidth(70);
        PasswordField studentPassword = new PasswordField();
        studentPassword.setPrefWidth(100);
        studentPasswordBox.getChildren().addAll(studentPasswordLabel, studentPassword);

        Hyperlink studentForgot = new Hyperlink("forgot password?");
        studentForgot.setTextFill(Color.web("#1976D2"));
        studentForgot.setUnderline(true);
        studentForgot.setAlignment(Pos.CENTER_RIGHT);

        Button studentLogin = new Button("Log in");
        studentLogin.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-background-radius: 15;");
        studentLogin.setPrefWidth(120);

        studentPanel.getChildren().addAll(
                studentIconView, studentLabel, studentUsernameBox, studentPasswordBox, studentForgot, studentLogin
        );

        // Title "Class iQ"
        Label titleLabel = new Label("Class iQ");
        titleLabel.setFont(Font.font("KyivType Sans", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.BLACK);

        // HBox to hold both panels
        HBox panelsBox = new HBox(30);
        panelsBox.setAlignment(Pos.CENTER);
        panelsBox.getChildren().addAll(teacherPanel, studentPanel);

        //  Main content container
        VBox mainContent = new VBox(30);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(30, 20, 20, 20));
        mainContent.getChildren().addAll(titleLabel, panelsBox);

        // Root StackPane
        StackPane root = new StackPane();
        root.getChildren().addAll(bgImageView, mainContent);
        
        // Bind background image to root size so it scales properly
        bgImageView.fitWidthProperty().bind(root.widthProperty());
        bgImageView.fitHeightProperty().bind(root.heightProperty());

        Scene scene = new Scene(root, 600, 500);
        return scene;
    }


}
