// ===============================
// StudentMyGradesPage.java
// ===============================
package Frontend.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.InputStream;

public class StudentMyGradesPage {

    private final Runnable onBack;
    private final Runnable onLogout;
    private final String backgroundResourcePath;

    // keeps old calls working: new StudentMyGradesPage().getView()
    public StudentMyGradesPage() {
        this(null, null, null);
    }

    // use this to show Back + Logout + background
    public StudentMyGradesPage(Runnable onBack, Runnable onLogout, String backgroundResourcePath) {
        this.onBack = onBack;
        this.onLogout = onLogout;
        this.backgroundResourcePath = backgroundResourcePath;
    }

    public Node getView() {

        VBox content = new VBox(18);
        content.setPadding(new Insets(18));
        content.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Grades");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label criteriaTitle = new Label("Grading Criteria");
        criteriaTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextFlow criteriaText = new TextFlow();
        criteriaText.setLineSpacing(5);
        criteriaText.setMaxWidth(620);

        Text t1 = new Text("The ");
        Text t2 = new Text("Total mark ");
        t2.setFont(Font.font("System", FontWeight.BOLD, 14));
        Text t3 = new Text("for this course is ");
        Text t4 = new Text("100 marks ");
        t4.setFont(Font.font("System", FontWeight.BOLD, 14));
        Text t5 = new Text("and it is calculated based on three assessment components.\n\n");

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

        Text t19 = new Text("The final result is calculated by combining the marks obtained from all three components.");

        criteriaText.getChildren().addAll(
                t1, t2, t3, t4, t5,
                t7, t8, t9, t10,
                t11, t12, t13, t14, t15,
                t16, t17, t18,
                t19
        );

        Label gradeScaleTitle = new Label("Grade Scale");
        gradeScaleTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane gradeTable = new GridPane();
        gradeTable.setVgap(10);
        gradeTable.setHgap(60);
        gradeTable.setPadding(new Insets(10, 0, 0, 0));

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

        content.getChildren().addAll(title, criteriaTitle, criteriaText, gradeScaleTitle, gradeTable);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        // old usage: no buttons
        if (onBack == null && onLogout == null && backgroundResourcePath == null) {
            return scrollPane;
        }

        BorderPane page = new BorderPane();
        page.setCenter(scrollPane);

        BorderPane bottomBar = new BorderPane();
        bottomBar.setPadding(new Insets(15, 20, 15, 20));

        Button backBtn = new Button("Back");
        backBtn.setStyle("""
            -fx-background-color: #e6e6e6;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-padding: 8 18;
        """);
        backBtn.setOnAction(e -> { if (onBack != null) onBack.run(); });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("""
            -fx-background-color: #b7f7b7;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-padding: 8 18;
        """);
        logoutBtn.setOnAction(e -> { if (onLogout != null) onLogout.run(); });

        bottomBar.setLeft(backBtn);
        bottomBar.setRight(logoutBtn);
        page.setBottom(bottomBar);

        if (backgroundResourcePath != null) {
            InputStream is = getClass().getResourceAsStream(backgroundResourcePath);
            if (is != null) {
                ImageView bg = new ImageView(new Image(is));
                bg.setPreserveRatio(false);
                bg.fitWidthProperty().bind(page.widthProperty());
                bg.fitHeightProperty().bind(page.heightProperty());
                return new StackPane(bg, page);
            }
        }

        return page;
    }
}
