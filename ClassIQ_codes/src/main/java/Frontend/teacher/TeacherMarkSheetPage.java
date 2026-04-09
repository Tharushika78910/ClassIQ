package Frontend.teacher;

import Backend.db.DBConnection;
import Backend.model.dao.impl.StudentDaoImpl;
import Backend.model.dao.impl.StudentMarksDaoImpl;
import Backend.model.dao.impl.TeacherMarkSheetDaoImpl;
import Backend.model.entity.Student;
import Backend.service.WeightedMarkService;
import Frontend.Session;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class TeacherMarkSheetPage {

    private static final WeightedMarkService weightedMarkService = new WeightedMarkService();

    private final TableView<MarkRow> table = new TableView<>();
    private final Label status = new Label("");
    private final TeacherDashboard dashboard;

    private BorderPane root;
    private Label subjectTitle;
    private Button btnSave;
    private Button btnClear;

    private TableColumn<MarkRow, String> colNo;
    private TableColumn<MarkRow, String> colName;
    private TableColumn<MarkRow, Integer> colAssignment;
    private TableColumn<MarkRow, Integer> colProject;
    private TableColumn<MarkRow, Integer> colFinal;
    private TableColumn<MarkRow, Integer> colTotal;
    private TableColumn<MarkRow, String> colGrade;

    public TeacherMarkSheetPage(TeacherDashboard dashboard) {
        this.dashboard = dashboard;
    }

    private enum MarkType {
        ASSIGNMENT,
        PROJECT,
        FINAL_EXAM
    }

    public Parent getView() {

        root = new BorderPane();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("page-bg");

        VBox centerBox = new VBox(16);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setFillWidth(false);

        subjectTitle = new Label();
        subjectTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: #1f2d2a;");

        buildTable();

        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER);
        card.setFillWidth(false);

        card.setPrefWidth(table.getPrefWidth() + 40);
        card.setMaxWidth(Region.USE_PREF_SIZE);

        btnSave = new Button();
        btnSave.getStyleClass().add("primary-btn");

        btnClear = new Button();
        btnClear.getStyleClass().add("secondary-btn");

        status.getStyleClass().add("muted-text");

        applyTranslations();
        loadStudentsAndMarks();

        btnSave.setOnAction(e -> handleSave());
        btnClear.setOnAction(e -> handleClear());

        HBox actions = new HBox(10, btnSave, btnClear, status);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.setPadding(new Insets(2, 0, 0, 0));

        card.getChildren().addAll(table, actions);
        centerBox.getChildren().addAll(subjectTitle, card);

        StackPane centerWrap = new StackPane(centerBox);
        centerWrap.setAlignment(Pos.CENTER);
        root.setCenter(centerWrap);

        return root;
    }

    private void applyTranslations() {
        String lang = Session.getLanguageCode();

        btnSave.setText(getMarksheetText("button.save"));
        btnClear.setText(getMarksheetText("button.clear"));

        colNo.setText(getMarksheetText("table.student_no"));
        colName.setText(getMarksheetText("table.student_name"));
        colTotal.setText(getMarksheetText("table.total"));
        colGrade.setText(getMarksheetText("table.grade"));

        colAssignment.setText(getTranslatedCategoryName(1, lang));
        colProject.setText(getTranslatedCategoryName(2, lang));
        colFinal.setText(getTranslatedCategoryName(3, lang));

        String subject = Session.getTeacherSubject();
        if (subject == null || subject.isBlank()) {
            subject = "Mathematics";
        }
        subjectTitle.setText(getTranslatedSubjectName(subject, lang));

        applyOrientation(lang);
    }

    private void applyOrientation(String lang) {
        if ("ar".equalsIgnoreCase(lang)) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            root.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    private void handleSave() {
        status.setText("");

        if (!Session.isTeacherLoggedIn()) {
            status.setText(getMarksheetText("status.teacher_session_not_found"));
            return;
        }

        if (!allInputsValid()) {
            status.setText(getMarksheetText("status.invalid_marks"));
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

                studentMarksDao.upsertSubjectTotal(
                        r.getStudentDbId(),
                        subj,
                        r.getTotal()
                );

                savedCount++;
            }

            status.setText(String.format(getMarksheetText("status.saved_records"), savedCount));
            loadStudentsAndMarks();

        } catch (Exception ex) {
            status.setText(getMarksheetText("status.db_error") + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleClear() {
        status.setText("");

        if (!Session.isTeacherLoggedIn()) {
            status.setText(getMarksheetText("status.teacher_session_not_found"));
            return;
        }

        int teacherId = Session.getTeacherId();
        String subj = Session.getTeacherSubject();
        if (subj == null || subj.isBlank()) subj = "Mathematics";

        try {
            TeacherMarkSheetDaoImpl dao = new TeacherMarkSheetDaoImpl();
            StudentMarksDaoImpl studentMarksDao = new StudentMarksDaoImpl();

            int deleted = dao.deleteMarksForTeacherSubject(teacherId, subj);
            int updated = studentMarksDao.clearSubjectForAllStudents(subj);

            status.setText(String.format(getMarksheetText("status.clear_success"), deleted, updated));
            loadStudentsAndMarks();

        } catch (Exception ex) {
            status.setText(getMarksheetText("status.db_error") + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadStudentsAndMarks() {

        if (!Session.isTeacherLoggedIn()) return;

        int teacherId = Session.getTeacherId();
        String subj = Session.getTeacherSubject();
        if (subj == null || subj.isBlank()) subj = "Mathematics";

        try {
            StudentDaoImpl studentDao = new StudentDaoImpl();
            String lang = Session.getLanguageCode();
            List<Student> students = studentDao.findAll(lang);

            ObservableList<MarkRow> rows = FXCollections.observableArrayList();

            for (Student s : students) {
                String fullName = studentDao.getTranslatedStudentName(s.getStudentId(), lang);
                if (fullName == null || fullName.isBlank()) {
                    fullName = s.getFirstName() + " " + s.getLastName();
                }

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
            status.setText(getMarksheetText("status.load_error") + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void buildTable() {

        table.setEditable(true);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(42);

        table.setRowFactory(tv -> {
            TableRow<MarkRow> row = new TableRow<>();
            row.setStyle("-fx-cell-size: 42px;");
            return row;
        });

        colNo = new TableColumn<>();
        colNo.setCellValueFactory(c -> c.getValue().studentNumberProperty());

        colName = new TableColumn<>();
        colName.setCellValueFactory(c -> c.getValue().studentNameProperty());

        colAssignment = editableMarkColumn(MarkType.ASSIGNMENT, 20);
        colProject = editableMarkColumn(MarkType.PROJECT, 30);
        colFinal = editableMarkColumn(MarkType.FINAL_EXAM, 50);

        colTotal = new TableColumn<>();
        colTotal.setCellValueFactory(c -> c.getValue().totalProperty().asObject());

        colGrade = new TableColumn<>();
        colGrade.setCellValueFactory(c -> c.getValue().gradeProperty());

        colNo.setPrefWidth(110);
        colName.setPrefWidth(280);
        colAssignment.setPrefWidth(95);
        colProject.setPrefWidth(85);
        colFinal.setPrefWidth(110);
        colTotal.setPrefWidth(70);
        colGrade.setPrefWidth(75);

        colNo.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 6 0 6; -fx-text-fill: black;");
        colName.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 6 0 6; -fx-text-fill: black;");

        colAssignment.setStyle("-fx-alignment: CENTER; -fx-padding: 0; -fx-text-fill: black;");
        colProject.setStyle("-fx-alignment: CENTER; -fx-padding: 0; -fx-text-fill: black;");
        colFinal.setStyle("-fx-alignment: CENTER; -fx-padding: 0; -fx-text-fill: black;");
        colTotal.setStyle("-fx-alignment: CENTER; -fx-padding: 0; -fx-text-fill: black;");
        colGrade.setStyle("-fx-alignment: CENTER; -fx-padding: 0; -fx-text-fill: black;");

        table.getColumns().setAll(
                colNo, colName,
                colAssignment, colProject, colFinal,
                colTotal, colGrade
        );

        double tableW =
                colNo.getPrefWidth()
                        + colName.getPrefWidth()
                        + colAssignment.getPrefWidth()
                        + colProject.getPrefWidth()
                        + colFinal.getPrefWidth()
                        + colTotal.getPrefWidth()
                        + colGrade.getPrefWidth()
                        + 22;

        table.setPrefWidth(tableW);
        table.setMaxWidth(tableW);

        int visibleRows = 10;
        double tableH = visibleRows * table.getFixedCellSize() + 40;
        table.setPrefHeight(tableH);
        table.setMaxHeight(tableH);
    }

    private TableColumn<MarkRow, Integer> editableMarkColumn(MarkType type, int max) {
        TableColumn<MarkRow, Integer> col = new TableColumn<>();
        col.setCellValueFactory(c -> c.getValue().getPropertyFor(type).asObject());

        col.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        col.setOnEditCommit(ev -> {
            Integer val = ev.getNewValue();
            if (val == null) val = 0;

            if (val < 0 || val > max) {
                status.setText(getRangeMessage(type, max));
                table.refresh();
                return;
            }

            switch (type) {
                case ASSIGNMENT -> ev.getRowValue().setAssignment(val);
                case PROJECT -> ev.getRowValue().setProject(val);
                case FINAL_EXAM -> ev.getRowValue().setFinalExam(val);
            }

            status.setText("");
            table.refresh();
        });

        return col;
    }

    private String getRangeMessage(MarkType type, int max) {
        return switch (type) {
            case ASSIGNMENT -> String.format(getMarksheetText("status.assignment_range"), max);
            case PROJECT -> String.format(getMarksheetText("status.project_range"), max);
            case FINAL_EXAM -> String.format(getMarksheetText("status.final_exam_range"), max);
        };
    }

    private boolean allInputsValid() {
        for (MarkRow r : table.getItems()) {
            if (r.getAssignment() < 0 || r.getAssignment() > 20) return false;
            if (r.getProject() < 0 || r.getProject() > 30) return false;
            if (r.getFinalExam() < 0 || r.getFinalExam() > 50) return false;
        }
        return true;
    }

    private String getMarksheetText(String key) {
        String sql = "SELECT translated_text FROM marksheet_translation WHERE translation_key = ? AND language_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, key);
            stmt.setString(2, Session.getLanguageCode());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("translated_text");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return key;
    }

    private String getTranslatedSubjectName(String subjectCode, String languageCode) {
        String sql = "SELECT subject_name FROM subject_translation WHERE subject_code = ? AND language_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subjectCode);
            stmt.setString(2, languageCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("subject_name");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return subjectCode;
    }

    private String getTranslatedCategoryName(int categoryId, String languageCode) {
        String sql = "SELECT category_name FROM gradecategory_translation WHERE category_id = ? AND language_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            stmt.setString(2, languageCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("category_name");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public void refreshTranslations() {
        applyTranslations();
        loadStudentsAndMarks();
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

        public IntegerProperty getPropertyFor(MarkType type) {
            return switch (type) {
                case ASSIGNMENT -> assignment;
                case PROJECT -> project;
                case FINAL_EXAM -> finalExam;
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