package Frontend;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HomePageTest {

    @BeforeAll
    static void initJavaFX() {
        // Initializes JavaFX runtime once for all tests
        new JFXPanel();
    }

    @Test
    void getScene_shouldCreateHomePageScene() throws InterruptedException {
        // Use a latch to wait for Platform.runLater to finish
        final var latch = new java.util.concurrent.CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // 1. Create a stage
                Stage stage = new Stage();

                // 2. Create HomePage instance
                HomePage homePage = new HomePage();

                // 3. Get scene from HomePage
                Scene scene = homePage.getScene(stage);

                // 4. Basic assertions

                // Scene should not be null
                assertNotNull(scene, "Scene should not be null");

                // Root should be StackPane
                assertTrue(scene.getRoot() instanceof StackPane, "Root should be a StackPane");

                StackPane root = (StackPane) scene.getRoot();

                // Root should contain at least one child
                assertTrue(root.getChildren().size() > 0, "Root should have at least 1 child");

            } finally {
                latch.countDown(); // signal test completion
            }
        });

        // Wait for JavaFX thread to finish
        latch.await();
    }
}
