package FrontendTests;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginPageTest {

    @BeforeAll
    static void initJavaFX() {
        // Initializes JavaFX runtime so we can test JavaFX components
        new JFXPanel();
    }

    @Test
    void getScene_shouldCreateLoginPageScene() {
        Platform.runLater(() -> {
            // 1. Create a stage
            Stage stage = new Stage();

            // 2. Create LoginPage instance
            LoginPage loginPage = new LoginPage();

            // 3. Get scene from LoginPage
            Scene scene = loginPage.getScene(stage);

            // 4. Assertions

            // Scene should not be null
            assertNotNull(scene, "Scene should not be null");

            // Root should be StackPane
            assertTrue(scene.getRoot() instanceof StackPane, "Root should be a StackPane");

            // Root should contain exactly 2 children: background image + HBox
            StackPane root = (StackPane) scene.getRoot();
            assertEquals(2, root.getChildren().size(), "Root should have 2 children");

            // Second child should be HBox (panelsBox)
            assertTrue(root.getChildren().get(1) instanceof HBox, "Second child should be HBox");

            HBox panelsBox = (HBox) root.getChildren().get(1);

            // HBox should contain exactly 2 children: teacher panel + student panel
            assertEquals(2, panelsBox.getChildren().size(), "HBox should have 2 children");
        });
    }
}
