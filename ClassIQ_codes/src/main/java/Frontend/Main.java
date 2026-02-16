package Frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final double APP_WIDTH = 1300;
    public static final double APP_HEIGHT = 800;

    @Override
    public void start(Stage stage) {

        // Fixed window size
        stage.setWidth(APP_WIDTH);
        stage.setHeight(APP_HEIGHT);
        stage.setMinWidth(APP_WIDTH);
        stage.setMinHeight(APP_HEIGHT);
        stage.setMaxWidth(APP_WIDTH);
        stage.setMaxHeight(APP_HEIGHT);
        stage.setResizable(false);

        stage.setTitle("Class iQ");

        // Start with HomePage
        HomePage homePage = new HomePage();
        Scene scene = homePage.getScene(stage);

        // Apply background to the WHOLE SCENE using CSS (cannot be removed by setRoot)
        scene.getRoot().setStyle(
                "-fx-background-image: url('/Homepage.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center center;" +
                        "-fx-background-repeat: no-repeat;"
        );

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
