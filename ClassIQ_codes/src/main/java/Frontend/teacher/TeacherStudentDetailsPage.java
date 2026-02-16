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

        // FORCE ENABLE + EDITABLE (teacher must be able to type)
        feedbackArea.setDisable(false);
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

            // On open: EMPTY textbox so teacher can type NEW feedback immediately
            feedbackArea.clear();
            status.setText("");

        } catch (Exception ex) {
            status.setText("Load error: " + ex.getMessage());
            ex.printStackTrace();
        }

        // Edit: load existing feedback into box, then teacher types new and saves
        btnEdit.setOnAction(e -> {
            try {
                String fb = controller.loadFeedback(studentNumber);
                feedbackArea.setDisable(false);
                feedbackArea.setEditable(true);
                feedbackArea.setText(fb == null ? "" : fb);
                feedbackArea.requestFocus();
                feedbackArea.positionCaret(feedbackArea.getText().length());
                status.setText("Edit the feedback and press Save.");
            } catch (Exception ex) {
                status.setText("Edit load error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Save: save into student_marks.feed_back, then CLEAR textbox
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

                // clear after save
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

        //Green outer frame (like Image 1)
        StackPane greenFrame = new StackPane(card);


        greenFrame.setAlignment(Pos.CENTER);


        greenFrame.setPadding(new Insets(20));  // increase if you want more top/bottom space


        greenFrame.setMaxWidth(720);
        greenFrame.setPrefWidth(720);  // force width


        greenFrame.setStyle("""
            -fx-background-color: rgba(200, 230, 200, 0.70);
            -fx-background-radius: 18;
            -fx-border-color: rgba(60, 90, 60, 0.55);
            -fx-border-width: 2;
            -fx-border-radius: 18;
        """);

        root.getChildren().add(greenFrame);
        return root;
    }

    private void addRow(GridPane grid, int row, String subject, Label markLabel) {
        grid.add(new Label(subject), 0, row);
        grid.add(markLabel, 1, row);
    }
}
