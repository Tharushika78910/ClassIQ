package Frontend.teacher;

import Backend.controller.StudentInfoController;
import Backend.model.entity.Student;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TeacherStudentsInfoPage {

    private final TeacherDashboard dashboard;
    private final StudentInfoController controller = new StudentInfoController();

    public TeacherStudentsInfoPage(TeacherDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Parent getView() {

        // ==========================
        // ROOT
        // ==========================
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(18));
        root.getStyleClass().add("page-bg");

        // ==========================
        // HEADER BAR (Professional)
        // ==========================
        Label headerTitle = new Label("Students");
        headerTitle.getStyleClass().addAll("header-title");

        Label headerSub = new Label("Search and open a student profile");
        headerSub.getStyleClass().add("subtitle");

        VBox titleBox = new VBox(2, headerTitle, headerSub);

        // ✅ REMOVED: back button (TeacherDashboard wrapper provides it)

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("logout-btn");
        logoutBtn.setOnAction(e -> dashboard.showPage(new Label("Logged out (placeholder)")));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ✅ backBtn removed from header
        HBox header = new HBox(12, titleBox, spacer, logoutBtn);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");

        root.setTop(header);

        VBox card = new VBox(14);
        card.setPadding(new Insets(16));
        card.setMaxWidth(760);
        card.getStyleClass().add("card");

        TableView<StudentRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPrefHeight(420);
        table.getStyleClass().add("app-table");

        TableColumn<StudentRow, String> colNumber = new TableColumn<>("Student Number");
        colNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        colNumber.setMinWidth(180);

        TableColumn<StudentRow, String> colName = new TableColumn<>("Full Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colName.setMinWidth(380);

        table.getColumns().setAll(colNumber, colName);
        table.setPlaceholder(new Label("No students found."));

        // Row hover feels more “modern”
        table.setRowFactory(tv -> {
            TableRow<StudentRow> row = new TableRow<>();
            row.hoverProperty().addListener((obs, wasHover, isHover) -> {
                if (!row.isEmpty()) {
                    row.setStyle(isHover
                            ? "-fx-background-color: rgba(156,195,174,0.25);"
                            : "");
                }
            });
            return row;
        });

        ObservableList<StudentRow> rows = FXCollections.observableArrayList();
        Label status = new Label();
        status.setTextFill(Color.DARKRED);

        try {
            for (Student s : controller.getAllStudentsBasic()) {
                String fullName = (s.getFirstName() == null ? "" : s.getFirstName()) + " " +
                        (s.getLastName() == null ? "" : s.getLastName());
                rows.add(new StudentRow(
                        s.getStudentNumber(),
                        fullName.trim()
                ));
            }
        } catch (Exception ex) {
            status.setText("Error loading student list.");
            ex.printStackTrace();
        }

        FilteredList<StudentRow> filtered = new FilteredList<>(rows, r -> true);
        table.setItems(filtered);

        Label searchIcon = new Label("🔎");
        searchIcon.getStyleClass().add("muted-text");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by Student Number (e.g., S1001) or Name");
        searchField.getStyleClass().add("input");

        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("secondary-btn");

        Button openBtn = new Button("Open Profile");
        openBtn.getStyleClass().add("primary-btn");

        // Disable open when empty
        openBtn.disableProperty().bind(
                Bindings.createBooleanBinding(() ->
                                searchField.getText() == null || searchField.getText().trim().isEmpty(),
                        searchField.textProperty())
        );

        HBox searchBar = new HBox(10, searchIcon, searchField, clearBtn, openBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        // live filter while typing (by ID or name)
        searchField.textProperty().addListener((obs, old, text) -> {
            String q = text == null ? "" : text.trim().toLowerCase();
            if (q.isEmpty()) {
                filtered.setPredicate(r -> true);
            } else {
                filtered.setPredicate(r ->
                        r.getStudentNumber().toLowerCase().contains(q) ||
                                r.getFullName().toLowerCase().contains(q)
                );
            }
        });

        clearBtn.setOnAction(e -> {
            searchField.clear();
            table.getSelectionModel().clearSelection();
            status.setText("");
        });

        // click row fills search
        table.setOnMouseClicked(e -> {
            StudentRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                searchField.setText(selected.getStudentNumber());
            }
        });

        Runnable goNext = () -> {
            String input = searchField.getText() == null ? "" : searchField.getText().trim();
            if (input.isEmpty()) {
                status.setText("Please enter a student number or name.");
                return;
            }

            StudentRow selected = table.getSelectionModel().getSelectedItem();
            String studentNumberToOpen = selected != null ? selected.getStudentNumber() : input;

            try {
                Student s = controller.findStudentByNumber(studentNumberToOpen);
                if (s == null) {
                    status.setText("Student not found: " + studentNumberToOpen);
                    return;
                }

                status.setText("");
                dashboard.showPage(new TeacherStudentDetailsPage(dashboard, s.getStudentNumber()).getView());

            } catch (Exception ex) {
                status.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        };

        openBtn.setOnAction(e -> goNext.run());
        searchField.setOnAction(e -> goNext.run());

        // ==========================
        // BUILD CARD
        // ==========================
        Label sectionTitle = new Label("All Students");
        sectionTitle.getStyleClass().add("section-title");

        card.getChildren().addAll(sectionTitle, table, searchBar, status);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(22));
        root.setCenter(center);

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