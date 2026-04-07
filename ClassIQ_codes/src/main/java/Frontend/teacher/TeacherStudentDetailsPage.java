package Frontend.teacher;

import Backend.controller.StudentDetailsController;
import Backend.model.dto.StudentDetailsDTO;
import Backend.model.entity.Student;
import Backend.model.entity.StudentMarks;
import Frontend.Session;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Locale;
import java.util.ResourceBundle;

public class TeacherStudentDetailsPage {

    private final TeacherDashboard dashboard;
    private final String studentNumber;

    private final StudentDetailsController controller = new StudentDetailsController();

    public TeacherStudentDetailsPage(TeacherDashboard dashboard, String studentNumber) {
        this.dashboard = dashboard;
        this.studentNumber = studentNumber;
    }

    public Parent getView() {

        String languageCode = Session.getCurrentLocale().getLanguage();
        Locale locale = Session.getCurrentLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        boolean isArabic = "ar".equalsIgnoreCase(languageCode);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));
        root.getStyleClass().add("page-bg");
        root.setNodeOrientation(isArabic ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);

        Label headerTitle = new Label(bundle.getString("teacher.studentDetails.title"));
        headerTitle.getStyleClass().add("header-title");

        Label headerSub = new Label(bundle.getString("teacher.studentDetails.subtitle"));
        headerSub.getStyleClass().add("subtitle");

        VBox titleBox = new VBox(2, headerTitle, headerSub);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(titleBox);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");

        root.setTop(header);

        HBox card = new HBox(18);
        card.setPadding(new Insets(18));
        card.setMaxWidth(900);
        card.getStyleClass().add("details-card");

        VBox left = new VBox(10);
        left.getStyleClass().add("details-left");
        left.setPrefWidth(300);

        Label profileTitle = new Label(bundle.getString("teacher.studentDetails.profile"));
        profileTitle.getStyleClass().add("section-title");

        Label lblStudentNo = new Label(bundle.getString("teacher.studentDetails.studentNumber") + ": " + studentNumber);
        lblStudentNo.getStyleClass().add("info-text");

        Label lblName = new Label(bundle.getString("teacher.studentDetails.name") + ": ");
        lblName.getStyleClass().add("info-text");

        Label lblEmail = new Label(bundle.getString("teacher.studentDetails.email") + ": ");
        lblEmail.getStyleClass().add("info-text");

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
        marksTitle.getStyleClass().add("section-title");

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

        Label fbLbl = new Label(bundle.getString("teacher.studentDetails.feedback"));
        fbLbl.getStyleClass().add("section-title");

        TextArea feedbackArea = new TextArea();
        feedbackArea.setPrefRowCount(4);
        feedbackArea.setWrapText(true);
        feedbackArea.getStyleClass().add("feedback-area");
        feedbackArea.setEditable(false);

        Button btnSave = new Button(bundle.getString("teacher.studentDetails.save"));
        btnSave.getStyleClass().add("primary-btn");
        btnSave.setDisable(true);

        Button btnEdit = new Button(bundle.getString("teacher.studentDetails.edit"));
        btnEdit.getStyleClass().add("primary-btn");

        Button btnDelete = new Button(bundle.getString("teacher.studentDetails.delete"));
        btnDelete.getStyleClass().add("primary-btn");
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
                fbLbl,
                feedbackArea,
                actions,
                status
        );

        Separator vSep = new Separator(Orientation.VERTICAL);
        vSep.getStyleClass().add("v-sep");

        card.getChildren().addAll(left, vSep, right);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(18));
        root.setCenter(center);

        final boolean canEditFeedback = isMathSubject(Session.getTeacherSubject());

        try {
            StudentDetailsDTO dto = controller.getDetails(studentNumber, languageCode);
            Student s = dto.getStudent();
            StudentMarks mk = dto.getMarks();

            lblName.setText(bundle.getString("teacher.studentDetails.name") + ": "
                    + safe(s.getFirstName()) + " " + safe(s.getLastName()));

            lblEmail.setText(bundle.getString("teacher.studentDetails.email") + ": "
                    + safe(s.getEmail()));

            m1.setText(String.valueOf(mk.getSubject1()));
            m2.setText(String.valueOf(mk.getSubject2()));
            m3.setText(String.valueOf(mk.getSubject3()));
            m4.setText(String.valueOf(mk.getSubject4()));
            m5.setText(String.valueOf(mk.getSubject5()));

            totalChip.setText(bundle.getString("teacher.studentDetails.total") + ": " + mk.getTotal());
            avgChip.setText(String.format(locale, "%s: %.2f",
                    bundle.getString("teacher.studentDetails.average"),
                    mk.getAverage()));

            String fb = controller.loadFeedback(studentNumber);
            feedbackArea.setText(fb == null ? "" : fb);

            if (!canEditFeedback) {
                btnEdit.setDisable(true);
                btnSave.setDisable(true);
                feedbackArea.setEditable(false);

                btnDelete.setDisable(true);
                btnDelete.setVisible(false);
                btnDelete.setManaged(false);

                status.setText(bundle.getString("teacher.studentDetails.status.onlyClassTeacherEdit"));
                status.setTextFill(Color.DARKRED);
            } else {
                btnEdit.setDisable(false);
                btnSave.setDisable(false);
                feedbackArea.setEditable(true);
                feedbackArea.requestFocus();

                boolean hasFeedback = feedbackArea.getText() != null && !feedbackArea.getText().trim().isEmpty();
                btnDelete.setDisable(!hasFeedback);

                status.setText(bundle.getString("teacher.studentDetails.status.typeAndSave"));
                status.setTextFill(Color.DARKRED);
            }

        } catch (Exception ex) {
            status.setText(bundle.getString("teacher.studentDetails.error.load") + ": " + ex.getMessage());
            status.setTextFill(Color.DARKRED);
            System.err.println(ex.getMessage());
        }

        btnEdit.setOnAction(e -> {
            if (!canEditFeedback) {
                status.setText(bundle.getString("teacher.studentDetails.status.onlyClassTeacherEdit"));
                status.setTextFill(Color.DARKRED);
                return;
            }

            feedbackArea.setEditable(true);
            btnSave.setDisable(false);
            feedbackArea.requestFocus();
            status.setText(bundle.getString("teacher.studentDetails.status.editAndSave"));
            status.setTextFill(Color.DARKRED);
        });

        btnSave.setOnAction(e -> {
            try {
                if (!canEditFeedback) {
                    status.setText(bundle.getString("teacher.studentDetails.error.onlyClassTeacherSave"));
                    status.setTextFill(Color.DARKRED);
                    return;
                }

                String newFb = feedbackArea.getText() == null ? "" : feedbackArea.getText().trim();

                if (newFb.isEmpty()) {
                    status.setText(bundle.getString("teacher.studentDetails.error.emptyFeedback"));
                    status.setTextFill(Color.DARKRED);
                    return;
                }

                if (newFb.length() > 255) {
                    status.setText(bundle.getString("teacher.studentDetails.error.feedbackTooLong"));
                    status.setTextFill(Color.DARKRED);
                    return;
                }

                controller.saveFeedback(studentNumber, newFb, Session.getUserId(), languageCode);

                feedbackArea.setEditable(false);
                btnSave.setDisable(true);
                btnDelete.setDisable(false);

                status.setTextFill(Color.FORESTGREEN);
                status.setText(bundle.getString("teacher.studentDetails.status.saved"));

            } catch (Exception ex) {
                status.setTextFill(Color.DARKRED);
                status.setText(bundle.getString("teacher.studentDetails.error.save") + ": " + ex.getMessage());
                System.err.println(ex.getMessage());
            }
        });

        btnDelete.setOnAction(e -> {
            try {
                if (!canEditFeedback) {
                    status.setText(bundle.getString("teacher.studentDetails.error.onlyClassTeacherDelete"));
                    status.setTextFill(Color.DARKRED);
                    return;
                }

                String current = feedbackArea.getText() == null ? "" : feedbackArea.getText().trim();
                if (current.isEmpty()) {
                    btnDelete.setDisable(true);
                    status.setText(bundle.getString("teacher.studentDetails.status.noFeedbackToDelete"));
                    status.setTextFill(Color.DARKRED);
                    return;
                }

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle(bundle.getString("teacher.studentDetails.deleteDialog.title"));
                confirm.setHeaderText(bundle.getString("teacher.studentDetails.deleteDialog.header"));
                confirm.setContentText(bundle.getString("teacher.studentDetails.deleteDialog.content"));

                var result = confirm.showAndWait();
                if (result.isEmpty() || result.get() != ButtonType.OK) {
                    return;
                }

                controller.deleteFeedback(studentNumber, Session.getUserId(), languageCode);

                feedbackArea.clear();
                feedbackArea.setEditable(true);
                feedbackArea.requestFocus();

                btnSave.setDisable(false);
                btnDelete.setDisable(true);

                status.setTextFill(Color.FORESTGREEN);
                status.setText(bundle.getString("teacher.studentDetails.status.deleted"));

            } catch (Exception ex) {
                status.setTextFill(Color.DARKRED);
                status.setText(bundle.getString("teacher.studentDetails.error.delete") + ": " + ex.getMessage());
                System.err.println(ex.getMessage());
            }
        });

        return root;
    }

    private void addRow(GridPane grid, int row, String subject, Label markLabel) {
        Label subjectLbl = new Label(subject);
        subjectLbl.getStyleClass().add("table-cell");

        markLabel.getStyleClass().add("table-cell");

        grid.add(subjectLbl, 0, row);
        grid.add(markLabel, 1, row);
    }

    private boolean isMathSubject(String subject) {
        if (subject == null) return false;
        String s = subject.trim().toLowerCase();
        return s.equals("maths")
                || s.contains("mathematics")
                || s.contains("math");
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}