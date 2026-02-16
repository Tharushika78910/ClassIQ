package Frontend.teacher;

import Backend.model.dao.impl.TeacherMarkSheetDaoImpl;
import Backend.service.WeightedMarkService;

import javafx.beans.property.*;
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

    private final TeacherDashboard dashboard;

    public TeacherMarkSheetPage(TeacherDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Parent getView() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("page-bg");

        /* ===========================
           CENTER CONTENT (CENTERED)
        ============================ */
        VBox centerBox = new VBox(15);
        centerBox.setPadding(new Insets(10));
        centerBox.setAlignment(Pos.TOP_CENTER); // ✅ Center everything

        Label subjectTitle = new Label("Mathematics");
        subjectTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        buildTable();

        // ✅ Reduce table width
        table.setMaxWidth(780);
        table.setPrefWidth(780);

        // ✅ Show 15 visible rows (grid)
        table.setFixedCellSize(28);
        table.setPrefHeight((28 * 15) + 30); // 15 rows + header

        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setMaxWidth(800); // ✅ container width smaller
        card.setPadding(new Insets(12));

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

            try {
                TeacherMarkSheetDaoImpl dao = new TeacherMarkSheetDaoImpl();
                int savedCount = 0;

                for (MarkRow r : table.getItems()) {

                    if (r.getAssignment() == 0 &&
                            r.getProject() == 0 &&
                            r.getFinalExam() == 0) {
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

                status.setText("Saved " + savedCount + " record(s) to database.");

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

        // ✅ keep card centered
        StackPane centeredCard = new StackPane(card);
        centeredCard.setAlignment(Pos.TOP_CENTER);

        centerBox.getChildren().addAll(subjectTitle, centeredCard);
        root.setCenter(centerBox);

        /* ===========================
           BOTTOM BUTTON BAR
        ============================ */
        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("secondary-btn");

        Button btnLogout = new Button("Logout");
        btnLogout.getStyleClass().add("logout-btn");

        btnBack.setOnAction(e -> dashboard.showHome());

        btnLogout.setOnAction(e ->
                dashboard.showPage(new Label("Logged out (placeholder)"))
        );

        AnchorPane bottomBar = new AnchorPane();
        bottomBar.setPadding(new Insets(15));

        AnchorPane.setLeftAnchor(btnBack, 20.0);
        AnchorPane.setBottomAnchor(btnBack, 10.0);

        AnchorPane.setRightAnchor(btnLogout, 20.0);
        AnchorPane.setBottomAnchor(btnLogout, 10.0);

        bottomBar.getChildren().addAll(btnBack, btnLogout);
        root.setBottom(bottomBar);

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

        // ✅ No hardcoded list here.
        // Keep your existing DB-loading code elsewhere in your project
        // that sets table items.
        // (If your project loads DB rows after getView(), it will still work.)
    }

    private TableColumn<MarkRow, Integer> editableMarkColumn(
            String title,
            int max,
            java.util.function.BiConsumer<MarkRow, Integer> setter
    ) {
        TableColumn<MarkRow, Integer> col = new TableColumn<>(title);
        col.setCellValueFactory(c -> c.getValue().getPropertyFor(title).asObject());
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

    /* ===========================
       INNER CLASSES
    ============================ */

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

        public StringProperty studentIdProperty() { return studentId; }
        public StringProperty studentNameProperty() { return studentName; }
        public IntegerProperty totalProperty() { return total; }
        public StringProperty gradeProperty() { return grade; }

        public int getAssignment() { return assignment.get(); }
        public int getProject() { return project.get(); }
        public int getFinalExam() { return finalExam.get(); }

        public void setAssignment(int v) { assignment.set(v); }
        public void setProject(int v) { project.set(v); }
        public void setFinalExam(int v) { finalExam.set(v); }

        public String getStudentId() { return studentId.get(); }
        public String getStudentName() { return studentName.get(); }
        public int getTotal() { return total.get(); }
        public String getGrade() { return grade.get(); }

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
        public String toString(Integer v) {
            return v == null ? "0" : v.toString();
        }

        @Override
        public Integer fromString(String s) {
            try {
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                return 0;
            }
        }
    }
}
