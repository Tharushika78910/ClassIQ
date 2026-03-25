package Frontend.teacher;

import Frontend.Session;
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

import java.util.Locale;
import java.util.ResourceBundle;

public class TeacherMyGradesPage {

    private final Runnable onBack;
    private final Runnable onLogout;
    private final String backgroundResourcePath;
    private final Locale currentLocale;
    private final ResourceBundle bundle;

    public TeacherMyGradesPage() {
        this(null, null, null,
                Session.getCurrentLocale() != null ? Session.getCurrentLocale() : new Locale("en", "US"));
    }

    public TeacherMyGradesPage(Locale locale) {
        this(null, null, null, locale);
    }

    public TeacherMyGradesPage(Runnable onBack, Runnable onLogout, String backgroundResourcePath, Locale locale) {
        this.onBack = onBack;
        this.onLogout = onLogout;
        this.backgroundResourcePath = backgroundResourcePath;
        this.currentLocale = (locale == null) ? new Locale("en", "US") : locale;
        this.bundle = ResourceBundle.getBundle("messages", this.currentLocale);
    }

    public Node getView() {

        boolean rtl = isRTL(currentLocale);

        VBox content = new VBox(18);
        content.setPadding(new Insets(22));
        content.setAlignment(Pos.TOP_LEFT);

        Label title = new Label(getText("teacher.mygrades.title", "Students Grades"));
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label criteriaTitle = new Label(getText("teacher.mygrades.criteria.title", "Grading Criteria"));
        criteriaTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        TextFlow criteriaText = new TextFlow();
        criteriaText.setLineSpacing(5);
        criteriaText.setMaxWidth(720);

        Text t1 = normal(getText("teacher.mygrades.criteria.part1", "The "));
        Text t2 = bold(getText("teacher.mygrades.criteria.totalmark.bold", "Total mark"));
        Text t3 = normal(getText("teacher.mygrades.criteria.part2", " for this course is "));
        Text t4 = bold(getText("teacher.mygrades.criteria.100marks.bold", "100 marks"));
        Text t5 = normal(getText("teacher.mygrades.criteria.part3", " and it is calculated based on three assessment components.\n\n"));

        Text t6 = bold(getText("teacher.mygrades.criteria.assignments.bold", "Assignments"));
        Text t7 = normal(getText("teacher.mygrades.criteria.part4", " contribute "));
        Text t8 = bold(getText("teacher.mygrades.criteria.20marks.bold", "20 marks"));
        Text t9 = normal(getText("teacher.mygrades.criteria.part5", " and are used to evaluate continuous learning and understanding of the subject. The "));

        Text t10 = bold(getText("teacher.mygrades.criteria.project.bold", "project"));
        Text t11 = normal(getText("teacher.mygrades.criteria.part6", " carries "));
        Text t12 = bold(getText("teacher.mygrades.criteria.30marks.bold", "30 marks"));
        Text t13 = normal(getText("teacher.mygrades.criteria.part7", " and assesses practical implementation skills and problem-solving ability. The "));

        Text t14 = bold(getText("teacher.mygrades.criteria.finalexam.bold", "final examination"));
        Text t15 = normal(getText("teacher.mygrades.criteria.part8", " contributes "));
        Text t16 = bold(getText("teacher.mygrades.criteria.50marks.bold", "50 marks"));
        Text t17 = normal(getText("teacher.mygrades.criteria.part9", " and measures the overall knowledge gained throughout the course.\n\n"));

        Text t18 = normal(getText("teacher.mygrades.criteria.part10", "The final result is calculated by combining the marks obtained from all three components."));

        criteriaText.getChildren().addAll(
                t1, t2, t3, t4, t5,
                t6, t7, t8, t9,
                t10, t11, t12, t13,
                t14, t15, t16, t17,
                t18
        );

        Label gradeScaleTitle = new Label(getText("teacher.mygrades.scale.title", "Grade Scale"));
        gradeScaleTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        GridPane gradeTable = new GridPane();
        gradeTable.setVgap(10);
        gradeTable.setHgap(60);
        gradeTable.setPadding(new Insets(8, 0, 0, 0));

        Label r1a = new Label("0 - 34");
        Label r1b = new Label("F");
        Label r2a = new Label("35 - 54");
        Label r2b = new Label("S");
        Label r3a = new Label("55 - 64");
        Label r3b = new Label("C");
        Label r4a = new Label("65 - 74");
        Label r4b = new Label("B");
        Label r5a = new Label("75 - 100");
        Label r5b = new Label("A");

        String labelStyle = "-fx-text-fill: black; -fx-font-size: 15px;";
        r1a.setStyle(labelStyle); r1b.setStyle(labelStyle);
        r2a.setStyle(labelStyle); r2b.setStyle(labelStyle);
        r3a.setStyle(labelStyle); r3b.setStyle(labelStyle);
        r4a.setStyle(labelStyle); r4b.setStyle(labelStyle);
        r5a.setStyle(labelStyle); r5b.setStyle(labelStyle);

        if (rtl) {
            gradeTable.add(r1b, 0, 0); gradeTable.add(r1a, 1, 0);
            gradeTable.add(r2b, 0, 1); gradeTable.add(r2a, 1, 1);
            gradeTable.add(r3b, 0, 2); gradeTable.add(r3a, 1, 2);
            gradeTable.add(r4b, 0, 3); gradeTable.add(r4a, 1, 3);
            gradeTable.add(r5b, 0, 4); gradeTable.add(r5a, 1, 4);
        } else {
            gradeTable.add(r1a, 0, 0); gradeTable.add(r1b, 1, 0);
            gradeTable.add(r2a, 0, 1); gradeTable.add(r2b, 1, 1);
            gradeTable.add(r3a, 0, 2); gradeTable.add(r3b, 1, 2);
            gradeTable.add(r4a, 0, 3); gradeTable.add(r4b, 1, 3);
            gradeTable.add(r5a, 0, 4); gradeTable.add(r5b, 1, 4);
        }

        content.getChildren().addAll(
                title,
                criteriaTitle,
                criteriaText,
                gradeScaleTitle,
                gradeTable
        );

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent;");
        scroll.setBackground(Background.EMPTY);

        StackPane card = new StackPane(scroll);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(6));
        card.setMaxWidth(860);
        card.setPrefWidth(860);
        card.setStyle(
                "-fx-background-color: rgba(255,255,255,0.95);" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-color: #5E9E74;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 18;"
        );

        StackPane centeredCard = new StackPane(card);
        centeredCard.setAlignment(Pos.TOP_CENTER);
        centeredCard.setPadding(new Insets(28, 28, 18, 28));

        BorderPane page = new BorderPane();
        page.setCenter(centeredCard);
        page.setStyle("-fx-background-color: transparent;");

        StackPane root = new StackPane(page);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: transparent;");

        return root;
    }

    private Text normal(String value) {
        Text text = new Text(value);
        text.setStyle("-fx-fill: black;");
        text.setFont(Font.font("System", 14));
        return text;
    }

    private Text bold(String value) {
        Text text = new Text(value);
        text.setStyle("-fx-fill: black;");
        text.setFont(Font.font("System", FontWeight.BOLD, 14));
        return text;
    }

    private String getText(String key, String fallback) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return fallback;
        }
    }

    private boolean isRTL(Locale locale) {
        return locale != null && "ar".equalsIgnoreCase(locale.getLanguage());
    }
}