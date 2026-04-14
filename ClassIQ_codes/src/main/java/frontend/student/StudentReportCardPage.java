package frontend.student;

import backend.model.dao.impl.MarksDaoImpl;
import backend.model.entity.Student;
import backend.model.entity.StudentMarks;
import frontend.LoginPage;
import frontend.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudentReportCardPage {

    private static final String PAGE_STYLESHEET = "/css/student-dashboard.css";

    private final StudentDashboard dashboard;

    public StudentReportCardPage(StudentDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Parent getView() {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Session.getCurrentLocale());

        BorderPane root = createRoot();
        StackPane centerWrapper = createCenterWrapper();
        VBox contentBox = createContentBox();
        Label title = createTitle(bundle);
        Region titleGap = createTitleGap();

        Student student = Session.getCurrentStudent();
        if (student == null) {
            showNoSessionState(bundle, root, centerWrapper, contentBox, title, titleGap);
            return root;
        }

        StudentMarks marks = loadStudentMarks(student);
        ReportCardViewData viewData = buildViewData(bundle, marks);

        GridPane grid = createReportGrid(bundle, student, viewData);

        contentBox.getChildren().addAll(title, titleGap, grid);
        centerWrapper.getChildren().add(contentBox);
        StackPane.setAlignment(contentBox, Pos.TOP_CENTER);
        root.setCenter(centerWrapper);

        createBottomBar(root, bundle, student, viewData);
        return root;
    }

    private BorderPane createRoot() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-bg");
        root.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(PAGE_STYLESHEET)).toExternalForm()
        );
        root.setPadding(new Insets(30));
        return root;
    }

    private StackPane createCenterWrapper() {
        StackPane centerWrapper = new StackPane();
        centerWrapper.setPadding(new Insets(20));
        return centerWrapper;
    }

    private VBox createContentBox() {
        VBox contentBox = new VBox(12);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setMaxWidth(700);
        contentBox.setPrefWidth(700);
        return contentBox;
    }

    private Label createTitle(ResourceBundle bundle) {
        Label title = new Label(bundle.getString("student.report.title"));
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: 800; -fx-text-fill: #1f2d2a;");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        return title;
    }

    private Region createTitleGap() {
        Region titleGap = new Region();
        titleGap.setPrefHeight(12);
        return titleGap;
    }

    private void showNoSessionState(
            ResourceBundle bundle,
            BorderPane root,
            StackPane centerWrapper,
            VBox contentBox,
            Label title,
            Region titleGap
    ) {
        Label errorLabel = new Label(bundle.getString("student.report.error.noSession"));
        errorLabel.setStyle("-fx-font-size: 16px;");
        contentBox.getChildren().addAll(title, titleGap, errorLabel);
        centerWrapper.getChildren().add(contentBox);
        root.setCenter(centerWrapper);
    }

    private StudentMarks loadStudentMarks(Student student) {
        try {
            return new MarksDaoImpl().findByStudentId(student.getStudentId());
        } catch (SQLException | RuntimeException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private ReportCardViewData buildViewData(ResourceBundle bundle, StudentMarks marks) {
        String feedback = resolveFeedback(bundle, marks);
        Integer math = marks == null ? null : marks.getSubject1();
        Integer english = marks == null ? null : marks.getSubject2();
        Integer science = marks == null ? null : marks.getSubject3();
        Integer craft = marks == null ? null : marks.getSubject4();
        Integer language = marks == null ? null : marks.getSubject5();
        Integer total = marks == null ? null : marks.getTotal();
        Double average = marks == null ? null : marks.getAverage();

        Map<String, SubjectResult> subjectResults = new LinkedHashMap<>();
        subjectResults.put(
                bundle.getString("student.report.subject.mathematics"),
                new SubjectResult(math, gradeFromMark(math))
        );
        subjectResults.put(
                bundle.getString("student.report.subject.english"),
                new SubjectResult(english, gradeFromMark(english))
        );
        subjectResults.put(
                bundle.getString("student.report.subject.science"),
                new SubjectResult(science, gradeFromMark(science))
        );
        subjectResults.put(
                bundle.getString("student.report.subject.language"),
                new SubjectResult(language, gradeFromMark(language))
        );
        subjectResults.put(
                bundle.getString("student.report.subject.craft"),
                new SubjectResult(craft, gradeFromMark(craft))
        );

        return new ReportCardViewData(subjectResults, total, average, feedback);
    }

    private String resolveFeedback(ResourceBundle bundle, StudentMarks marks) {
        if (marks == null || marks.getFeedback() == null || marks.getFeedback().isBlank()) {
            return bundle.getString("student.report.noFeedback");
        }
        return marks.getFeedback();
    }

    private GridPane createReportGrid(ResourceBundle bundle, Student student, ReportCardViewData viewData) {
        GridPane grid = createBaseGrid();

        int rowIndex = 0;
        rowIndex = addStudentNumberRow(grid, rowIndex, bundle, student);
        rowIndex = addClassRow(grid, rowIndex, bundle);
        rowIndex = addSubjectRows(grid, rowIndex, bundle, viewData.subjectResults);
        rowIndex = addTotalRow(grid, rowIndex, bundle, viewData.total);
        rowIndex = addAverageRow(grid, rowIndex, bundle, viewData.average);
        addFeedbackRow(grid, rowIndex, bundle, viewData.feedback);

        return grid;
    }

    private GridPane createBaseGrid() {
        GridPane grid = new GridPane();
        grid.setVgap(18);
        grid.setHgap(40);
        grid.setAlignment(Pos.CENTER);

        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setMinWidth(260);
        firstColumn.setHalignment(javafx.geometry.HPos.LEFT);

        ColumnConstraints secondColumn = new ColumnConstraints();
        secondColumn.setMinWidth(120);
        secondColumn.setHalignment(javafx.geometry.HPos.CENTER);

        ColumnConstraints thirdColumn = new ColumnConstraints();
        thirdColumn.setMinWidth(120);
        thirdColumn.setHalignment(javafx.geometry.HPos.CENTER);

        grid.getColumnConstraints().addAll(firstColumn, secondColumn, thirdColumn);
        return grid;
    }

    private int addStudentNumberRow(GridPane grid, int rowIndex, ResourceBundle bundle, Student student) {
        grid.add(labelLeft(bundle.getString("student.report.studentNumber")), 0, rowIndex);
        grid.add(labelCenter(nullSafe(student.getStudentNumber())), 1, rowIndex, 2, 1);
        return rowIndex + 1;
    }

    private int addClassRow(GridPane grid, int rowIndex, ResourceBundle bundle) {
        grid.add(labelLeft(bundle.getString("student.report.class")), 0, rowIndex);
        grid.add(labelCenter(bundle.getString("student.report.defaultClass")), 1, rowIndex, 2, 1);
        return rowIndex + 1;
    }

    private int addSubjectRows(
            GridPane grid,
            int rowIndex,
            ResourceBundle bundle,
            Map<String, SubjectResult> subjectResults
    ) {
        rowIndex = addSubjectRow(grid, rowIndex, bundle.getString("student.report.subject.mathematics"), subjectResults);
        rowIndex = addSubjectRow(grid, rowIndex, bundle.getString("student.report.subject.english"), subjectResults);
        rowIndex = addSubjectRow(grid, rowIndex, bundle.getString("student.report.subject.science"), subjectResults);
        rowIndex = addSubjectRow(grid, rowIndex, bundle.getString("student.report.subject.language"), subjectResults);
        return addSubjectRow(grid, rowIndex, bundle.getString("student.report.subject.craft"), subjectResults);
    }

    private int addTotalRow(GridPane grid, int rowIndex, ResourceBundle bundle, Integer total) {
        grid.add(labelLeft(bundle.getString("student.report.total")), 0, rowIndex);
        grid.add(labelCenter(total == null ? "-" : String.valueOf(total)), 1, rowIndex, 2, 1);
        return rowIndex + 1;
    }

    private int addAverageRow(GridPane grid, int rowIndex, ResourceBundle bundle, Double average) {
        grid.add(labelLeft(bundle.getString("student.report.average")), 0, rowIndex);
        grid.add(labelCenter(average == null ? "-" : String.format("%.2f", average)), 1, rowIndex, 2, 1);
        return rowIndex + 1;
    }

    private void addFeedbackRow(GridPane grid, int rowIndex, ResourceBundle bundle, String feedback) {
        grid.add(labelLeft(bundle.getString("student.report.feedback")), 0, rowIndex);
        Label feedbackLabel = labelCenter(feedback);
        feedbackLabel.setWrapText(true);
        feedbackLabel.setMaxWidth(420);
        grid.add(feedbackLabel, 1, rowIndex, 2, 1);
    }

    private void createBottomBar(
            BorderPane root,
            ResourceBundle bundle,
            Student student,
            ReportCardViewData viewData
    ) {
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

        Button btnBack = dashboard.createStudentBackButton(dashboard::showHome);
        Button btnPdf = createPdfButton(root, bundle, student, viewData, pillNormal, pillHover);
        Button btnLogout = dashboard.createStudentLogoutButton(() -> logout(root));

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
    }

    private Button createPdfButton(
            BorderPane root,
            ResourceBundle bundle,
            Student student,
            ReportCardViewData viewData,
            String pillNormal,
            String pillHover
    ) {
        Button btnPdf = new Button(bundle.getString("student.downloadPdf"));
        btnPdf.setStyle(pillNormal);
        btnPdf.setOnMouseEntered(e -> btnPdf.setStyle(pillHover));
        btnPdf.setOnMouseExited(e -> btnPdf.setStyle(pillNormal));
        btnPdf.setOnAction(event -> exportPdf(root, bundle, student, viewData));
        return btnPdf;
    }

    private void exportPdf(
            BorderPane root,
            ResourceBundle bundle,
            Student student,
            ReportCardViewData viewData
    ) {
        try {
            Map<String, ReportCardPdfExporter.SubjectLine> pdfSubjects = buildPdfSubjects(viewData.subjectResults);

            FileChooser fileChooser = createFileChooser(bundle, student);
            Stage currentStage = (Stage) root.getScene().getWindow();
            File selectedFile = fileChooser.showSaveDialog(currentStage);

            if (selectedFile == null) {
                return;
            }

            ReportCardPdfExporter.ReportCardData pdfData =
                    new ReportCardPdfExporter.ReportCardData(
                            student.getStudentNumber(),
                            student.getFirstName() + " " + student.getLastName(),
                            bundle.getString("student.report.defaultClass"),
                            pdfSubjects,
                            viewData.total,
                            viewData.average,
                            viewData.feedback
                    );

            File pdf = ReportCardPdfExporter.export(selectedFile, pdfData);
            showInfoAlert(bundle, pdf);
        } catch (ReportCardPdfExporter.ReportCardExportException exception) {
            exception.printStackTrace();
            showErrorAlert(bundle, exception.getMessage());
        }
    }

    private Map<String, ReportCardPdfExporter.SubjectLine> buildPdfSubjects(Map<String, SubjectResult> subjectResults) {
        Map<String, ReportCardPdfExporter.SubjectLine> pdfSubjects = new LinkedHashMap<>();
        for (Map.Entry<String, SubjectResult> entry : subjectResults.entrySet()) {
            SubjectResult subjectResult = entry.getValue();
            pdfSubjects.put(
                    entry.getKey(),
                    new ReportCardPdfExporter.SubjectLine(subjectResult.total, subjectResult.grade)
            );
        }
        return pdfSubjects;
    }

    private FileChooser createFileChooser(ResourceBundle bundle, Student student) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("student.report.saveDialog.title"));
        fileChooser.setInitialFileName(
                bundle.getString("student.report.saveDialog.filenamePrefix")
                        + "_" + student.getStudentNumber() + "_" + LocalDate.now() + ".pdf"
        );
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(bundle.getString("student.report.saveDialog.filterName"), "*.pdf")
        );
        return fileChooser;
    }

    private void showInfoAlert(ResourceBundle bundle, File pdf) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(bundle.getString("student.report.pdf.generated"));
        alert.setContentText(bundle.getString("student.report.pdf.saved") + "\n" + pdf.getAbsolutePath());
        alert.showAndWait();
    }

    private void showErrorAlert(ResourceBundle bundle, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(bundle.getString("student.report.pdf.error"));
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void logout(BorderPane root) {
        Locale savedLocale = Session.getCurrentLocale();
        Session.clear();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new LoginPage(stage, savedLocale).getScene());
    }

    private int addSubjectRow(GridPane grid, int rowIndex, String uiSubject, Map<String, SubjectResult> map) {
        SubjectResult result = map.get(uiSubject);
        String markText = (result == null || result.total == null) ? "-" : String.valueOf(result.total);
        String gradeText = (result == null || result.grade == null || result.grade.isBlank()) ? "-" : result.grade;

        grid.add(labelLeft(uiSubject), 0, rowIndex);
        grid.add(labelCenter(markText), 1, rowIndex);
        grid.add(labelCenter(gradeText), 2, rowIndex);

        return rowIndex + 1;
    }

    private String gradeFromMark(Integer mark) {
        if (mark == null) {
            return "-";
        }
        if (mark >= 75) {
            return "A";
        }
        if (mark >= 65) {
            return "B";
        }
        if (mark >= 55) {
            return "C";
        }
        if (mark >= 35) {
            return "S";
        }
        return "F";
    }

    private Label labelLeft(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private Label labelCenter(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 18px;");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private String nullSafe(String text) {
        return text == null ? "" : text;
    }

    private static class SubjectResult {
        final Integer total;
        final String grade;

        SubjectResult(Integer total, String grade) {
            this.total = total;
            this.grade = grade;
        }
    }

    private static class ReportCardViewData {
        final Map<String, SubjectResult> subjectResults;
        final Integer total;
        final Double average;
        final String feedback;

        ReportCardViewData(
                Map<String, SubjectResult> subjectResults,
                Integer total,
                Double average,
                String feedback
        ) {
            this.subjectResults = subjectResults;
            this.total = total;
            this.average = average;
            this.feedback = feedback;
        }
    }
}