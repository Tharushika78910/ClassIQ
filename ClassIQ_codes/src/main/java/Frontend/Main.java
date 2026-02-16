package Frontend;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static final double APP_WIDTH = 1300;
    public static final double APP_HEIGHT = 800;


    private static final String LOGO_PATH = "/logo_badge.png";

    private Scene firstHomeScene;

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

        // Start with HomePage (UNCHANGED)
        HomePage homePage = new HomePage();
        Scene homeScene = homePage.getScene(stage);
        firstHomeScene = homeScene;

        stage.setScene(homeScene);

        // Listen when Scene changes
        stage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) return;

            // Skip first HomePage scene
            if (newScene == firstHomeScene) return;

            wrapSceneRootWithLogo(newScene);

            newScene.rootProperty().addListener((o, oldRoot, newRoot) -> {
                if (newRoot == null) return;
                if (newRoot.getProperties().containsKey("LOGO_WRAPPED")) return;
                wrapSceneRootWithLogo(newScene);
            });
        });

        stage.show();
    }

    private void wrapSceneRootWithLogo(Scene scene) {

        Parent root = scene.getRoot();
        if (root == null) return;

        // Prevent double wrapping
        if (root.getProperties().containsKey("LOGO_WRAPPED")) return;

        ImageView smallLogo = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(LOGO_PATH))
        ));

        smallLogo.setPreserveRatio(true);
        smallLogo.setFitWidth(190);   // Adjust size if needed
        smallLogo.setSmooth(true);
        smallLogo.setMouseTransparent(true); // Don't block logout button

        // Soft shadow for better visibility
        smallLogo.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 12,0.3,0,2);"
        );

        StackPane wrapper = new StackPane(root, smallLogo);

        StackPane.setAlignment(smallLogo, Pos.BOTTOM_RIGHT);


        StackPane.setMargin(smallLogo, new Insets(0, 25, 90, 0));
        //  top right bottom left

        wrapper.getProperties().put("LOGO_WRAPPED", true);

        scene.setRoot(wrapper);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
