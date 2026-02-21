package Frontend.teacher;

import Backend.controller.StudentDetailsController;
import Backend.model.dto.StudentDetailsDTO;
import Backend.model.entity.Student;
import Backend.model.entity.StudentMarks;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TeacherStudentDetailsPage {

    private final TeacherDashboard dashboard;
    private final String studentNumber;

    private final StudentDetailsController controller = new StudentDetailsController();

    public TeacherStudentDetailsPage(TeacherDashboard dashboard, String studentNumber) {
        this.dashboard = dashboard;
        this.studentNumber = studentNumber;
    }

    public Parent getView() {


        // ROOT

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));
        root.getStyleClass().add("page-bg");


        // HEADER

        Label headerTitle = new Label("Student Details");
        headerTitle.getStyleClass().add("header-title");

        Label headerSub = new Label("View marks and write feedback");
        headerSub.getStyleClass().add("subtitle");

        VBox titleBox = new VBox(2, headerTitle, headerSub);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(titleBox);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");

        root.setTop(header);


        // MAIN CARD

        HBox card = new HBox(18);
        card.setPadding(new Insets(18));
        card.setMaxWidth(900);
        card.getStyleClass().add("details-card");

        // LEFT COLUMN
        VBox left = new VBox(10);
        left.getStyleClass().add("details-left");
        left.setPrefWidth(300);

        Label profileTitle = new Label("Student Profile");
        profileTitle.getStyleClass().add("section-title");

        Label lblStudentNo = new Label("Student Number: " + studentNumber);
        lblStudentNo.getStyleClass().add("info-text");

        Label lblName = new Label("Name: ");
        lblName.getStyleClass().add("info-text");

        Label lblEmail = new Label("Email: ");
        lblEmail.getStyleClass().add("info-text");

        Label totalChip = new Label("Total: 0");
        totalChip.getStyleClass().add("info-chip");

        Label avgChip = new Label("Average: 0.00");
        avgChip.getStyleClass().add("info-chip");

        VBox chips = new VBox(8, totalChip, avgChip);
        chips.setPadding(new Insets(6, 0, 0, 0));

        left.getChildren().addAll(profileTitle, lblStudentNo, lblName, lblEmail, new Separator(), chips);

        // RIGHT COLUMN
        VBox right = new VBox(14);
        right.getStyleClass().add("details-right");
        right.setFillWidth(true);

        Label marksTitle = new Label("Marks");
        marksTitle.getStyleClass().add("section-title");

        GridPane grid = new GridPane();
        grid.getStyleClass().add("marks-grid");
        grid.setHgap(30);
        grid.setVgap(10);

        Label subHead = new Label("Subject");
        subHead.getStyleClass().add("table-head");

        Label markHead = new Label("Marks");
        markHead.getStyleClass().add("table-head");

        grid.add(subHead, 0, 0);
        grid.add(markHead, 1, 0);

        Label m1 = new Label("0");
        Label m2 = new Label("0");
        Label m3 = new Label("0");
        Label m4 = new Label("0");
        Label m5 = new Label("0");

        addRow(grid, 1, "Mathematics", m1);
        addRow(grid, 2, "English", m2);
        addRow(grid, 3, "Science", m3);
        addRow(grid, 4, "Craft", m4);
        addRow(grid, 5, "Language", m5);

        Label fbLbl = new Label("Feedback");
        fbLbl.getStyleClass().add("section-title");

        TextArea feedbackArea = new TextArea();
        feedbackArea.setPrefRowCount(4);
        feedbackArea.setWrapText(true);
        feedbackArea.getStyleClass().add("feedback-area");

        Button btnSave = new Button("Save");
        btnSave.getStyleClass().add("primary-btn");

        Button btnEdit = new Button("Edit");
        btnEdit.getStyleClass().add("primary-btn");

        HBox actions = new HBox(10, btnEdit, btnSave);
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


        // BOTTOM LEFT BUTTONS

        Button btnBack = new Button("← Back");
        btnBack.getStyleClass().add("back-pill-btn");

        Button btnDashboard = new Button("Dashboard");
        btnDashboard.getStyleClass().add("back-pill-btn");

        HBox bottomBar = new HBox(10, btnBack, btnDashboard);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(10, 0, 0, 10));

        root.setBottom(bottomBar);


        // LOGIC

        try {
            StudentDetailsDTO dto = controller.getDetails(studentNumber);
            Student s = dto.getStudent();
            StudentMarks mk = dto.getMarks();

            lblName.setText("Name: " + s.getFirstName() + " " + s.getLastName());
            lblEmail.setText("Email: " + (s.getEmail() == null ? "" : s.getEmail()));

            m1.setText(String.valueOf(mk.getSubject1()));
            m2.setText(String.valueOf(mk.getSubject2()));
            m3.setText(String.valueOf(mk.getSubject3()));
            m4.setText(String.valueOf(mk.getSubject4()));
            m5.setText(String.valueOf(mk.getSubject5()));

            totalChip.setText("Total: " + mk.getTotal());
            avgChip.setText(String.format("Average: %.2f", mk.getAverage()));

            feedbackArea.clear();
            status.setText("");

        } catch (Exception ex) {
            status.setText("Load error: " + ex.getMessage());
            ex.printStackTrace();
        }

        btnEdit.setOnAction(e -> {
            try {
                String fb = controller.loadFeedback(studentNumber);
                feedbackArea.setText(fb == null ? "" : fb);
                feedbackArea.requestFocus();
                status.setText("Edit the feedback and press Save.");
            } catch (Exception ex) {
                status.setText("Edit load error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnSave.setOnAction(e -> {
            try {
                String newFb = feedbackArea.getText() == null ? "" : feedbackArea.getText().trim();

                if (newFb.isEmpty()) {
                    status.setText("Please type feedback before saving.");
                    return;
                }

                if (newFb.length() > 255) {
                    status.setText("Feedback is too long (max 255 characters).");
                    return;
                }

                controller.saveFeedback(studentNumber, newFb);

                feedbackArea.clear();
                status.setText("Feedback saved successfully.");

            } catch (Exception ex) {
                status.setText("Save error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnBack.setOnAction(e ->
                dashboard.showPage(new TeacherStudentsInfoPage(dashboard).getView())
        );

        btnDashboard.setOnAction(e -> dashboard.showHome());

        return root;
    }

    private void addRow(GridPane grid, int row, String subject, Label markLabel) {
        Label subjectLbl = new Label(subject);
        subjectLbl.getStyleClass().add("table-cell");

        markLabel.getStyleClass().add("table-cell");

        grid.add(subjectLbl, 0, row);
        grid.add(markLabel, 1, row);
    }
}