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

    public static final double APP_WIDTH = 1100;
    public static final double APP_HEIGHT = 700;

    private static final String LOGO_PATH = "/logo_badge.png";

    private Scene firstHomeScene;

    @Override
    public void start(Stage stage) {

        //  Default start size
        stage.setWidth(APP_WIDTH);
        stage.setHeight(APP_HEIGHT);

        // Allow resizing/maximizing
        stage.setResizable(true);

        // Optional: set a minimum size so layout doesn't break
        stage.setMinWidth(900);
        stage.setMinHeight(600);



        stage.setTitle("Class iQ");

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
        smallLogo.setFitWidth(190);
        smallLogo.setSmooth(true);
        smallLogo.setMouseTransparent(true);

        smallLogo.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 12,0.3,0,2);"
        );

        StackPane wrapper = new StackPane(root, smallLogo);

        StackPane.setAlignment(smallLogo, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(smallLogo, new Insets(0, 25, 90, 0));

        wrapper.getProperties().put("LOGO_WRAPPED", true);

        scene.setRoot(wrapper);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
