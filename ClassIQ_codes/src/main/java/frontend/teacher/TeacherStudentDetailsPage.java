package frontend.teacher;

import backend.controller.StudentDetailsController;
import backend.model.dto.StudentDetailsDTO;
import backend.model.entity.Student;
import backend.model.entity.StudentMarks;
import frontend.Session;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeacherStudentDetailsPage {

    private static final Logger LOGGER = Logger.getLogger(TeacherStudentDetailsPage.class.getName());

    private static final String SECTION_TITLE_CLASS = "section-title";
    private static final String INFO_TEXT_CLASS = "info-text";
    private static final String PRIMARY_BUTTON_CLASS = "primary-btn";

    private final String studentNumber;
    private final StudentDetailsController controller = new StudentDetailsController();

    public TeacherStudentDetailsPage(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Parent getView() {
        Locale locale = Session.getCurrentLocale();
        String languageCode = locale.getLanguage();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        boolean isArabic = "ar".equalsIgnoreCase(languageCode);
        boolean canEditFeedback = isMathSubject(Session.getTeacherSubject());

        BorderPane root = createRoot(isArabic);
        StudentDetailsView view = createStudentDetailsView(bundle);

        root.setTop(view.header());
        root.setCenter(view.center());

        loadStudentData(bundle, locale, languageCode, canEditFeedback, view);
        wireActions(bundle, languageCode, canEditFeedback, view);

        return root;
    }

    private BorderPane createRoot(boolean isArabic) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));
        root.getStyleClass().add("page-bg");
        root.setNodeOrientation(isArabic ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
        return root;
    }

    private StudentDetailsView createStudentDetailsView(ResourceBundle bundle) {
        Label headerTitle = new Label(bundle.getString("teacher.studentDetails.title"));
        headerTitle.getStyleClass().add("header-title");

        Label headerSub = new Label(bundle.getString("teacher.studentDetails.subtitle"));
        headerSub.getStyleClass().add("subtitle");

        VBox titleBox = new VBox(2, headerTitle, headerSub);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(titleBox);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");

        HBox card = new HBox(18);
        card.setPadding(new Insets(18));
        card.setMaxWidth(900);
        card.getStyleClass().add("details-card");

        VBox left = new VBox(10);
        left.getStyleClass().add("details-left");
        left.setPrefWidth(300);

        Label profileTitle = new Label(bundle.getString("teacher.studentDetails.profile"));
        profileTitle.getStyleClass().add(SECTION_TITLE_CLASS);

        Label lblStudentNo = new Label(bundle.getString("teacher.studentDetails.studentNumber") + ": " + studentNumber);
        lblStudentNo.getStyleClass().add(INFO_TEXT_CLASS);

        Label lblName = new Label(bundle.getString("teacher.studentDetails.name") + ": ");
        lblName.getStyleClass().add(INFO_TEXT_CLASS);

        Label lblEmail = new Label(bundle.getString("teacher.studentDetails.email") + ": ");
        lblEmail.getStyleClass().add(INFO_TEXT_CLASS);

        Label totalChip = new Label(bundle.getString("teacher.studentDetails.total") + ": 0");
        totalChip.getStyleClass().add("info-chip");

        Label avgChip = new Label(bundle.getString("teacher.studentDetails.average") + ": 0.00");
        avgChip.getStyleClass().add("info-chip");

        VBox chips = new VBox(8, totalChip, avgChip);
        chips.setPadding(new Insets(6, 0, 0, 0));

        left.getChildren().addAll(profileTitle, lblStudentNo, lblName, lblEmail, new Separator(), chips);

        VBox right = new VBox(14);
        right.getStyleClass().add("details-right");
        right.setFillWidth(true);

        Label marksTitle = new Label(bundle.getString("teacher.studentDetails.marks"));
        marksTitle.getStyleClass().add(SECTION_TITLE_CLASS);

        GridPane grid = createMarksGrid(bundle);

        Label m1 = new Label("0");
        Label m2 = new Label("0");
        Label m3 = new Label("0");
        Label m4 = new Label("0");
        Label m5 = new Label("0");

        addRow(grid, 1, bundle.getString("teacher.studentDetails.subject.mathematics"), m1);
        addRow(grid, 2, bundle.getString("teacher.studentDetails.subject.english"), m2);
        addRow(grid, 3, bundle.getString("teacher.studentDetails.subject.science"), m3);
        addRow(grid, 4, bundle.getString("teacher.studentDetails.subject.craft"), m4);
        addRow(grid, 5, bundle.getString("teacher.studentDetails.subject.language"), m5);

        Label feedbackTitle = new Label(bundle.getString("teacher.studentDetails.feedback"));
        feedbackTitle.getStyleClass().add(SECTION_TITLE_CLASS);

        TextArea feedbackArea = new TextArea();
        feedbackArea.setPrefRowCount(4);
        feedbackArea.setWrapText(true);
        feedbackArea.getStyleClass().add("feedback-area");
        feedbackArea.setEditable(false);

        Button btnSave = new Button(bundle.getString("teacher.studentDetails.save"));
        btnSave.getStyleClass().add(PRIMARY_BUTTON_CLASS);
        btnSave.setDisable(true);

        Button btnEdit = new Button(bundle.getString("teacher.studentDetails.edit"));
        btnEdit.getStyleClass().add(PRIMARY_BUTTON_CLASS);

        Button btnDelete = new Button(bundle.getString("teacher.studentDetails.delete"));
        btnDelete.getStyleClass().add(PRIMARY_BUTTON_CLASS);
        btnDelete.setDisable(true);

        HBox actions = new HBox(10, btnEdit, btnSave, btnDelete);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Label status = new Label();
        status.setTextFill(Color.DARKRED);
        status.getStyleClass().add("status-text");

        right.getChildren().addAll(
                marksTitle,
                grid,
                new Separator(),
                feedbackTitle,
                feedbackArea,
                actions,
                status
        );

        Separator verticalSeparator = new Separator(Orientation.VERTICAL);
        verticalSeparator.getStyleClass().add("v-sep");

        card.getChildren().addAll(left, verticalSeparator, right);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(18));

        return new StudentDetailsView(
                header,
                center,
                lblName,
                lblEmail,
                totalChip,
                avgChip,
                m1,
                m2,
                m3,
                m4,
                m5,
                feedbackArea,
                btnEdit,
                btnSave,
                btnDelete,
                status
        );
    }

    private GridPane createMarksGrid(ResourceBundle bundle) {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("marks-grid");
        grid.setHgap(30);
        grid.setVgap(10);

        Label subHead = new Label(bundle.getString("teacher.studentDetails.subject"));
        subHead.getStyleClass().add("table-head");

        Label markHead = new Label(bundle.getString("teacher.studentDetails.marks"));
        markHead.getStyleClass().add("table-head");

        grid.add(subHead, 0, 0);
        grid.add(markHead, 1, 0);

        return grid;
    }

    private void loadStudentData(
            ResourceBundle bundle,
            Locale locale,
            String languageCode,
            boolean canEditFeedback,
            StudentDetailsView view
    ) {
        try {
            StudentDetailsDTO dto = controller.getDetails(studentNumber, languageCode);
            Student student = dto.getStudent();
            StudentMarks marks = dto.getMarks();

            populateStudentInfo(bundle, locale, student, marks, view);
            populateFeedback(view);
            applyEditPermissions(bundle, canEditFeedback, view);

        } catch (Exception exception) {
            showErrorStatus(
                    view.status(),
                    bundle.getString("teacher.studentDetails.error.load") + ": " + exception.getMessage()
            );
            LOGGER.log(
                    Level.SEVERE,
                    exception,
                    () -> "Failed to load student details for student number: " + studentNumber
            );
        }
    }

    private void populateStudentInfo(
            ResourceBundle bundle,
            Locale locale,
            Student student,
            StudentMarks marks,
            StudentDetailsView view
    ) {
        view.lblName().setText(
                bundle.getString("teacher.studentDetails.name") + ": "
                        + safe(student.getFirstName()) + " " + safe(student.getLastName())
        );

        view.lblEmail().setText(
                bundle.getString("teacher.studentDetails.email") + ": "
                        + safe(student.getEmail())
        );

        view.m1().setText(String.valueOf(marks.getSubject1()));
        view.m2().setText(String.valueOf(marks.getSubject2()));
        view.m3().setText(String.valueOf(marks.getSubject3()));
        view.m4().setText(String.valueOf(marks.getSubject4()));
        view.m5().setText(String.valueOf(marks.getSubject5()));

        view.totalChip().setText(bundle.getString("teacher.studentDetails.total") + ": " + marks.getTotal());
        view.avgChip().setText(String.format(
                locale,
                "%s: %.2f",
                bundle.getString("teacher.studentDetails.average"),
                marks.getAverage()
        ));
    }

    private void populateFeedback(StudentDetailsView view) throws Exception {
        String feedback = controller.loadFeedback(studentNumber);
        view.feedbackArea().setText(feedback == null ? "" : feedback);
    }

    private void applyEditPermissions(ResourceBundle bundle, boolean canEditFeedback, StudentDetailsView view) {
        if (!canEditFeedback) {
            view.btnEdit().setDisable(true);
            view.btnSave().setDisable(true);
            view.feedbackArea().setEditable(false);

            view.btnDelete().setDisable(true);
            view.btnDelete().setVisible(false);
            view.btnDelete().setManaged(false);

            showErrorStatus(
                    view.status(),
                    bundle.getString("teacher.studentDetails.status.onlyClassTeacherEdit")
            );
            return;
        }

        view.btnEdit().setDisable(false);
        view.btnSave().setDisable(false);
        view.feedbackArea().setEditable(true);
        view.feedbackArea().requestFocus();

        boolean hasFeedback = view.feedbackArea().getText() != null
                && !view.feedbackArea().getText().trim().isEmpty();
        view.btnDelete().setDisable(!hasFeedback);

        showErrorStatus(
                view.status(),
                bundle.getString("teacher.studentDetails.status.typeAndSave")
        );
    }

    private void wireActions(
            ResourceBundle bundle,
            String languageCode,
            boolean canEditFeedback,
            StudentDetailsView view
    ) {
        view.btnEdit().setOnAction(event -> handleEdit(bundle, canEditFeedback, view));
        view.btnSave().setOnAction(event -> handleSave(bundle, languageCode, canEditFeedback, view));
        view.btnDelete().setOnAction(event -> handleDelete(bundle, languageCode, canEditFeedback, view));
    }

    private void handleEdit(ResourceBundle bundle, boolean canEditFeedback, StudentDetailsView view) {
        if (!canEditFeedback) {
            showErrorStatus(
                    view.status(),
                    bundle.getString("teacher.studentDetails.status.onlyClassTeacherEdit")
            );
            return;
        }

        view.feedbackArea().setEditable(true);
        view.btnSave().setDisable(false);
        view.feedbackArea().requestFocus();
        showErrorStatus(
                view.status(),
                bundle.getString("teacher.studentDetails.status.editAndSave")
        );
    }

    private void handleSave(
            ResourceBundle bundle,
            String languageCode,
            boolean canEditFeedback,
            StudentDetailsView view
    ) {
        try {
            if (!canEditFeedback) {
                showErrorStatus(
                        view.status(),
                        bundle.getString("teacher.studentDetails.error.onlyClassTeacherSave")
                );
                return;
            }

            String newFeedback = view.feedbackArea().getText() == null
                    ? ""
                    : view.feedbackArea().getText().trim();

            if (newFeedback.isEmpty()) {
                showErrorStatus(
                        view.status(),
                        bundle.getString("teacher.studentDetails.error.emptyFeedback")
                );
                return;
            }

            if (newFeedback.length() > 255) {
                showErrorStatus(
                        view.status(),
                        bundle.getString("teacher.studentDetails.error.feedbackTooLong")
                );
                return;
            }

            controller.saveFeedback(studentNumber, newFeedback, Session.getUserId(), languageCode);

            view.feedbackArea().setEditable(false);
            view.btnSave().setDisable(true);
            view.btnDelete().setDisable(false);

            showSuccessStatus(
                    view.status(),
                    bundle.getString("teacher.studentDetails.status.saved")
            );

        } catch (Exception exception) {
            showErrorStatus(
                    view.status(),
                    bundle.getString("teacher.studentDetails.error.save") + ": " + exception.getMessage()
            );
            LOGGER.log(
                    Level.SEVERE,
                    exception,
                    () -> "Failed to save feedback for student number: " + studentNumber
            );
        }
    }

    private void handleDelete(
            ResourceBundle bundle,
            String languageCode,
            boolean canEditFeedback,
            StudentDetailsView view
    ) {
        try {
            if (!canEditFeedback) {
                showErrorStatus(
                        view.status(),
                        bundle.getString("teacher.studentDetails.error.onlyClassTeacherDelete")
                );
                return;
            }

            String current = view.feedbackArea().getText() == null
                    ? ""
                    : view.feedbackArea().getText().trim();

            if (current.isEmpty()) {
                view.btnDelete().setDisable(true);
                showErrorStatus(
                        view.status(),
                        bundle.getString("teacher.studentDetails.status.noFeedbackToDelete")
                );
                return;
            }

            if (!confirmDelete(bundle)) {
                return;
            }

            controller.deleteFeedback(studentNumber, Session.getUserId(), languageCode);

            view.feedbackArea().clear();
            view.feedbackArea().setEditable(true);
            view.feedbackArea().requestFocus();

            view.btnSave().setDisable(false);
            view.btnDelete().setDisable(true);

            showSuccessStatus(
                    view.status(),
                    bundle.getString("teacher.studentDetails.status.deleted")
            );

        } catch (Exception exception) {
            showErrorStatus(
                    view.status(),
                    bundle.getString("teacher.studentDetails.error.delete") + ": " + exception.getMessage()
            );
            LOGGER.log(
                    Level.SEVERE,
                    exception,
                    () -> "Failed to delete feedback for student number: " + studentNumber
            );
        }
    }

    private boolean confirmDelete(ResourceBundle bundle) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(bundle.getString("teacher.studentDetails.deleteDialog.title"));
        confirm.setHeaderText(bundle.getString("teacher.studentDetails.deleteDialog.header"));
        confirm.setContentText(bundle.getString("teacher.studentDetails.deleteDialog.content"));

        var result = confirm.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showErrorStatus(Label status, String message) {
        status.setTextFill(Color.DARKRED);
        status.setText(message);
    }

    private void showSuccessStatus(Label status, String message) {
        status.setTextFill(Color.FORESTGREEN);
        status.setText(message);
    }

    private void addRow(GridPane grid, int row, String subject, Label markLabel) {
        Label subjectLabel = new Label(subject);
        subjectLabel.getStyleClass().add("table-cell");

        markLabel.getStyleClass().add("table-cell");

        grid.add(subjectLabel, 0, row);
        grid.add(markLabel, 1, row);
    }

    private boolean isMathSubject(String subject) {
        if (subject == null) {
            return false;
        }
        String value = subject.trim().toLowerCase();
        return value.equals("maths")
                || value.contains("mathematics")
                || value.contains("math");
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private record StudentDetailsView(
            HBox header,
            StackPane center,
            Label lblName,
            Label lblEmail,
            Label totalChip,
            Label avgChip,
            Label m1,
            Label m2,
            Label m3,
            Label m4,
            Label m5,
            TextArea feedbackArea,
            Button btnEdit,
            Button btnSave,
            Button btnDelete,
            Label status
    ) {
    }
}