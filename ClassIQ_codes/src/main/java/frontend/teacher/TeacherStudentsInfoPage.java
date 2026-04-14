package frontend.teacher;

import backend.controller.StudentInfoController;
import backend.model.entity.Student;
import frontend.Session;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
        Locale locale = Session.getCurrentLocale();
        String languageCode = locale.getLanguage();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        boolean isArabic = "ar".equalsIgnoreCase(languageCode);

        BorderPane root = createRoot(isArabic);
        root.setTop(createHeader(bundle));

        Label status = createStatusLabel();
        TableView<StudentRow> table = createTable(bundle);
        ObservableList<StudentRow> rows = loadStudentRows(languageCode, bundle, status);
        FilteredList<StudentRow> filtered = new FilteredList<>(rows, row -> true);
        table.setItems(filtered);

        TextField searchField = createSearchField(bundle);
        Button clearButton = createClearButton(bundle);
        Button openButton = createOpenButton(bundle);

        bindOpenButton(openButton, table, searchField);
        wireSearch(searchField, filtered, table);
        wireClear(clearButton, searchField, filtered, table, status);
        wireOpen(openButton, searchField, table, bundle, languageCode, status);
        wireTable(table, status);

        VBox card = createCard(bundle, table, searchField, clearButton, openButton, status);
        root.setCenter(wrapCenter(card));

        return root;
    }

    private BorderPane createRoot(boolean isArabic) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(18));
        root.getStyleClass().add("page-bg");
        root.setNodeOrientation(isArabic ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
        return root;
    }

    private HBox createHeader(ResourceBundle bundle) {
        Label headerTitle = new Label(bundle.getString("teacher.students.title"));
        headerTitle.getStyleClass().add("header-title");

        Label headerSub = new Label(bundle.getString("teacher.students.subtitle"));
        headerSub.getStyleClass().add("subtitle");

        VBox titleBox = new VBox(2, headerTitle, headerSub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(12, titleBox, spacer);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");
        return header;
    }

    private VBox createCard(
            ResourceBundle bundle,
            TableView<StudentRow> table,
            TextField searchField,
            Button clearButton,
            Button openButton,
            Label status
    ) {
        VBox card = new VBox(14);
        card.setPadding(new Insets(16));
        card.setMaxWidth(760);
        card.getStyleClass().add("card");

        Label sectionTitle = new Label(bundle.getString("teacher.students.sectionTitle"));
        sectionTitle.getStyleClass().add("section-title");

        Label searchIcon = new Label("🔎");
        searchIcon.getStyleClass().add("muted-text");

        HBox searchBar = new HBox(10, searchIcon, searchField, clearButton, openButton);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(sectionTitle, table, searchBar, status);
        return card;
    }

    private StackPane wrapCenter(VBox card) {
        StackPane center = new StackPane(card);
        center.setPadding(new Insets(22));
        return center;
    }

    private Label createStatusLabel() {
        Label status = new Label();
        status.setTextFill(Color.DARKRED);
        return status;
    }

    private TextField createSearchField(ResourceBundle bundle) {
        TextField searchField = new TextField();
        searchField.setPromptText(bundle.getString("teacher.students.searchPrompt"));
        searchField.getStyleClass().add("input");
        return searchField;
    }

    private Button createClearButton(ResourceBundle bundle) {
        Button clearButton = new Button(bundle.getString("teacher.students.clear"));
        clearButton.getStyleClass().add("secondary-btn");
        return clearButton;
    }

    private Button createOpenButton(ResourceBundle bundle) {
        Button openButton = new Button(bundle.getString("teacher.students.openProfile"));
        openButton.getStyleClass().add("primary-btn");
        return openButton;
    }

    private TableView<StudentRow> createTable(ResourceBundle bundle) {
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
        table.setRowFactory(tv -> createStyledRow());

        return table;
    }

    private TableRow<StudentRow> createStyledRow() {
        TableRow<StudentRow> row = new TableRow<>();

        row.hoverProperty().addListener((obs, wasHover, isHover) -> {
            boolean hovered = Boolean.TRUE.equals(isHover);

            if (!row.isEmpty() && !row.isSelected()) {
                row.setStyle(hovered ? "-fx-background-color: rgba(156,195,174,0.25);" : "");
            }
        });

        return row;
    }

    private ObservableList<StudentRow> loadStudentRows(
            String languageCode,
            ResourceBundle bundle,
            Label status
    ) {
        ObservableList<StudentRow> rows = FXCollections.observableArrayList();

        try {
            for (Student student : controller.getAllStudentsBasic(languageCode)) {
                String fullName = student.getFullName() == null ? "" : student.getFullName().trim();
                rows.add(new StudentRow(student.getStudentNumber(), fullName));
            }
        } catch (Exception exception) {
            status.setText(bundle.getString("teacher.students.error.load"));
            exception.printStackTrace();
        }

        return rows;
    }

    private void bindOpenButton(Button openButton, TableView<StudentRow> table, TextField searchField) {
        openButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> shouldDisableOpenButton(table, searchField),
                        searchField.textProperty(),
                        table.getSelectionModel().selectedItemProperty()
                )
        );
    }

    private boolean shouldDisableOpenButton(TableView<StudentRow> table, TextField searchField) {
        boolean noSelection = table.getSelectionModel().getSelectedItem() == null;
        String text = searchField.getText();
        boolean noSearchText = text == null || text.trim().isEmpty();
        return noSelection && noSearchText;
    }

    private void wireSearch(
            TextField searchField,
            FilteredList<StudentRow> filtered,
            TableView<StudentRow> table
    ) {
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            String query = newValue == null ? "" : newValue.trim().toLowerCase();

            filtered.setPredicate(row -> matchesQuery(row, query));

            if (!query.isEmpty()) {
                table.getSelectionModel().clearSelection();
            }
        });
    }

    private boolean matchesQuery(StudentRow row, String query) {
        if (query.isEmpty()) {
            return true;
        }
        return row.getStudentNumber().toLowerCase().contains(query)
                || row.getFullName().toLowerCase().contains(query);
    }

    private void wireClear(
            Button clearButton,
            TextField searchField,
            FilteredList<StudentRow> filtered,
            TableView<StudentRow> table,
            Label status
    ) {
        clearButton.setOnAction(event -> {
            searchField.clear();
            table.getSelectionModel().clearSelection();
            filtered.setPredicate(row -> true);
            status.setText("");
        });
    }

    private void wireTable(TableView<StudentRow> table, Label status) {
        table.setOnMouseClicked(event -> status.setText(""));
    }

    private void wireOpen(
            Button openButton,
            TextField searchField,
            TableView<StudentRow> table,
            ResourceBundle bundle,
            String languageCode,
            Label status
    ) {
        Runnable goNext = () -> openSelectedOrTypedStudent(searchField, table, bundle, languageCode, status);

        openButton.setOnAction(event -> goNext.run());
        searchField.setOnAction(event -> goNext.run());
    }

    private void openSelectedOrTypedStudent(
            TextField searchField,
            TableView<StudentRow> table,
            ResourceBundle bundle,
            String languageCode,
            Label status
    ) {
        StudentRow selected = table.getSelectionModel().getSelectedItem();
        String input = searchField.getText() == null ? "" : searchField.getText().trim();

        String studentNumberToOpen = resolveStudentNumberToOpen(selected, input, bundle, status);
        if (studentNumberToOpen == null) {
            return;
        }

        try {
            Student student = controller.findStudentByNumber(studentNumberToOpen, languageCode);
            if (student == null) {
                status.setText(bundle.getString("teacher.students.error.notFound") + " " + studentNumberToOpen);
                return;
            }

            status.setText("");
            dashboard.showTeacherStudentDetailsPage(student.getStudentNumber());

        } catch (Exception exception) {
            status.setText(
                    exception.getMessage() == null
                            ? bundle.getString("teacher.students.error.load")
                            : exception.getMessage()
            );
            exception.printStackTrace();
        }
    }

    private String resolveStudentNumberToOpen(
            StudentRow selected,
            String input,
            ResourceBundle bundle,
            Label status
    ) {
        if (selected != null) {
            return selected.getStudentNumber();
        }

        if (!input.isEmpty()) {
            return input;
        }

        status.setText(bundle.getString("teacher.students.error.enterStudent"));
        return null;
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