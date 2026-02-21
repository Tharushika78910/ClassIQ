package Frontend.student;

import Backend.db.DBConnection;
import Backend.model.dao.impl.MarksDaoImpl;
import Backend.model.entity.Student;
import Backend.model.entity.StudentMarks;
import Frontend.LoginPage;
import Frontend.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class StudentReportCardPage {

    private final StudentDashboard dashboard;

    public StudentReportCardPage(StudentDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Parent getView() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));

        // =========================
        // CENTER CONTENT
        // =========================
        StackPane centerWrapper = new StackPane();
        centerWrapper.setPadding(new Insets(20));

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_LEFT);
        contentBox.setMaxWidth(760);

        Label title = new Label("Academic Report Card");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        // extra space under title
        Region titleGap = new Region();
        titleGap.setPrefHeight(18);

        Student student = Session.getCurrentStudent();
        if (student == null) {
            Label err = new Label("No student session found. Please log in again.");
            err.setStyle("-fx-font-size: 16px;");
            contentBox.getChildren().addAll(title, titleGap, err);
            centerWrapper.getChildren().add(contentBox);
            root.setCenter(centerWrapper);
            return root;
        }

        // 1) Load SUBJECT -> (mark,total grade) from teacher_marksheet
        Map<String, SubjectResult> subjectResults = loadResultsFromTeacherMarksheet(student.getStudentId());

        // 2) Load feedback from student_marks table
        StudentMarks marks = null;
        try {
            marks = new MarksDaoImpl().findByStudentId(student.getStudentId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String feedback = (marks == null || marks.getFeedback() == null || marks.getFeedback().isBlank())
                ? "No feedback has been added yet."
                : marks.getFeedback();

        // =========================
        // GRID (3 columns for subjects)
        // =========================
        GridPane grid = new GridPane();
        grid.setVgap(18);
        grid.setHgap(60);

        // Make columns align nicely
        ColumnConstraints c0 = new ColumnConstraints();
        c0.setMinWidth(220);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(90);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setMinWidth(70);
        grid.getColumnConstraints().addAll(c0, c1, c2);

        int r = 0;

        // Student Number + Class
        grid.add(labelLeft("Student Number"), 0, r);
        grid.add(labelValue(nullSafe(student.getStudentNumber())), 1, r, 2, 1);
        r++;

        grid.add(labelLeft("Class"), 0, r);
        grid.add(labelValue("10A"), 1, r, 2, 1);
        r++;

        // Subject rows (Subject | Mark | Grade)
        r = addSubjectRow(grid, r, "Mathematics", subjectResults);
        r = addSubjectRow(grid, r, "English", subjectResults);
        r = addSubjectRow(grid, r, "Science", subjectResults);
        r = addSubjectRow(grid, r, "Language", subjectResults);
        r = addSubjectRow(grid, r, "Craft", subjectResults);

        // Feedback row (Feedback | text across 2 columns)
        grid.add(labelLeft("Feedback"), 0, r);
        Label fb = labelValue(feedback);
        fb.setWrapText(true);
        fb.setMaxWidth(420);
        grid.add(fb, 1, r, 2, 1);

        contentBox.getChildren().addAll(title, titleGap, grid);

        centerWrapper.getChildren().add(contentBox);
        StackPane.setAlignment(contentBox, Pos.TOP_CENTER);
        root.setCenter(centerWrapper);

        // =========================
        // BOTTOM BUTTON BAR (TeacherMarkSheet style)
        // =========================
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

    private int addSubjectRow(GridPane grid, int rowIndex, String uiSubject, Map<String, SubjectResult> map) {

        SubjectResult sr = map.get(uiSubject);
        String markStr = (sr == null || sr.total == null) ? "-" : String.valueOf(sr.total);
        String gradeStr = (sr == null || sr.grade == null || sr.grade.isBlank()) ? "-" : sr.grade;

        grid.add(labelLeft(uiSubject), 0, rowIndex);
        grid.add(labelValue(markStr), 1, rowIndex);
        grid.add(labelValue(gradeStr), 2, rowIndex);

        return rowIndex + 1;
    }

    /**
     * Reads latest (total, grade) per subject for this student from teacher_marksheet.
     * If multiple rows exist per subject, we keep the most recent (created_at DESC, id DESC).
     */
    private Map<String, SubjectResult> loadResultsFromTeacherMarksheet(int studentId) {

        Map<String, SubjectResult> out = new HashMap<>();

        String sql = """
            SELECT subject, total, grade
            FROM teacher_marksheet
            WHERE student_id = ?
            ORDER BY created_at DESC, id DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    String subjectKey = normalizeSubject(rs.getString("subject"));
                    if (subjectKey == null || subjectKey.isBlank()) continue;

                    // keep first occurrence (latest) only
                    if (!out.containsKey(subjectKey)) {
                        Integer total = (Integer) rs.getObject("total"); // can be null
                        String grade = rs.getString("grade");
                        out.put(subjectKey, new SubjectResult(total, grade));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    private String normalizeSubject(String dbSubject) {
        if (dbSubject == null) return null;
        String s = dbSubject.trim().toLowerCase();

        if (s.contains("math")) return "Mathematics";
        if (s.contains("eng")) return "English";
        if (s.contains("sci")) return "Science";
        if (s.contains("craft")) return "Craft";
        if (s.contains("lang")) return "Language"; // Language / Languages

        // fallback
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private Label labelLeft(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        return l;
    }

    private Label labelValue(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 18px;");
        return l;
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    private static class SubjectResult {
        final Integer total;
        final String grade;

        SubjectResult(Integer total, String grade) {
            this.total = total;
            this.grade = grade;
        }
    }
}