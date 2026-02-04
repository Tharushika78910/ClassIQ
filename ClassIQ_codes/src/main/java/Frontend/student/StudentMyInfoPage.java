package Frontend.student;

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

        VBox infoCard = new VBox(10);
        infoCard.getStyleClass().add("card");

        HBox infoRow = new HBox(30);

        VBox left = new VBox(6);
        left.getChildren().addAll(
                new Label("Name: Bao Tran"),
                new Label("Class: 10A"),
                new Label("Term: 1"),
                new Label("Roll No: 12")
        );

        VBox right = new VBox(6);
        right.getChildren().addAll(
                new Label("Email: bao@student.com"),
                new Label("Phone: +358 00 000 000"),
                new Label("Address: Helsinki")
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

        TableColumn<SummaryRow, String> colGrade = new TableColumn<>("Grade");
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        table.getColumns().addAll(colSub, colTotal, colGrade);

        table.setItems(FXCollections.observableArrayList(
                new SummaryRow("Mathematics", 90, "A"),
                new SummaryRow("Science", 78, "B"),
                new SummaryRow("English", 70, "B"),
                new SummaryRow("History", 62, "C"),
                new SummaryRow("Geography",84,"A")
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
