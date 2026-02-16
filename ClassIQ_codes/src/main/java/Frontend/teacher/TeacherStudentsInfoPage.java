package Frontend.teacher;

import Backend.controller.StudentInfoController;
import Backend.model.entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TeacherStudentsInfoPage {

    private final TeacherDashboard dashboard;
    private final StudentInfoController controller = new StudentInfoController();

    public TeacherStudentsInfoPage(TeacherDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Parent getView() {

        // ==========================
        // ROOT (BorderPane so we can add bottom buttons)
        // ==========================
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("page-bg"); // keep your background style

        // ==========================
        // CENTER CONTENT (your existing UI - unchanged)
        // ==========================
        StackPane centerWrap = new StackPane();
        centerWrap.setPadding(new Insets(20));

        VBox centerBox = new VBox(15);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setMaxWidth(650);

        Label title = new Label("Students Info");
        title.setFont(Font.font(26));
        title.setTextFill(Color.BLACK);

        TableView<StudentRow> table = new TableView<>();
        table.setPrefHeight(320);
        table.setMaxWidth(650);

        TableColumn<StudentRow, String> colNumber = new TableColumn<>("Student Number");
        colNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        colNumber.setPrefWidth(200);

        TableColumn<StudentRow, String> colName = new TableColumn<>("Full Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colName.setPrefWidth(430);

        table.getColumns().addAll(colNumber, colName);

        HBox searchRow = new HBox(12);
        searchRow.setAlignment(Pos.CENTER);

        Label searchLbl = new Label("Search student");
        searchLbl.setFont(Font.font(16));

        TextField searchField = new TextField();
        searchField.setPromptText("Enter Student Number (ex: S1001)");
        searchField.setPrefWidth(320);

        Button enterBtn = new Button("Enter");
        Label status = new Label();
        status.setTextFill(Color.DARKRED);

        searchRow.getChildren().addAll(searchLbl, searchField, enterBtn);

        ObservableList<StudentRow> rows = FXCollections.observableArrayList();
        try {
            for (Student s : controller.getAllStudentsBasic()) {
                rows.add(new StudentRow(
                        s.getStudentNumber(),
                        s.getFirstName() + " " + s.getLastName()
                ));
            }
            table.setItems(rows);
        } catch (Exception ex) {
            status.setText("Error loading student list.");
            ex.printStackTrace();
        }

        table.setOnMouseClicked(e -> {
            StudentRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                searchField.setText(selected.getStudentNumber());
            }
        });

        Runnable goNext = () -> {
            String sn = searchField.getText() == null ? "" : searchField.getText().trim();
            if (sn.isEmpty()) {
                status.setText("Please enter student number.");
                return;
            }

            try {
                Student s = controller.findStudentByNumber(sn);
                if (s == null) {
                    status.setText("Student not found: " + sn);
                    return;
                }

                status.setText("");
                dashboard.showPage(new TeacherStudentDetailsPage(dashboard, s.getStudentNumber()).getView());

            } catch (Exception ex) {
                status.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        };

        enterBtn.setOnAction(e -> goNext.run());
        searchField.setOnAction(e -> goNext.run());

        centerBox.getChildren().addAll(title, table, searchRow, status);
        centerWrap.getChildren().add(centerBox);

        root.setCenter(centerWrap);

        // ==========================
        // BOTTOM BAR (Back + Logout)  ✅ added
        // ==========================
        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("secondary-btn");
        btnBack.setOnAction(e -> dashboard.showHome());

        Button btnLogout = new Button("Logout");
        btnLogout.getStyleClass().add("logout-btn"); // uses your CSS
        btnLogout.setOnAction(e -> dashboard.showPage(new Label("Logged out (placeholder)")));

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

    public static class StudentRow {
        private final String studentNumber;
        private final String fullName;

        public StudentRow(String studentNumber, String fullName) {
            this.studentNumber = studentNumber;
            this.fullName = fullName;
        }

        public String getStudentNumber() {
            return studentNumber;
        }

        public String getFullName() {
            return fullName;
        }
    }
}
