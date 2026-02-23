package Frontend.teacher;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TeacherMyGradesPage {

    // Keeping these fields (not used right now, but no harm)
    private final Runnable onBack;
    private final Runnable onLogout;
    private final String backgroundResourcePath;

    public TeacherMyGradesPage() {
        this(null, null, null);
    }

    public TeacherMyGradesPage(Runnable onBack, Runnable onLogout, String backgroundResourcePath) {
        this.onBack = onBack;
        this.onLogout = onLogout;
        this.backgroundResourcePath = backgroundResourcePath;
    }

    public Node getView() {

        // =========================
        // CONTENT
        // =========================
        VBox content = new VBox(18);
        content.setPadding(new Insets(22));
        content.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Students Grades");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label criteriaTitle = new Label("Grading Criteria");
        criteriaTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        TextFlow criteriaText = new TextFlow();
        criteriaText.setLineSpacing(5);
        criteriaText.setMaxWidth(720);

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

        // Make all text black
        String black = "-fx-fill: black;";
        t1.setStyle(black);
        t2.setStyle(black);
        t3.setStyle(black);
        t4.setStyle(black);
        t5.setStyle(black);
        t7.setStyle(black);
        t8.setStyle(black);
        t9.setStyle(black);
        t10.setStyle(black);
        t11.setStyle(black);
        t12.setStyle(black);
        t13.setStyle(black);
        t14.setStyle(black);
        t15.setStyle(black);
        t16.setStyle(black);
        t17.setStyle(black);
        t18.setStyle(black);
        t19.setStyle(black);

        criteriaText.getChildren().addAll(
                t1, t2, t3, t4, t5,
                t7, t8, t9, t10,
                t11, t12, t13, t14,
                t15, t16, t17, t18,
                t19
        );

        Label gradeScaleTitle = new Label("Grade Scale");
        gradeScaleTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        GridPane gradeTable = new GridPane();
        gradeTable.setVgap(10);
        gradeTable.setHgap(60);
        gradeTable.setPadding(new Insets(8, 0, 0, 0));

        Label r1a = new Label("0 - 34");   r1a.setStyle("-fx-text-fill: black;");
        Label r1b = new Label("F");        r1b.setStyle("-fx-text-fill: black;");
        Label r2a = new Label("35 - 54");  r2a.setStyle("-fx-text-fill: black;");
        Label r2b = new Label("S");        r2b.setStyle("-fx-text-fill: black;");
        Label r3a = new Label("55 - 64");  r3a.setStyle("-fx-text-fill: black;");
        Label r3b = new Label("C");        r3b.setStyle("-fx-text-fill: black;");
        Label r4a = new Label("65 - 74");  r4a.setStyle("-fx-text-fill: black;");
        Label r4b = new Label("B");        r4b.setStyle("-fx-text-fill: black;");
        Label r5a = new Label("75 - 100"); r5a.setStyle("-fx-text-fill: black;");
        Label r5b = new Label("A");        r5b.setStyle("-fx-text-fill: black;");

        gradeTable.add(r1a, 0, 0); gradeTable.add(r1b, 1, 0);
        gradeTable.add(r2a, 0, 1); gradeTable.add(r2b, 1, 1);
        gradeTable.add(r3a, 0, 2); gradeTable.add(r3b, 1, 2);
        gradeTable.add(r4a, 0, 3); gradeTable.add(r4b, 1, 3);
        gradeTable.add(r5a, 0, 4); gradeTable.add(r5b, 1, 4);

        content.getChildren().addAll(title, criteriaTitle, criteriaText, gradeScaleTitle, gradeTable);

        // =========================
        // SCROLL (transparent)
        // =========================
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setBackground(Background.EMPTY);

        // =========================
        // CARD (WHITE INSIDE + GREEN FRAME)
        // =========================
        StackPane card = new StackPane(scroll);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(6));
        card.setMaxWidth(860);
        card.setPrefWidth(860);

        // ✅ frame only (no full green fill)
        card.setStyle("""
            -fx-background-color: rgba(255,255,255,0.95);
            -fx-background-radius: 18;
            -fx-border-color: #5E9E74;
            -fx-border-width: 3;
            -fx-border-radius: 18;
        """);

        StackPane centeredCard = new StackPane(card);
        centeredCard.setAlignment(Pos.TOP_CENTER);
        centeredCard.setPadding(new Insets(28, 28, 18, 28));

        // TeacherDashboard background should show
        BorderPane page = new BorderPane();
        page.setCenter(centeredCard);
        page.setStyle("-fx-background-color: transparent;");

        StackPane root = new StackPane(page);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: transparent;");

        return root;
    }
}