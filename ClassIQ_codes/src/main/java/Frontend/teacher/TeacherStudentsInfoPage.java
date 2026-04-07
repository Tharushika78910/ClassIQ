package Frontend.teacher;

import Backend.controller.StudentInfoController;
import Backend.model.entity.Student;
import Frontend.Session;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Locale;
import java.util.ResourceBundle;

public class TeacherStudentsInfoPage {

    private final TeacherDashboard dashboard;
    private final StudentInfoController controller = new StudentInfoController();

    public TeacherStudentsInfoPage(TeacherDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Parent getView() {

        String languageCode = Session.getCurrentLocale().getLanguage();
        Locale locale = Session.getCurrentLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        boolean isArabic = "ar".equalsIgnoreCase(languageCode);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(18));
        root.getStyleClass().add("page-bg");
        root.setNodeOrientation(isArabic ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);

        Label headerTitle = new Label(bundle.getString("teacher.students.title"));
        headerTitle.getStyleClass().addAll("header-title");

        Label headerSub = new Label(bundle.getString("teacher.students.subtitle"));
        headerSub.getStyleClass().add("subtitle");

        VBox titleBox = new VBox(2, headerTitle, headerSub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(12, titleBox, spacer);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");

        root.setTop(header);

        VBox card = new VBox(14);
        card.setPadding(new Insets(16));
        card.setMaxWidth(760);
        card.getStyleClass().add("card");

        Label sectionTitle = new Label(bundle.getString("teacher.students.sectionTitle"));
        sectionTitle.getStyleClass().add("section-title");

        TableView<StudentRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPrefHeight(420);
        table.getStyleClass().add("app-table");

        TableColumn<StudentRow, String> colNumber =
                new TableColumn<>(bundle.getString("teacher.students.col.studentNumber"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        colNumber.setMinWidth(180);

        TableColumn<StudentRow, String> colName =
                new TableColumn<>(bundle.getString("teacher.students.col.fullName"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colName.setMinWidth(380);

        table.getColumns().setAll(colNumber, colName);
        table.setPlaceholder(new Label(bundle.getString("teacher.students.noStudents")));

        table.setRowFactory(tv -> {
            TableRow<StudentRow> row = new TableRow<>();

            row.hoverProperty().addListener((obs, wasHover, isHover) -> {
                if (!row.isEmpty() && !row.isSelected()) {
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
            for (Student s : controller.getAllStudentsBasic(languageCode)) {
                String fullName = s.getFullName() == null ? "" : s.getFullName().trim();
                rows.add(new StudentRow(s.getStudentNumber(), fullName));
            }
        } catch (Exception ex) {
            status.setText(bundle.getString("teacher.students.error.load"));
            ex.printStackTrace();
        }

        FilteredList<StudentRow> filtered = new FilteredList<>(rows, r -> true);
        table.setItems(filtered);

        Label searchIcon = new Label("🔎");
        searchIcon.getStyleClass().add("muted-text");

        TextField searchField = new TextField();
        searchField.setPromptText(bundle.getString("teacher.students.searchPrompt"));
        searchField.getStyleClass().add("input");

        Button clearBtn = new Button(bundle.getString("teacher.students.clear"));
        clearBtn.getStyleClass().add("secondary-btn");

        Button openBtn = new Button(bundle.getString("teacher.students.openProfile"));
        openBtn.getStyleClass().add("primary-btn");

        openBtn.disableProperty().bind(
                Bindings.createBooleanBinding(() ->
                                table.getSelectionModel().getSelectedItem() == null &&
                                        (searchField.getText() == null || searchField.getText().trim().isEmpty()),
                        searchField.textProperty(),
                        table.getSelectionModel().selectedItemProperty())
        );

        HBox searchBar = new HBox(10, searchIcon, searchField, clearBtn, openBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);

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

            if (!q.isEmpty()) {
                table.getSelectionModel().clearSelection();
            }
        });

        clearBtn.setOnAction(e -> {
            searchField.clear();
            table.getSelectionModel().clearSelection();
            filtered.setPredicate(r -> true);
            status.setText("");
        });

        table.setOnMouseClicked(e -> status.setText(""));

        Runnable goNext = () -> {
            StudentRow selected = table.getSelectionModel().getSelectedItem();
            String input = searchField.getText() == null ? "" : searchField.getText().trim();

            String studentNumberToOpen;
            if (selected != null) {
                studentNumberToOpen = selected.getStudentNumber();
            } else if (!input.isEmpty()) {
                studentNumberToOpen = input;
            } else {
                status.setText(bundle.getString("teacher.students.error.enterStudent"));
                return;
            }

            try {
                Student s = controller.findStudentByNumber(studentNumberToOpen, languageCode);
                if (s == null) {
                    status.setText(bundle.getString("teacher.students.error.notFound") + " " + studentNumberToOpen);
                    return;
                }

                status.setText("");
                dashboard.showTeacherStudentDetailsPage(s.getStudentNumber());

            } catch (Exception ex) {
                status.setText(ex.getMessage() == null
                        ? bundle.getString("teacher.students.error.load")
                        : ex.getMessage());
                ex.printStackTrace();
            }
        };

        openBtn.setOnAction(e -> goNext.run());
        searchField.setOnAction(e -> goNext.run());

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