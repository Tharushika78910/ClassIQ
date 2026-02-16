package Frontend.student;

import Backend.model.dao.impl.MarksDaoImpl;
import Backend.model.entity.Student;
import Backend.model.entity.StudentMarks;
import Backend.service.GradeService;
import Frontend.Session;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StudentMyInfoPage {

    public Parent getView() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("page-bg");

        Label title = new Label("My Info");
        title.getStyleClass().add("title-xl");

        //  Get logged-in student from Session
        Student s = Session.getCurrentStudent();

        if (s == null) {
            Label err = new Label("No student session found. Please log in again.");
            err.getStyleClass().add("section-title");
            root.getChildren().addAll(title, err);
            return root;
        }

        String fullName = s.getFirstName() + " " + s.getLastName();

        VBox infoCard = new VBox(10);
        infoCard.getStyleClass().add("card");

        HBox infoRow = new HBox(30);

        VBox left = new VBox(6);
        left.getChildren().addAll(
                new Label("Name: " + fullName),
                new Label("Student No: " + (s.getStudentNumber() == null ? "" : s.getStudentNumber())),
                new Label("Student ID: " + s.getStudentId())
        );

        VBox right = new VBox(6);
        right.getChildren().addAll(
                new Label("Email: " + (s.getEmail() == null ? "" : s.getEmail()))
        );

        infoRow.getChildren().addAll(left, right);
        infoCard.getChildren().add(infoRow);

        Label marksTitle = new Label("Marks Summary");
        marksTitle.getStyleClass().add("section-title");

        VBox tableCard = new VBox(10);
        tableCard.getStyleClass().add("card");

        TableView<SummaryRow> table = new TableView<>();
        table.getStyleClass().add("app-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SummaryRow, String> colSub = new TableColumn<>("Subject");
        colSub.setCellValueFactory(new PropertyValueFactory<>("subject"));

        TableColumn<SummaryRow, Integer> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<SummaryRow, String> colGrade = new TableColumn<>("grade");
        colGrade.setText("Grade");
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        table.getColumns().addAll(colSub, colTotal, colGrade);

        // Load marks from DB
        MarksDaoImpl marksDao = new MarksDaoImpl();
        GradeService gradeService = new GradeService();

        StudentMarks m = null;
        try {
            m = marksDao.findByStudentId(s.getStudentId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // If no marks row exists yet, show zeros
        int math = (m == null) ? 0 : m.getSubject1();      // mathematics
        int eng  = (m == null) ? 0 : m.getSubject2();      // english
        int sci  = (m == null) ? 0 : m.getSubject3();      // science
        int craft= (m == null) ? 0 : m.getSubject4();      // craft
        int lang = (m == null) ? 0 : m.getSubject5();      // languages

        table.setItems(FXCollections.observableArrayList(
                new SummaryRow("Mathematics", math, gradeService.calculateGrade(math)),
                new SummaryRow("English", eng, gradeService.calculateGrade(eng)),
                new SummaryRow("Science", sci, gradeService.calculateGrade(sci)),
                new SummaryRow("Craft", craft, gradeService.calculateGrade(craft)),
                new SummaryRow("Languages", lang, gradeService.calculateGrade(lang))
        ));

        tableCard.getChildren().add(table);

        root.getChildren().addAll(title, infoCard, marksTitle, tableCard);
        return root;
    }

    public static class SummaryRow {
        private final String subject;
        private final int total;
        private final String grade;

        public SummaryRow(String subject, int total, String grade) {
            this.subject = subject;
            this.total = total;
            this.grade = grade;
        }

        public String getSubject() { return subject; }
        public int getTotal() { return total; }
        public String getGrade() { return grade; }
    }
}
