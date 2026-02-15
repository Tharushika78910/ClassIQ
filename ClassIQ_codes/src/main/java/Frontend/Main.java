package Frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // One size for the whole application
    public static final double APP_WIDTH = 1300;
    public static final double APP_HEIGHT = 800;

    @Override
    public void start(Stage stage) {

        //Force fixed window size ONCE here
        stage.setWidth(APP_WIDTH);
        stage.setHeight(APP_HEIGHT);
        stage.setMinWidth(APP_WIDTH);
        stage.setMinHeight(APP_HEIGHT);
        stage.setMaxWidth(APP_WIDTH);
        stage.setMaxHeight(APP_HEIGHT);
        stage.setResizable(false);

        HomePage homePage = new HomePage();
        Scene scene = homePage.getScene(stage);

        stage.setTitle("Class iQ");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
