package Frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
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
