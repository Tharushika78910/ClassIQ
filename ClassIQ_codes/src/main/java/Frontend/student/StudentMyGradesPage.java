package Frontend.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class StudentMyGradesPage {

    public Node getView() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_LEFT);

        // ===== Title =====
        Label title = new Label("Grades");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // ===== Grading Criteria Title =====
        Label criteriaTitle = new Label("Grading Criteria");
        criteriaTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ===== Paragraph with Bold Words =====
        TextFlow criteriaText = new TextFlow();
        criteriaText.setLineSpacing(5);
        criteriaText.setMaxWidth(750);

        // First paragraph
        Text t1 = new Text("The ");
        Text t2 = new Text("Total mark ");
        t2.setFont(Font.font("System", FontWeight.BOLD, 14));
        Text t3 = new Text("for this course is ");
        Text t4 = new Text("100 marks ");
        t4.setFont(Font.font("System", FontWeight.BOLD, 14));
        Text t5 = new Text("and it is calculated based on three assessment components.\n\n");

        // Second paragraph
        Text t7 = new Text("Assignments ");
        t7.setFont(Font.font("System", FontWeight.BOLD, 14));
        Text t8 = new Text("contribute ");
        Text t9 = new Text("20 marks ");
        t9.setFont(Font.font("System", FontWeight.BOLD, 14));
        Text t10 = new Text("and are used to evaluate continuous learning and understanding of the subject. The ");
        Text t11 = new Text("project ");
        t11.setFont(Font.font("System", FontWeight.BOLD, 14));
        Text t12 = new Text("carries ");
        Text t13 = new Text("30 marks ");
        t13.setFont(Font.font("System", FontWeight.BOLD, 14));
        Text t14 = new Text("and assesses practical implementation skills and problem-solving ability. The ");
        Text t15 = new Text("final examination ");
        t15.setFont(Font.font("System", FontWeight.BOLD, 14));
        Text t16 = new Text("contributes ");
        Text t17 = new Text("50 marks ");
        t17.setFont(Font.font("System", FontWeight.BOLD, 14));
        Text t18 = new Text("and measures the overall knowledge gained throughout the course.\n\n");

        // Third paragraph
        Text t19 = new Text("The final result is calculated by combining the marks obtained from all three components.");

        criteriaText.getChildren().addAll(
                t1, t2, t3, t4, t5,
                t7, t8, t9, t10,
                t11, t12, t13, t14, t15,
                t16, t17, t18,
                t19
        );

        // ===== Grade Scale Title =====
        Label gradeScaleTitle = new Label("Grade Scale");
        gradeScaleTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ===== Grade Table =====
        GridPane gradeTable = new GridPane();
        gradeTable.setVgap(10);
        gradeTable.setHgap(60);
        gradeTable.setPadding(new Insets(10, 0, 0, 0));

        // Adding grades properly
        gradeTable.add(new Label("0 - 34"), 0, 0);
        gradeTable.add(new Label("F"), 1, 0);

        gradeTable.add(new Label("35 - 54"), 0, 1);
        gradeTable.add(new Label("S"), 1, 1);

        gradeTable.add(new Label("55 - 64"), 0, 2);
        gradeTable.add(new Label("C"), 1, 2);

        gradeTable.add(new Label("65 - 74"), 0, 3);
        gradeTable.add(new Label("B"), 1, 3);

        gradeTable.add(new Label("75 - 100"), 0, 4);
        gradeTable.add(new Label("A"), 1, 4);

        // Add all to VBox
        content.getChildren().addAll(title, criteriaTitle, criteriaText, gradeScaleTitle, gradeTable);

        // ===== Scroll support =====
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        return scrollPane;
    }
}
