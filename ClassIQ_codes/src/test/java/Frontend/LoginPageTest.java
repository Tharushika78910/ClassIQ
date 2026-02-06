package Frontend;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginPageTest {

    @BeforeAll
    static void initJavaFX() {
        new JFXPanel();
    }

    @Test
    void getScene_basicTestForLoginPage() {
        Platform.runLater(() -> {
            // Create stage and page
            Stage stage = new Stage();
            LoginPage loginPage = new LoginPage();

            // Build scene
            Scene scene = loginPage.getScene(stage);

            // Basic checks
            assertNotNull(scene, "Scene should not be null");
            assertTrue(scene.getRoot() instanceof StackPane, "Root should be a StackPane");

            StackPane root = (StackPane) scene.getRoot();
            // bg image + main content
            assertEquals(2, root.getChildren().size(), "Root should have 2 children");

            // Second child is mainContent VBox
            assertTrue(root.getChildren().get(1) instanceof VBox, "Second child should be mainContent VBox");
            VBox mainContent = (VBox) root.getChildren().get(1);

            // mainContent- [titleLabel, panelsBox]
            assertTrue(mainContent.getChildren().size() >= 2, "Main content should have title and panels");
            assertTrue(mainContent.getChildren().get(1) instanceof HBox, "Panels container should be an HBox");
            HBox panelsBox = (HBox) mainContent.getChildren().get(1);

            // panelsBox- [teacherPanel, studentPanel]
            assertEquals(2, panelsBox.getChildren().size(), "Panels box should have 2 children");
            assertTrue(panelsBox.getChildren().get(0) instanceof VBox, "Teacher panel should be a VBox");
            assertTrue(panelsBox.getChildren().get(1) instanceof VBox, "Student panel should be a VBox");

            VBox teacherPanel = (VBox) panelsBox.getChildren().get(0);
            VBox studentPanel = (VBox) panelsBox.getChildren().get(1);

            // Find "Log in" buttons in each panel
            Button teacherLogin = findButtonByText(teacherPanel, "Log in");
            Button studentLogin = findButtonByText(studentPanel, "Log in");

            assertNotNull(teacherLogin, "Teacher panel should contain 'Log in' button");
            assertNotNull(studentLogin, "Student panel should contain 'Log in' button");

            // Click buttons to execute the handlers (navigation code)
            teacherLogin.fire();
            studentLogin.fire();
        });
    }

    private static Button findButtonByText(VBox panel, String text) {
        for (var node : panel.getChildren()) {
            if (node instanceof Button b && text.equals(b.getText())) {
                return b;
            }
        }
        return null;
    }
}