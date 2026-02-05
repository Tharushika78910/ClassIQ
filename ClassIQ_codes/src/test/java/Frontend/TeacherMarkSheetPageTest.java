package Frontend;

import Frontend.teacher.TeacherMarkSheetPage;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMarkSheetPageTest {

    @BeforeAll
    static void initJavaFX() {
        // starts JavaFX runtime
        new JFXPanel();
    }

    // helper to run code on FX thread and wait
    private static void runOnFxAndWait(Runnable action) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        javafx.application.Platform.runLater(() -> {
            try { action.run(); }
            finally { latch.countDown(); }
        });
        latch.await();
    }

    @Test
    void getView_shouldBuildWithoutException() throws InterruptedException {
        runOnFxAndWait(() -> {
            TeacherMarkSheetPage page = new TeacherMarkSheetPage();
            Parent view = page.getView();
            assertNotNull(view);
        });
    }

    @Test
    void markRow_recalculatesTotalAndGrade_whenMarksChange() throws InterruptedException {
        runOnFxAndWait(() -> {
            TeacherMarkSheetPage.MarkRow row =
                    new TeacherMarkSheetPage.MarkRow("S001", "Poornima Jayamanna");

            // initial
            assertEquals(0, row.getTotal());
            assertEquals("F", row.getGrade());

            // 20 + 30 + 50 = 100 => A
            row.setAssignment(20);
            row.setProject(30);
            row.setFinalExam(50);

            assertEquals(100, row.getTotal());
            assertEquals("A", row.getGrade());

            // 10 + 20 + 20 = 50 => S (>=35 and <55)
            row.setAssignment(10);
            row.setProject(20);
            row.setFinalExam(20);

            assertEquals(50, row.getTotal());
            assertEquals("S", row.getGrade());
        });
    }

    @Test
    void markRow_gradeBoundaries_areCorrect() throws InterruptedException {
        runOnFxAndWait(() -> {
            TeacherMarkSheetPage.MarkRow row =
                    new TeacherMarkSheetPage.MarkRow("S002", "Test Student");

            // 75 => A
            row.setAssignment(20);
            row.setProject(30);
            row.setFinalExam(25);
            assertEquals(75, row.getTotal());
            assertEquals("A", row.getGrade());

            // 65 => B
            row.setFinalExam(15); // 20+30+15=65
            assertEquals(65, row.getTotal());
            assertEquals("B", row.getGrade());

            // 55 => C
            row.setFinalExam(5); // 20+30+5=55
            assertEquals(55, row.getTotal());
            assertEquals("C", row.getGrade());

            // 35 => S
            row.setAssignment(10);
            row.setProject(10);
            row.setFinalExam(15); // 35
            assertEquals(35, row.getTotal());
            assertEquals("S", row.getGrade());

            // 34 => F
            row.setFinalExam(14); // 10+10+14=34
            assertEquals(34, row.getTotal());
            assertEquals("F", row.getGrade());
        });
    }

    @Test
    void allInputsValid_returnsFalse_whenOutOfRange() throws Exception {
        // allInputsValid() is private, so we call it using reflection.
        // This is acceptable for student projects when you want to test private validation logic.

        TeacherMarkSheetPage page = new TeacherMarkSheetPage();

        // Build UI so table is initialized and items exist
        runOnFxAndWait(page::getView);

        Method m = TeacherMarkSheetPage.class.getDeclaredMethod("allInputsValid");
        m.setAccessible(true);

        // first should be valid (all zeros)
        boolean valid1 = (boolean) m.invoke(page);
        assertTrue(valid1);

        // make one row invalid: assignment > 20
        runOnFxAndWait(() -> {
            // access first row through inner MarkRow class by rebuilding a row reference
            // We can’t access the private table directly here, so we use reflection too.
            try {
                var tableField = TeacherMarkSheetPage.class.getDeclaredField("table");
                tableField.setAccessible(true);
                var table = (javafx.scene.control.TableView<TeacherMarkSheetPage.MarkRow>) tableField.get(page);
                table.getItems().get(0).setAssignment(21); // invalid
            } catch (Exception ex) {
                fail("Reflection access failed: " + ex.getMessage());
            }
        });

        boolean valid2 = (boolean) m.invoke(page);
        assertFalse(valid2);
    }
}
