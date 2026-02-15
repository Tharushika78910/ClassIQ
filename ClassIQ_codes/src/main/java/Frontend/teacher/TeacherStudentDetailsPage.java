package Frontend.teacher;

import Backend.controller.StudentDetailsController;
import Backend.model.dto.StudentDetailsDTO;
import Backend.model.entity.Student;
import Backend.model.entity.StudentMarks;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TeacherStudentDetailsPage {

    private final TeacherDashboard dashboard;
    private final String studentNumber;

    private final StudentDetailsController controller = new StudentDetailsController();

    // keep latest loaded feedback for edit mode
    private String lastSavedFeedback = "";

    public TeacherStudentDetailsPage(TeacherDashboard dashboard, String studentNumber) {
        this.dashboard = dashboard;
        this.studentNumber = studentNumber;
    }

    public Parent getView() {

        StackPane root = new StackPane();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("page-bg");

        VBox card = new VBox(14);
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.TOP_LEFT);
        card.setMaxWidth(700);

        card.setBackground(new Background(new BackgroundFill(
                Color.rgb(255, 255, 255, 0.78),
                new CornerRadii(18),
                Insets.EMPTY
        )));

        Label title = new Label("Student Details");
        title.setFont(Font.font(24));

        Label lblStudentNo = new Label("Student Number: " + studentNumber);
        lblStudentNo.setFont(Font.font(16));

        Label lblName = new Label("Name: ");
        lblName.setFont(Font.font(16));

        Label lblEmail = new Label("Email: ");
        lblEmail.setFont(Font.font(16));

        // Subjects + Marks grid
        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(10);

        Label subHead = new Label("Subjects");
        subHead.setFont(Font.font(16));
        Label markHead = new Label("Marks");
        markHead.setFont(Font.font(16));

        grid.add(subHead, 0, 0);
        grid.add(markHead, 1, 0);

        Label m1 = new Label("0");
        Label m2 = new Label("0");
        Label m3 = new Label("0");
        Label m4 = new Label("0");
        Label m5 = new Label("0");

        addRow(grid, 1, "Mathematics", m1);
        addRow(grid, 2, "English",     m2);
        addRow(grid, 3, "Science",     m3);
        addRow(grid, 4, "Craft",       m4);
        addRow(grid, 5, "Language",    m5);

        // Total + Average
        Label totalVal = new Label("0");
        Label avgVal = new Label("0");

        GridPane totals = new GridPane();
        totals.setHgap(12);
        totals.setVgap(8);
        totals.add(new Label("Total:"), 0, 0);
        totals.add(totalVal, 1, 0);
        totals.add(new Label("Average:"), 0, 1);
        totals.add(avgVal, 1, 1);

        // Feedback
        Label fbLbl = new Label("Feedback");
        fbLbl.setFont(Font.font(16));

        TextArea feedbackArea = new TextArea();
        feedbackArea.setPrefRowCount(3);
        feedbackArea.setWrapText(true);

        // Required behavior:
        // - first teacher should type => we let edit mode be ON by default if feedback empty
        // - after saving => clear and lock
        feedbackArea.setEditable(true);

        Button btnSave = new Button("Save");
        Button btnEdit = new Button("Edit");
        Button btnBack = new Button("Back");
        Button btnDashboard = new Button("Dashboard");

        Label status = new Label();
        status.setTextFill(Color.DARKRED);

        HBox buttons = new HBox(10, btnSave, btnEdit, btnBack, btnDashboard);
        buttons.setAlignment(Pos.CENTER_LEFT);

        // ===== Load data from DB =====
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

            totalVal.setText(String.valueOf(mk.getTotal()));
            avgVal.setText(String.format("%.2f", mk.getAverage()));

            lastSavedFeedback = mk.getFeedback() == null ? "" : mk.getFeedback();


            // If feedback already exists -> show empty box and lock (teacher must press Edit)
            // If feedback empty -> allow typing immediately
            if (lastSavedFeedback.isBlank()) {
                feedbackArea.clear();
                feedbackArea.setEditable(true);
                status.setText("");
            } else {
                feedbackArea.clear();          // do NOT show old feedback in box
                feedbackArea.setEditable(false); // teacher must press Edit
                status.setText("Press Edit to update existing feedback.");
            }

        } catch (Exception ex) {
            status.setText("Load error: " + ex.getMessage());
            ex.printStackTrace();
        }

        //  load existing feedback and unlock
        btnEdit.setOnAction(e -> {
            try {
                StudentDetailsDTO dto = controller.getDetails(studentNumber);
                String fb = dto.getMarks().getFeedback();
                lastSavedFeedback = (fb == null ? "" : fb);

                feedbackArea.setText(lastSavedFeedback);
                feedbackArea.setEditable(true);
                feedbackArea.requestFocus();
                feedbackArea.positionCaret(feedbackArea.getText().length());

                status.setText("Editing enabled.");
            } catch (Exception ex) {
                status.setText("Edit load error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        //save to DB, then clear textbox and lock it
        btnSave.setOnAction(e -> {
            try {
                String newFb = feedbackArea.getText();

                controller.saveFeedback(studentNumber, newFb);

                // update last saved value
                lastSavedFeedback = newFb == null ? "" : newFb;

                // clear and lock after save
                feedbackArea.clear();
                feedbackArea.setEditable(false);

                status.setText("Feedback saved. Press Edit to change.");
            } catch (Exception ex) {
                status.setText("Save error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnBack.setOnAction(e ->
                dashboard.showPage(new TeacherStudentsInfoPage(dashboard).getView())
        );

        btnDashboard.setOnAction(e -> dashboard.showHome());

        card.getChildren().addAll(
                title,
                lblStudentNo,
                lblName,
                lblEmail,
                new Separator(),
                grid,
                new Separator(),
                totals,
                fbLbl,
                feedbackArea,
                buttons,
                status
        );

        root.getChildren().add(card);
        return root;
    }

    private void addRow(GridPane grid, int row, String subject, Label markLabel) {
        grid.add(new Label(subject), 0, row);
        grid.add(markLabel, 1, row);
    }
}
