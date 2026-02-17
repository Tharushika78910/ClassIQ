package Frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final double APP_WIDTH = 1100;
    public static final double APP_HEIGHT = 700;

    @Override
    public void start(Stage stage) {

        // Default start size
        stage.setWidth(APP_WIDTH);
        stage.setHeight(APP_HEIGHT);

        // Allow resizing/maximizing
        stage.setResizable(true);

        // Optional minimum size
        stage.setMinWidth(900);
        stage.setMinHeight(600);

        stage.setTitle("Class iQ");

        // Load HomePage normally
        HomePage homePage = new HomePage();
        Scene homeScene = homePage.getScene(stage);

        stage.setScene(homeScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
