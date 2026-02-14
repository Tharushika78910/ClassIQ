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

        // Root fills the available space
        StackPane root = new StackPane();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("page-bg"); // keep your background style

        // Center container (this will be in the middle)
        VBox centerBox = new VBox(15);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setMaxWidth(650);

        // Title (optional) - remove if you don’t want it
        Label title = new Label("Students Info");
        title.setFont(Font.font(26));
        title.setTextFill(Color.BLACK);

        // Table
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

        // Search row
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

        // Load table data
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

        // Click row -> fill textbox
        table.setOnMouseClicked(e -> {
            StudentRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                searchField.setText(selected.getStudentNumber());
            }
        });

        // Enter button -> go next interface
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

        // Put everything in the center
        centerBox.getChildren().addAll(title, table, searchRow, status);
        root.getChildren().add(centerBox);

        return root;
    }

    // Table row model
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
