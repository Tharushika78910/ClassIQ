package Frontend.teacher;

import Backend.model.dao.impl.TeacherMarkSheetDaoImpl;
import Backend.service.WeightedMarkService;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

public class TeacherMarkSheetPage {

    private static final WeightedMarkService weightedMarkService = new WeightedMarkService();

    private final TableView<MarkRow> table = new TableView<>();
    private final Label status = new Label("");

    public Parent getView() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("page-bg");

        Label title = new Label("Mark Sheet");
        title.getStyleClass().add("title-xl");

        Label subtitle = new Label(
                "Class: 10A   |   Term: 1   |   Subject: Mathematics\n" +
                        "Assignment /20   Project /30   Final Exam /50"
        );
        subtitle.getStyleClass().add("subtitle");

        VBox card = new VBox(12);
        card.getStyleClass().add("card");

        buildTable();

        Button btnSave = new Button("Save");
        btnSave.getStyleClass().add("primary-btn");

        Button btnClear = new Button("Clear");
        btnClear.getStyleClass().add("secondary-btn");

        status.getStyleClass().add("muted-text");

        btnSave.setOnAction(e -> {
            if (!allInputsValid()) {
                status.setText("Please enter valid marks before saving.");
                return;
            }


            if (Boolean.getBoolean("testMode")) {
                status.setText("Saved (test mode).");
                return;
            }

            try {
                TeacherMarkSheetDaoImpl dao = new TeacherMarkSheetDaoImpl();

                int savedCount = 0;

                for (MarkRow r : table.getItems()) {


                    if (r.getAssignment() == 0 && r.getProject() == 0 && r.getFinalExam() == 0) {
                        continue;
                    }

                    int dbStudentId = mapUiStudentToDbId(r.getStudentId());

                    dao.saveTeacherMarkSheetRow(
                            dbStudentId,
                            r.getStudentName(),
                            r.getAssignment(),
                            r.getProject(),
                            r.getFinalExam(),
                            r.getTotal(),
                            r.getGrade()
                    );

                    savedCount++;
                }

                status.setText("Saved " + savedCount + " record(s) to database (teacher_marksheet).");

            } catch (Exception ex) {
                status.setText("Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnClear.setOnAction(e -> {
            for (MarkRow r : table.getItems()) {
                r.setAssignment(0);
                r.setProject(0);
                r.setFinalExam(0);
            }
            status.setText("All marks cleared.");
        });

        HBox actions = new HBox(10, btnSave, btnClear, status);
        actions.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(table, actions);
        root.getChildren().addAll(title, subtitle, card);
        return root;
    }

    private int mapUiStudentToDbId(String uiStudentId) {
        return switch (uiStudentId) {
            case "S001" -> 201;
            case "S002" -> 202;
            case "S003" -> 203;
            case "S004" -> 204;
            case "S005" -> 205;
            default -> throw new IllegalArgumentException("No DB mapping for: " + uiStudentId);
        };
    }

    private void buildTable() {
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MarkRow, String> colId = new TableColumn<>("Student ID");
        colId.setCellValueFactory(c -> c.getValue().studentIdProperty());

        TableColumn<MarkRow, String> colName = new TableColumn<>("Student Name");
        colName.setCellValueFactory(c -> c.getValue().studentNameProperty());

        TableColumn<MarkRow, Integer> colAssignment =
                editableMarkColumn("Assignment", 20, MarkRow::setAssignment);

        TableColumn<MarkRow, Integer> colProject =
                editableMarkColumn("Project", 30, MarkRow::setProject);

        TableColumn<MarkRow, Integer> colFinal =
                editableMarkColumn("Final Exam", 50, MarkRow::setFinalExam);

        TableColumn<MarkRow, Integer> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(c -> c.getValue().totalProperty().asObject());

        TableColumn<MarkRow, String> colGrade = new TableColumn<>("Grade");
        colGrade.setCellValueFactory(c -> c.getValue().gradeProperty());

        table.getColumns().setAll(
                colId, colName,
                colAssignment, colProject, colFinal,
                colTotal, colGrade
        );

        table.setItems(fixedStudents());
    }

    private TableColumn<MarkRow, Integer> editableMarkColumn(
            String title,
            int max,
            java.util.function.BiConsumer<MarkRow, Integer> setter
    ) {
        TableColumn<MarkRow, Integer> col = new TableColumn<>(title);
        col.setCellValueFactory(c -> c.getValue().getPropertyFor(title).asObject());
        col.setEditable(true);

        col.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        col.setOnEditCommit(ev -> {
            Integer val = ev.getNewValue();
            if (val == null) val = 0;

            if (val < 0 || val > max) {
                status.setText(title + " must be between 0 and " + max);
                table.refresh();
                return;
            }

            setter.accept(ev.getRowValue(), val);
            status.setText("");
        });

        return col;
    }

    private boolean allInputsValid() {
        for (MarkRow r : table.getItems()) {
            if (r.getAssignment() < 0 || r.getAssignment() > 20) return false;
            if (r.getProject() < 0 || r.getProject() > 30) return false;
            if (r.getFinalExam() < 0 || r.getFinalExam() > 50) return false;
        }
        return true;
    }

    private ObservableList<MarkRow> fixedStudents() {
        return FXCollections.observableArrayList(
                new MarkRow("S001", "Poornima Jayamanna"),
                new MarkRow("S002", "Hathadura Chathurika Silva"),
                new MarkRow("S003", "Malmige Roshini"),
                new MarkRow("S004", "Kumudu Nalleperuma"),
                new MarkRow("S005", "Dilmi Rajapaksha"),
                new MarkRow("S006", "Saumya Sompala"),
                new MarkRow("S007", "Chani Anjalika")
        );
    }

    public static class MarkRow {
        private final StringProperty studentId = new SimpleStringProperty();
        private final StringProperty studentName = new SimpleStringProperty();

        private final IntegerProperty assignment = new SimpleIntegerProperty(0);
        private final IntegerProperty project = new SimpleIntegerProperty(0);
        private final IntegerProperty finalExam = new SimpleIntegerProperty(0);

        private final IntegerProperty total = new SimpleIntegerProperty();
        private final StringProperty grade = new SimpleStringProperty();

        public MarkRow(String id, String name) {
            studentId.set(id);
            studentName.set(name);

            assignment.addListener((o, a, b) -> recalc());
            project.addListener((o, a, b) -> recalc());
            finalExam.addListener((o, a, b) -> recalc());

            recalc();
        }

        private void recalc() {
            int t = assignment.get() + project.get() + finalExam.get();
            total.set(t);

            grade.set(weightedMarkService.gradeFromComponents(
                    assignment.get(), project.get(), finalExam.get()
            ));
        }

        public String getStudentId() { return studentId.get(); }
        public String getStudentName() { return studentName.get(); }

        public int getAssignment() { return assignment.get(); }
        public int getProject() { return project.get(); }
        public int getFinalExam() { return finalExam.get(); }

        public void setAssignment(int v) { assignment.set(v); }
        public void setProject(int v) { project.set(v); }
        public void setFinalExam(int v) { finalExam.set(v); }

        public int getTotal() { return total.get(); }
        public String getGrade() { return grade.get(); }

        public StringProperty studentIdProperty() { return studentId; }
        public StringProperty studentNameProperty() { return studentName; }
        public IntegerProperty totalProperty() { return total; }
        public StringProperty gradeProperty() { return grade; }

        public IntegerProperty getPropertyFor(String title) {
            return switch (title) {
                case "Assignment" -> assignment;
                case "Project" -> project;
                case "Final Exam" -> finalExam;
                default -> new SimpleIntegerProperty(0);
            };
        }
    }

    public static class IntegerStringConverter extends StringConverter<Integer> {
        @Override
        public String toString(Integer value) {
            return value == null ? "0" : value.toString();
        }

        @Override
        public Integer fromString(String s) {
            if (s == null || s.trim().isEmpty()) return 0;
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }
}
