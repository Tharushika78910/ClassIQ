package Frontend.teacher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class TeacherMarkSheetPage {

    public Parent getView() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("page-bg");

        Label title = new Label("Mark Sheet");
        title.getStyleClass().add("title-xl");

        Label subtitle = new Label("Class: 10A   |   Term: 1   |   Subject: Mathematics");
        subtitle.getStyleClass().add("subtitle");

        VBox card = new VBox(12);
        card.getStyleClass().add("card");

        TableView<MarkRow> table = new TableView<>();
        table.getStyleClass().add("app-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MarkRow, String> colId = new TableColumn<>("Student ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<MarkRow, String> colName = new TableColumn<>("Student Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        TableColumn<MarkRow, Integer> colAttendance = new TableColumn<>("Attendance");
        colAttendance.setCellValueFactory(new PropertyValueFactory<>("attendance"));

        TableColumn<MarkRow, Integer> colMid = new TableColumn<>("Mid");
        colMid.setCellValueFactory(new PropertyValueFactory<>("mid"));

        TableColumn<MarkRow, Integer> colFinal = new TableColumn<>("Final");
        colFinal.setCellValueFactory(new PropertyValueFactory<>("finalExam"));

        TableColumn<MarkRow, Integer> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<MarkRow, String> colGrade = new TableColumn<>("Grade");
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        table.getColumns().addAll(colId, colName, colAttendance, colMid, colFinal, colTotal, colGrade);
        table.setItems(sampleData());

        card.getChildren().add(table);
        root.getChildren().addAll(title, subtitle, card);
        return root;
    }

    private ObservableList<MarkRow> sampleData() {
        return FXCollections.observableArrayList(
                new MarkRow("S001", "Alex Johnson", 10, 25, 55),
                new MarkRow("S002", "Mia Tran", 9, 28, 60),
                new MarkRow("S003", "Noah Silva", 8, 20, 50),
                new MarkRow("S004", "Emma Lee", 10, 30, 65)
        );
    }

    public static class MarkRow {
        private final String studentId;
        private final String studentName;
        private final int attendance;
        private final int mid;
        private final int finalExam;

        public MarkRow(String studentId, String studentName, int attendance, int mid, int finalExam) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.attendance = attendance;
            this.mid = mid;
            this.finalExam = finalExam;
        }

        public String getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public int getAttendance() { return attendance; }
        public int getMid() { return mid; }
        public int getFinalExam() { return finalExam; }

        public int getTotal() { return attendance + mid + finalExam; }

        public String getGrade() {
            int t = getTotal();
            if (t >= 85) return "A";
            if (t >= 70) return "B";
            if (t >= 55) return "C";
            if (t >= 40) return "D";
            return "F";
        }
    }
}
