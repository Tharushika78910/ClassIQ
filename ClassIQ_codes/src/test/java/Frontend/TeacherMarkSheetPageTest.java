package Frontend;

import Frontend.teacher.TeacherMarkSheetPage;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMarkSheetPageTest {

    @BeforeAll
    static void initJavaFX() {

        new JFXPanel();
    }

    private static void runOnFxAndWait(Runnable action) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try { action.run(); }
            finally { latch.countDown(); }
        });
        latch.await();
    }

    @SuppressWarnings("unchecked")
    private static TableView<TeacherMarkSheetPage.MarkRow> getTable(TeacherMarkSheetPage page) {
        try {
            Field f = TeacherMarkSheetPage.class.getDeclaredField("table");
            f.setAccessible(true);
            return (TableView<TeacherMarkSheetPage.MarkRow>) f.get(page);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access table field: " + e.getMessage(), e);
        }
    }

    private static Button findButtonByText(Parent root, String text) {
        for (Node n : root.lookupAll(".button")) {
            if (n instanceof Button b && text.equals(b.getText())) return b;
        }
        return null;
    }

    @Test
    void getView_buildsTableAndButtons() throws InterruptedException {
        runOnFxAndWait(() -> {
            TeacherMarkSheetPage page = new TeacherMarkSheetPage();
            Parent view = page.getView();

            assertNotNull(view);

            Button save = findButtonByText(view, "Save");
            Button clear = findButtonByText(view, "Clear");

            assertNotNull(save, "Save button should exist");
            assertNotNull(clear, "Clear button should exist");

            TableView<TeacherMarkSheetPage.MarkRow> table = getTable(page);
            assertNotNull(table);

            assertEquals(7, table.getItems().size(), "Should have 7 fixed students");
            assertEquals(7, table.getColumns().size(), "Should have 7 columns");
            assertTrue(table.isEditable(), "Table should be editable");
        });
    }

    @Test
    void saveButton_runsValidationFailBranch_whenOutOfRange() throws InterruptedException {

        System.setProperty("testMode", "true");

        try {
            runOnFxAndWait(() -> {
                TeacherMarkSheetPage page = new TeacherMarkSheetPage();
                Parent view = page.getView();

                TableView<TeacherMarkSheetPage.MarkRow> table = getTable(page);


                table.getItems().get(0).setAssignment(21);

                Button save = findButtonByText(view, "Save");
                assertNotNull(save);

                assertDoesNotThrow(save::fire);
            });
        } finally {
            System.clearProperty("testMode");
        }
    }

    @Test
    void saveButton_runsSuccessBranch_whenAllValid_testMode() throws InterruptedException {
        System.setProperty("testMode", "true");

        try {
            runOnFxAndWait(() -> {
                TeacherMarkSheetPage page = new TeacherMarkSheetPage();
                Parent view = page.getView();

                TableView<TeacherMarkSheetPage.MarkRow> table = getTable(page);

                var r0 = table.getItems().get(0);
                r0.setAssignment(20);
                r0.setProject(30);
                r0.setFinalExam(50);

                Button save = findButtonByText(view, "Save");
                assertNotNull(save);

                assertDoesNotThrow(save::fire);

                assertEquals(100, r0.getTotal());
                assertEquals("A", r0.getGrade());
            });
        } finally {
            System.clearProperty("testMode");
        }
    }

    @Test
    void clearButton_resetsAllMarksToZero() throws InterruptedException {

        System.setProperty("testMode", "true");

        try {
            runOnFxAndWait(() -> {
                TeacherMarkSheetPage page = new TeacherMarkSheetPage();
                Parent view = page.getView();

                TableView<TeacherMarkSheetPage.MarkRow> table = getTable(page);

                var r0 = table.getItems().get(0);
                r0.setAssignment(10);
                r0.setProject(10);
                r0.setFinalExam(10);
                assertEquals(30, r0.getTotal());

                Button clear = findButtonByText(view, "Clear");
                assertNotNull(clear);

                clear.fire();

                assertEquals(0, r0.getAssignment());
                assertEquals(0, r0.getProject());
                assertEquals(0, r0.getFinalExam());
                assertEquals(0, r0.getTotal());
            });
        } finally {
            System.clearProperty("testMode");
        }
    }

    @Test
    void editCommit_invalidValue_hitsRefreshBranch() throws InterruptedException {
        System.setProperty("testMode", "true");

        try {
            runOnFxAndWait(() -> {
                TeacherMarkSheetPage page = new TeacherMarkSheetPage();
                page.getView();

                TableView<TeacherMarkSheetPage.MarkRow> table = getTable(page);

                @SuppressWarnings("unchecked")
                TableColumn<TeacherMarkSheetPage.MarkRow, Integer> assignmentCol =
                        (TableColumn<TeacherMarkSheetPage.MarkRow, Integer>) table.getColumns().get(2);

                TablePosition<TeacherMarkSheetPage.MarkRow, Integer> pos =
                        new TablePosition<>(table, 0, assignmentCol);


                TableColumn.CellEditEvent<TeacherMarkSheetPage.MarkRow, Integer> ev =
                        new TableColumn.CellEditEvent<>(table, pos, TableColumn.editCommitEvent(), 999);

                assertDoesNotThrow(() -> assignmentCol.getOnEditCommit().handle(ev));


                assertNotEquals(999, table.getItems().get(0).getAssignment());
            });
        } finally {
            System.clearProperty("testMode");
        }
    }

    @Test
    void editCommit_validValue_updatesRow() throws InterruptedException {
        System.setProperty("testMode", "true");

        try {
            runOnFxAndWait(() -> {
                TeacherMarkSheetPage page = new TeacherMarkSheetPage();
                page.getView();

                TableView<TeacherMarkSheetPage.MarkRow> table = getTable(page);

                @SuppressWarnings("unchecked")
                TableColumn<TeacherMarkSheetPage.MarkRow, Integer> projectCol =
                        (TableColumn<TeacherMarkSheetPage.MarkRow, Integer>) table.getColumns().get(3);

                TablePosition<TeacherMarkSheetPage.MarkRow, Integer> pos =
                        new TablePosition<>(table, 0, projectCol);

                TableColumn.CellEditEvent<TeacherMarkSheetPage.MarkRow, Integer> ev =
                        new TableColumn.CellEditEvent<>(table, pos, TableColumn.editCommitEvent(), 30);

                projectCol.getOnEditCommit().handle(ev);

                assertEquals(30, table.getItems().get(0).getProject());
            });
        } finally {
            System.clearProperty("testMode");
        }
    }

    @Test
    void markRow_gradeBranches_areCovered() throws InterruptedException {
        runOnFxAndWait(() -> {
            TeacherMarkSheetPage.MarkRow row = new TeacherMarkSheetPage.MarkRow("S999", "Test");


            row.setAssignment(0);
            row.setProject(0);
            row.setFinalExam(34);
            assertEquals("F", row.getGrade());


            row.setFinalExam(35);
            assertEquals("S", row.getGrade());


            row.setFinalExam(55);
            assertEquals("C", row.getGrade());


            row.setFinalExam(65);
            assertEquals("B", row.getGrade());


            row.setFinalExam(75);
            assertEquals("A", row.getGrade());
        });
    }

    @Test
    void converter_and_getPropertyFor_defaultBranch() throws InterruptedException {
        runOnFxAndWait(() -> {
            TeacherMarkSheetPage.IntegerStringConverter c = new TeacherMarkSheetPage.IntegerStringConverter();
            assertEquals("0", c.toString(null));
            assertEquals(0, c.fromString(""));
            assertEquals(0, c.fromString("   "));
            assertEquals(0, c.fromString("abc"));
            assertEquals(12, c.fromString("12"));

            TeacherMarkSheetPage.MarkRow row = new TeacherMarkSheetPage.MarkRow("S1", "X");
            assertNotNull(row.getPropertyFor("NOT_A_REAL_COLUMN"));
            assertEquals(0, row.getPropertyFor("NOT_A_REAL_COLUMN").get());
        });
    }


    @Test
    void markRow_getters_and_properties_areCovered() {
        TeacherMarkSheetPage.MarkRow row = new TeacherMarkSheetPage.MarkRow("S1", "Test");


        assertEquals("S1", row.getStudentId());
        assertEquals("Test", row.getStudentName());


        assertNotNull(row.studentIdProperty());
        assertNotNull(row.studentNameProperty());
        assertNotNull(row.totalProperty());
        assertNotNull(row.gradeProperty());
    }

    @Test
    void buildTable_columnValueFactories_execute_lambdas() throws InterruptedException {
        runOnFxAndWait(() -> {
            TeacherMarkSheetPage page = new TeacherMarkSheetPage();
            page.getView();

            TableView<TeacherMarkSheetPage.MarkRow> table = getTable(page);


            for (TableColumn<?, ?> col : table.getColumns()) {
                assertNotNull(col.getCellValueFactory(), "Each column should have a cellValueFactory");
                var value = col.getCellValueFactory().call(
                        new TableColumn.CellDataFeatures<>(table, (TableColumn) col, table.getItems().get(0))
                );
                assertNotNull(value);
            }
        });
    }

    @Test
    void mapUiStudentToDbId_branches_ifYourProductionReturnsMinusOneForUnknown() throws Exception {

        TeacherMarkSheetPage page = new TeacherMarkSheetPage();

        Method m = TeacherMarkSheetPage.class.getDeclaredMethod("mapUiStudentToDbId", String.class);
        m.setAccessible(true);

        assertEquals(201, (int) m.invoke(page, "S001"));
        assertEquals(202, (int) m.invoke(page, "S002"));
        assertEquals(203, (int) m.invoke(page, "S003"));
        assertEquals(204, (int) m.invoke(page, "S004"));
        assertEquals(205, (int) m.invoke(page, "S005"));


    }
}
