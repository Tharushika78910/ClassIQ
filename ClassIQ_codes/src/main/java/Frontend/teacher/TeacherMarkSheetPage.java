package Frontend.teacher;

import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.dao.impl.TeacherMarkSheetDaoImpl;
import Backend.model.dao.impl.StudentMarksDaoImpl;
import Backend.model.entity.Student;
import Backend.service.WeightedMarkService;
import Frontend.LoginPage;
import Frontend.Session;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Map;

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

        // Main centered content
        VBox centerBox = new VBox(16);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setFillWidth(false);

        String subject = Session.getTeacherSubject();
        if (subject == null || subject.isBlank()) subject = "Mathematics";

        Label subjectTitle = new Label(subject);
        subjectTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: #1f2d2a;");

        buildTable();

        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER);
        card.setFillWidth(false);

        // center table
        card.setPrefWidth(table.getPrefWidth() + 40);
        card.setMaxWidth(Region.USE_PREF_SIZE);

        Button btnSave = new Button("Save");
        btnSave.getStyleClass().add("primary-btn");

        Button btnClear = new Button("Clear");
        btnClear.getStyleClass().add("secondary-btn");

        status.getStyleClass().add("muted-text");

        loadStudentsAndMarks();

        // SAVE BUTTON (FIXED)

        btnSave.setOnAction(e -> {
            status.setText("");

            if (!Session.isTeacherLoggedIn()) {
                status.setText("Teacher session not found.");
                return;
            }

            if (!allInputsValid()) {
                status.setText("Please enter valid marks before saving.");
                return;
            }

            int teacherId = Session.getTeacherId();
            String subj = Session.getTeacherSubject();
            if (subj == null || subj.isBlank()) subj = "Mathematics";

            try {
                TeacherMarkSheetDaoImpl dao = new TeacherMarkSheetDaoImpl();
                StudentMarksDaoImpl studentMarksDao = new StudentMarksDaoImpl();

                int savedCount = 0;

                for (MarkRow r : table.getItems()) {
                    if (r.getAssignment() == 0 && r.getProject() == 0 && r.getFinalExam() == 0) continue;

                    // Save to teacher_marksheet
                    dao.saveTeacherMarkSheetRow(
                            r.getStudentDbId(),
                            r.getStudentName(),
                            r.getAssignment(),
                            r.getProject(),
                            r.getFinalExam(),
                            r.getTotal(),
                            r.getGrade(),
                            teacherId,
                            subj
                    );

                    // Save subject total to student_marks
                    studentMarksDao.upsertSubjectTotal(
                            r.getStudentDbId(),
                            subj,
                            r.getTotal()
                    );

                    savedCount++;
                }

                status.setText("Saved " + savedCount + " record(s).");
                loadStudentsAndMarks();

            } catch (Exception ex) {
                status.setText("Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // CLEAR BUTTON

        btnClear.setOnAction(e -> {
            status.setText("");

            if (!Session.isTeacherLoggedIn()) {
                status.setText("Teacher session not found.");
                return;
            }

            int teacherId = Session.getTeacherId();
            String subj = Session.getTeacherSubject();
            if (subj == null || subj.isBlank()) subj = "Mathematics";

            try {
                TeacherMarkSheetDaoImpl dao = new TeacherMarkSheetDaoImpl();
                StudentMarksDaoImpl studentMarksDao = new StudentMarksDaoImpl();

                // 1) Delete teacher_marksheet rows for this teacher + subject
                int deleted = dao.deleteMarksForTeacherSubject(teacherId, subj);

                // 2) Clear subject totals in student_marks and recalc total/average
                int updated = studentMarksDao.clearSubjectForAllStudents(subj);

                status.setText("All marks cleared. Deleted " + deleted +
                        " teacher record(s). Reset " + updated + " student_marks row(s).");

                loadStudentsAndMarks();

            } catch (Exception ex) {
                status.setText("Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        HBox actions = new HBox(10, btnSave, btnClear, status);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.setPadding(new Insets(2, 0, 0, 0));

        card.getChildren().addAll(table, actions);
        centerBox.getChildren().addAll(subjectTitle, card);

        // CENTER in the BorderPane
        StackPane centerWrap = new StackPane(centerBox);
        centerWrap.setAlignment(Pos.CENTER);
        root.setCenter(centerWrap);

        // BOTTOM BUTTON BAR
        String pillNormal =
                "-fx-background-color: rgba(255,255,255,0.92);" +
                        "-fx-text-fill: #2E6F62;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10,0,0,2);";

        String pillHover =
                "-fx-background-color: #9AC4B7;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;";

        Button btnBack = new Button("← Back");
        btnBack.setStyle(pillNormal);
        btnBack.setOnMouseEntered(e -> btnBack.setStyle(pillHover));
        btnBack.setOnMouseExited(e -> btnBack.setStyle(pillNormal));
        btnBack.setOnAction(e -> dashboard.showHome());

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle(pillNormal);
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle(pillHover));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle(pillNormal));
        btnLogout.setOnAction(e -> {
            Session.clear();
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new LoginPage(stage).getScene());
        });

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

    private void loadStudentsAndMarks() {

        if (!Session.isTeacherLoggedIn()) return;

        int teacherId = Session.getTeacherId();
        String subj = Session.getTeacherSubject();
        if (subj == null || subj.isBlank()) subj = "Mathematics";

        try {
            StudentDaoImpl studentDao = new StudentDaoImpl();
            List<Student> students = studentDao.findAll();

            ObservableList<MarkRow> rows = FXCollections.observableArrayList();

            for (Student s : students) {
                String fullName = s.getFirstName() + " " + s.getLastName();
                rows.add(new MarkRow(
                        s.getStudentId(),
                        s.getStudentNumber(),
                        fullName
                ));
            }

            TeacherMarkSheetDaoImpl markDao = new TeacherMarkSheetDaoImpl();
            Map<Integer, int[]> existing = markDao.loadExistingMarks(teacherId, subj);

            for (MarkRow r : rows) {
                int[] m = existing.get(r.getStudentDbId());
                if (m != null) {
                    r.setAssignment(m[0]);
                    r.setProject(m[1]);
                    r.setFinalExam(m[2]);
                } else {
                    r.setAssignment(0);
                    r.setProject(0);
                    r.setFinalExam(0);
                }
            }

            table.setItems(rows);

        } catch (Exception ex) {
            status.setText("Load error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void buildTable() {

        table.setEditable(true);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Row height
        table.setFixedCellSize(42);

        table.setRowFactory(tv -> {
            TableRow<MarkRow> row = new TableRow<>();
            row.setStyle("-fx-cell-size: 42px;");
            return row;
        });

        TableColumn<MarkRow, String> colNo = new TableColumn<>("Student No");
        colNo.setCellValueFactory(c -> c.getValue().studentNumberProperty());

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

        colNo.setPrefWidth(110);
        colName.setPrefWidth(280);

        // Tight numeric columns
        colAssignment.setPrefWidth(95);
        colProject.setPrefWidth(85);
        colFinal.setPrefWidth(110);
        colTotal.setPrefWidth(70);
        colGrade.setPrefWidth(75);

        // Alignment: name left, numbers centered
        colNo.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 6 0 6;");
        colName.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 6 0 6;");

        colAssignment.setStyle("-fx-alignment: CENTER; -fx-padding: 0 0 0 0;");
        colProject.setStyle("-fx-alignment: CENTER; -fx-padding: 0 0 0 0;");
        colFinal.setStyle("-fx-alignment: CENTER; -fx-padding: 0 0 0 0;");
        colTotal.setStyle("-fx-alignment: CENTER; -fx-padding: 0 0 0 0;");
        colGrade.setStyle("-fx-alignment: CENTER; -fx-padding: 0 0 0 0;");

        table.getColumns().setAll(
                colNo, colName,
                colAssignment, colProject, colFinal,
                colTotal, colGrade
        );

        // Table width = sum of columns
        double tableW =
                colNo.getPrefWidth()
                        + colName.getPrefWidth()
                        + colAssignment.getPrefWidth()
                        + colProject.getPrefWidth()
                        + colFinal.getPrefWidth()
                        + colTotal.getPrefWidth()
                        + colGrade.getPrefWidth()
                        + 22; // borders/scrollbar safety

        table.setPrefWidth(tableW);
        table.setMaxWidth(tableW);

        // Professional height: show 10 rows then scroll
        int visibleRows = 10;
        double tableH = visibleRows * table.getFixedCellSize() + 40; // + header
        table.setPrefHeight(tableH);
        table.setMaxHeight(tableH);
    }

    private TableColumn<MarkRow, Integer> editableMarkColumn(
            String title,
            int max,
            java.util.function.BiConsumer<MarkRow, Integer> setter
    ) {
        TableColumn<MarkRow, Integer> col = new TableColumn<>(title);
        col.setCellValueFactory(c -> c.getValue().getPropertyFor(title).asObject());

        // Editable numeric cell
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

    public static class MarkRow {
        private final IntegerProperty studentDbId = new SimpleIntegerProperty();
        private final StringProperty studentNumber = new SimpleStringProperty();
        private final StringProperty studentName = new SimpleStringProperty();

        private final IntegerProperty assignment = new SimpleIntegerProperty(0);
        private final IntegerProperty project = new SimpleIntegerProperty(0);
        private final IntegerProperty finalExam = new SimpleIntegerProperty(0);
        private final IntegerProperty total = new SimpleIntegerProperty();
        private final StringProperty grade = new SimpleStringProperty();

        public MarkRow(int dbId, String studentNo, String name) {
            studentDbId.set(dbId);
            studentNumber.set(studentNo);
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

        public int getStudentDbId() { return studentDbId.get(); }
        public StringProperty studentNumberProperty() { return studentNumber; }
        public StringProperty studentNameProperty() { return studentName; }
        public IntegerProperty totalProperty() { return total; }
        public StringProperty gradeProperty() { return grade; }

        public int getAssignment() { return assignment.get(); }
        public int getProject() { return project.get(); }
        public int getFinalExam() { return finalExam.get(); }

        public void setAssignment(int v) { assignment.set(v); }
        public void setProject(int v) { project.set(v); }
        public void setFinalExam(int v) { finalExam.set(v); }

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
        @Override public String toString(Integer v) { return v == null ? "0" : v.toString(); }
        @Override public Integer fromString(String s) {
            try { return Integer.parseInt(s.trim()); }
            catch (Exception e) { return 0; }
        }
    }
}