package Frontend.teacher;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherMarkSheetPageTest {

    @BeforeAll
    static void initJavaFX() {
        new JFXPanel();
    }

    @Test
    void buildTable_shouldCreateExpectedColumns_withoutDb() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                TeacherMarkSheetPage page = new TeacherMarkSheetPage(null);

                // Call private buildTable() (avoids DB calls in getView)
                Method buildTable = TeacherMarkSheetPage.class.getDeclaredMethod("buildTable");
                buildTable.setAccessible(true);
                buildTable.invoke(page);

                // Access private field "table"
                Field tableField = TeacherMarkSheetPage.class.getDeclaredField("table");
                tableField.setAccessible(true);
                TableView<?> table = (TableView<?>) tableField.get(page);

                assertNotNull(table, "TableView should exist");
                assertEquals(7, table.getColumns().size(), "Expected 7 columns in marksheet table");

                assertEquals("Student No", table.getColumns().get(0).getText());
                assertEquals("Student Name", table.getColumns().get(1).getText());
                assertEquals("Assignment", table.getColumns().get(2).getText());
                assertEquals("Project", table.getColumns().get(3).getText());
                assertEquals("Final Exam", table.getColumns().get(4).getText());
                assertEquals("Total", table.getColumns().get(5).getText());
                assertEquals("Grade", table.getColumns().get(6).getText());

            } catch (Exception ex) {
                fail("TeacherMarkSheetPage buildTable test failed: " + ex.getMessage());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "JavaFX thread timed out");
    }
}