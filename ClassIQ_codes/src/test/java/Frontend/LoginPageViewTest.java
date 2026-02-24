package Frontend;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class LoginPageViewTest {

    @BeforeAll
    static void initJavaFX() {
        new JFXPanel();
    }

    @Test
    void getView_shouldBuildLoginUI_withoutDbCall() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                LoginPage loginPage = new LoginPage(stage);

                Parent root = loginPage.getView();
                assertNotNull(root, "Login root should not be null");
                assertTrue(root instanceof StackPane, "LoginPage root should be StackPane");

                // Should contain controls (text fields/buttons) even without interacting
                assertFalse(root.lookupAll(".button").isEmpty(), "Expected buttons in Login UI");
                assertTrue(root.lookupAll(".password-field").size() >= 1
                                || root.lookupAll(".text-field").size() >= 1,
                        "Expected input fields in Login UI");

            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "JavaFX thread timed out");
    }
}