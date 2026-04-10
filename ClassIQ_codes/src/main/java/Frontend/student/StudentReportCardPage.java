package Frontend.student;

import Backend.model.dao.impl.MarksDaoImpl;
import Backend.model.entity.Student;
import Backend.model.entity.StudentMarks;
import Frontend.LoginPage;
import Frontend.Session;
import javafx.stage.FileChooser;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import java.util.Locale;


public class StudentReportCardPage {

    private final StudentDashboard dashboard;

    public StudentReportCardPage(StudentDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Parent getView() {

        ResourceBundle bundle = ResourceBundle.getBundle("messages", Session.getCurrentLocale());
        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-bg");

        root.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/student-dashboard.css")).toExternalForm()
        );

        root.setPadding(new Insets(30));

        StackPane centerWrapper = new StackPane();
        centerWrapper.setPadding(new Insets(20));

        VBox contentBox = new VBox(12);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setMaxWidth(700);
        contentBox.setPrefWidth(700);

        Label title = new Label(bundle.getString("student.report.title"));
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: 800; -fx-text-fill: #1f2d2a;");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        Region titleGap = new Region();
        titleGap.setPrefHeight(12);

        Student student = Session.getCurrentStudent();
        if (student == null) {
            Label err = new Label(bundle.getString("student.report.error.noSession"));
            err.setStyle("-fx-font-size: 16px;");
            contentBox.getChildren().addAll(title, titleGap, err);
            centerWrapper.getChildren().add(contentBox);
            root.setCenter(centerWrapper);
            return root;
        }

        StudentMarks marks = null;
        try {
            marks = new MarksDaoImpl().findByStudentId(student.getStudentId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String feedback = (marks == null || marks.getFeedback() == null || marks.getFeedback().isBlank())
                ? bundle.getString("student.report.noFeedback")
                : marks.getFeedback();

        Integer math  = (marks == null) ? null : marks.getSubject1();
        Integer eng   = (marks == null) ? null : marks.getSubject2();
        Integer sci   = (marks == null) ? null : marks.getSubject3();
        Integer craft = (marks == null) ? null : marks.getSubject4();
        Integer lang  = (marks == null) ? null : marks.getSubject5();

        Integer total = (marks == null) ? null : marks.getTotal();
        Double average = (marks == null) ? null : marks.getAverage();

        Map<String, SubjectResult> subjectResults = new LinkedHashMap<>();
        subjectResults.put(bundle.getString("student.report.subject.mathematics"), new SubjectResult(math, gradeFromMark(math)));
        subjectResults.put(bundle.getString("student.report.subject.english"), new SubjectResult(eng, gradeFromMark(eng)));
        subjectResults.put(bundle.getString("student.report.subject.science"), new SubjectResult(sci, gradeFromMark(sci)));
        subjectResults.put(bundle.getString("student.report.subject.language"), new SubjectResult(lang, gradeFromMark(lang)));
        subjectResults.put(bundle.getString("student.report.subject.craft"), new SubjectResult(craft, gradeFromMark(craft)));

        GridPane grid = new GridPane();
        grid.setVgap(18);
        grid.setHgap(40);
        grid.setAlignment(Pos.CENTER);

        ColumnConstraints c0 = new ColumnConstraints();
        c0.setMinWidth(260);
        c0.setHalignment(javafx.geometry.HPos.LEFT);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(120);
        c1.setHalignment(javafx.geometry.HPos.CENTER);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setMinWidth(120);
        c2.setHalignment(javafx.geometry.HPos.CENTER);

        grid.getColumnConstraints().addAll(c0, c1, c2);

        int r = 0;

        grid.add(labelLeft(bundle.getString("student.report.studentNumber")), 0, r);
        grid.add(labelCenter(nullSafe(student.getStudentNumber())), 1, r, 2, 1);
        r++;

        grid.add(labelLeft(bundle.getString("student.report.class")), 0, r);
        grid.add(labelCenter(bundle.getString("student.report.defaultClass")), 1, r, 2, 1);
        r++;

        r = addSubjectRow(grid, r, bundle.getString("student.report.subject.mathematics"), subjectResults);
        r = addSubjectRow(grid, r, bundle.getString("student.report.subject.english"), subjectResults);
        r = addSubjectRow(grid, r, bundle.getString("student.report.subject.science"), subjectResults);
        r = addSubjectRow(grid, r, bundle.getString("student.report.subject.language"), subjectResults);
        r = addSubjectRow(grid, r, bundle.getString("student.report.subject.craft"), subjectResults);

        grid.add(labelLeft(bundle.getString("student.report.total")), 0, r);
        grid.add(labelCenter(total == null ? "-" : String.valueOf(total)), 1, r, 2, 1);
        r++;

        grid.add(labelLeft(bundle.getString("student.report.average")), 0, r);
        grid.add(labelCenter(average == null ? "-" : String.format("%.2f", average)), 1, r, 2, 1);
        r++;

        grid.add(labelLeft(bundle.getString("student.report.feedback")), 0, r);
        Label fb = labelCenter(feedback);
        fb.setWrapText(true);
        fb.setMaxWidth(420);
        grid.add(fb, 1, r, 2, 1);

        contentBox.getChildren().addAll(title, titleGap, grid);
        centerWrapper.getChildren().add(contentBox);
        StackPane.setAlignment(contentBox, Pos.TOP_CENTER);
        root.setCenter(centerWrapper);

        String pillNormal =
                "-fx-background-color: #CFE8FF;" +
                        "-fx-text-fill: #0B3D91;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 8,0,0,2);";

        String pillHover =
                "-fx-background-color: #B7DBFF;" +
                        "-fx-text-fill: #082C6C;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;";

        Button btnBack = dashboard.createStudentBackButton(() -> dashboard.showHome());

        Button btnPdf = new Button(bundle.getString("student.downloadPdf"));
        btnPdf.setStyle(pillNormal);
        btnPdf.setOnMouseEntered(e -> btnPdf.setStyle(pillHover));
        btnPdf.setOnMouseExited(e -> btnPdf.setStyle(pillNormal));
        btnPdf.setOnAction(ev -> {
            try {
                Map<String, ReportCardPdfExporter.SubjectLine> pdfSubjects = new LinkedHashMap<>();
                for (Map.Entry<String, SubjectResult> e2 : subjectResults.entrySet()) {
                    SubjectResult sr = e2.getValue();
                    pdfSubjects.put(e2.getKey(), new ReportCardPdfExporter.SubjectLine(sr.total, sr.grade));
                }

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(bundle.getString("student.report.saveDialog.title"));
                fileChooser.setInitialFileName(
                        bundle.getString("student.report.saveDialog.filenamePrefix")
                                + "_" + student.getStudentNumber() + "_" + java.time.LocalDate.now() + ".pdf"
                );
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter(bundle.getString("student.report.saveDialog.filterName"), "*.pdf")
                );

                Stage currentStage = (Stage) root.getScene().getWindow();
                File selectedFile = fileChooser.showSaveDialog(currentStage);

                if (selectedFile == null) {
                    return;
                }

                File pdf = ReportCardPdfExporter.export(
                        selectedFile,
                        student.getStudentNumber(),
                        student.getFirstName() + " " + student.getLastName(),
                        bundle.getString("student.report.defaultClass"),
                        pdfSubjects,
                        total,
                        average,
                        feedback
                );

                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText(bundle.getString("student.report.pdf.generated"));
                a.setContentText(bundle.getString("student.report.pdf.saved") + "\n" + pdf.getAbsolutePath());
                a.showAndWait();

            } catch (Exception ex) {
                ex.printStackTrace();
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText(bundle.getString("student.report.pdf.error"));
                a.setContentText(ex.getMessage());
                a.showAndWait();
            }
        });

        Button btnLogout = dashboard.createStudentLogoutButton(() -> {
            Locale savedLocale = Session.getCurrentLocale();
            Session.clear();
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new LoginPage(stage, savedLocale).getScene());
        });

        AnchorPane bottomBar = new AnchorPane();
        bottomBar.setPadding(new Insets(15));

        AnchorPane.setLeftAnchor(btnBack, 20.0);
        AnchorPane.setBottomAnchor(btnBack, 10.0);

        AnchorPane.setLeftAnchor(btnPdf, 165.0);
        AnchorPane.setBottomAnchor(btnPdf, 10.0);

        AnchorPane.setRightAnchor(btnLogout, 20.0);
        AnchorPane.setBottomAnchor(btnLogout, 10.0);

        bottomBar.getChildren().addAll(btnBack, btnPdf, btnLogout);
        root.setBottom(bottomBar);

        return root;
    }

    private int addSubjectRow(GridPane grid, int rowIndex, String uiSubject, Map<String, SubjectResult> map) {
        SubjectResult sr = map.get(uiSubject);
        String markStr = (sr == null || sr.total == null) ? "-" : String.valueOf(sr.total);
        String gradeStr = (sr == null || sr.grade == null || sr.grade.isBlank()) ? "-" : sr.grade;

        grid.add(labelLeft(uiSubject), 0, rowIndex);
        grid.add(labelCenter(markStr), 1, rowIndex);
        grid.add(labelCenter(gradeStr), 2, rowIndex);

        return rowIndex + 1;
    }

    private String gradeFromMark(Integer mark) {
        if (mark == null) return "-";
        int m = mark;
        if (m >= 75) return "A";
        if (m >= 65) return "B";
        if (m >= 55) return "C";
        if (m >= 35) return "S";
        return "F";
    }

    private Label labelLeft(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        l.setMaxWidth(Double.MAX_VALUE);
        return l;
    }

    private Label labelCenter(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 18px;");
        l.setMaxWidth(Double.MAX_VALUE);
        l.setAlignment(Pos.CENTER);
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