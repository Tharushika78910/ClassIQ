package frontend.student;

import frontend.LoginPage;
import frontend.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudentMyGradesPage {

    private static final String SYSTEM_FONT = "System";

    private final StudentDashboard dashboard;
    private final ResourceBundle bundle;

    public StudentMyGradesPage(StudentDashboard dashboard) {
        this.dashboard = dashboard;
        this.bundle = ResourceBundle.getBundle("messages", Session.getCurrentLocale());
    }

    public StudentMyGradesPage() {
        this.dashboard = null;
        this.bundle = ResourceBundle.getBundle("messages", Session.getCurrentLocale());
    }

    public Node getView() {

        VBox content = new VBox(18);
        content.setPadding(new Insets(22));
        content.setAlignment(Pos.TOP_LEFT);

        Label title = new Label(bundle.getString("student.mygrades.title"));
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label criteriaTitle = new Label(bundle.getString("student.mygrades.criteria.title"));
        criteriaTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        TextFlow criteriaText = new TextFlow();
        criteriaText.setLineSpacing(5);
        criteriaText.setMaxWidth(720);

        String black = "-fx-fill: black;";

        Text t1 = new Text(bundle.getString("teacher.mygrades.criteria.part1")); t1.setStyle(black);
        Text t2 = new Text(bundle.getString("teacher.mygrades.criteria.totalmark.bold")); t2.setFont(Font.font(SYSTEM_FONT, FontWeight.BOLD, 14)); t2.setStyle(black);
        Text t3 = new Text(bundle.getString("teacher.mygrades.criteria.part2")); t3.setStyle(black);
        Text t4 = new Text(bundle.getString("teacher.mygrades.criteria.100marks.bold")); t4.setFont(Font.font(SYSTEM_FONT, FontWeight.BOLD, 14)); t4.setStyle(black);
        Text t5 = new Text(bundle.getString("teacher.mygrades.criteria.part3")); t5.setStyle(black);

        Text t7 = new Text(bundle.getString("teacher.mygrades.criteria.assignments.bold")); t7.setFont(Font.font(SYSTEM_FONT, FontWeight.BOLD, 14)); t7.setStyle(black);
        Text t8 = new Text(bundle.getString("teacher.mygrades.criteria.part4")); t8.setStyle(black);
        Text t9 = new Text(bundle.getString("teacher.mygrades.criteria.20marks.bold")); t9.setFont(Font.font(SYSTEM_FONT, FontWeight.BOLD, 14)); t9.setStyle(black);
        Text t10 = new Text(bundle.getString("teacher.mygrades.criteria.part5")); t10.setStyle(black);

        Text t11 = new Text(bundle.getString("teacher.mygrades.criteria.project.bold")); t11.setFont(Font.font(SYSTEM_FONT, FontWeight.BOLD, 14)); t11.setStyle(black);
        Text t12 = new Text(bundle.getString("teacher.mygrades.criteria.part6")); t12.setStyle(black);
        Text t13 = new Text(bundle.getString("teacher.mygrades.criteria.30marks.bold")); t13.setFont(Font.font(SYSTEM_FONT, FontWeight.BOLD, 14)); t13.setStyle(black);
        Text t14 = new Text(bundle.getString("teacher.mygrades.criteria.part7")); t14.setStyle(black);

        Text t15 = new Text(bundle.getString("teacher.mygrades.criteria.finalexam.bold")); t15.setFont(Font.font(SYSTEM_FONT, FontWeight.BOLD, 14)); t15.setStyle(black);
        Text t16 = new Text(bundle.getString("teacher.mygrades.criteria.part8")); t16.setStyle(black);
        Text t17 = new Text(bundle.getString("teacher.mygrades.criteria.50marks.bold")); t17.setFont(Font.font(SYSTEM_FONT, FontWeight.BOLD, 14)); t17.setStyle(black);
        Text t18 = new Text(bundle.getString("teacher.mygrades.criteria.part9")); t18.setStyle(black);

        Text t19 = new Text(bundle.getString("teacher.mygrades.criteria.part10"));
        t19.setStyle(black);

        criteriaText.getChildren().addAll(
                t1, t2, t3, t4, t5,
                t7, t8, t9, t10,
                t11, t12, t13, t14,
                t15, t16, t17, t18,
                t19
        );

        Label gradeScaleTitle = new Label(bundle.getString("student.mygrades.scale.title"));
        gradeScaleTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        GridPane gradeTable = new GridPane();
        gradeTable.setVgap(10);
        gradeTable.setHgap(60);
        gradeTable.setPadding(new Insets(8, 0, 0, 0));

        String labelBlack = "-fx-text-fill: black;";

        Label l1 = new Label(bundle.getString("student.mygrades.scale.range.f")); l1.setStyle(labelBlack);
        Label l2 = new Label(bundle.getString("student.mygrades.scale.grade.f")); l2.setStyle(labelBlack);
        Label l3 = new Label(bundle.getString("student.mygrades.scale.range.s")); l3.setStyle(labelBlack);
        Label l4 = new Label(bundle.getString("student.mygrades.scale.grade.s")); l4.setStyle(labelBlack);
        Label l5 = new Label(bundle.getString("student.mygrades.scale.range.c")); l5.setStyle(labelBlack);
        Label l6 = new Label(bundle.getString("student.mygrades.scale.grade.c")); l6.setStyle(labelBlack);
        Label l7 = new Label(bundle.getString("student.mygrades.scale.range.b")); l7.setStyle(labelBlack);
        Label l8 = new Label(bundle.getString("student.mygrades.scale.grade.b")); l8.setStyle(labelBlack);
        Label l9 = new Label(bundle.getString("student.mygrades.scale.range.a")); l9.setStyle(labelBlack);
        Label l10 = new Label(bundle.getString("student.mygrades.scale.grade.a")); l10.setStyle(labelBlack);

        gradeTable.add(l1, 0, 0); gradeTable.add(l2, 1, 0);
        gradeTable.add(l3, 0, 1); gradeTable.add(l4, 1, 1);
        gradeTable.add(l5, 0, 2); gradeTable.add(l6, 1, 2);
        gradeTable.add(l7, 0, 3); gradeTable.add(l8, 1, 3);
        gradeTable.add(l9, 0, 4); gradeTable.add(l10, 1, 4);

        content.getChildren().addAll(title, criteriaTitle, criteriaText, gradeScaleTitle, gradeTable);

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

        card.setStyle("""
            -fx-background-color: rgba(210, 230, 255, 0.85);
            -fx-background-radius: 18;
            -fx-border-color: rgba(80, 130, 200, 0.6);
            -fx-border-width: 2;
            -fx-border-radius: 18;
        """);

        StackPane centeredCard = new StackPane(card);
        centeredCard.setAlignment(Pos.TOP_CENTER);
        centeredCard.setPadding(new Insets(28, 28, 18, 28));

        BorderPane page = new BorderPane();
        page.setCenter(centeredCard);

        Button btnBack = dashboard.createStudentBackButton(dashboard::showHome);
        Button btnLogout = dashboard.createStudentLogoutButton(() -> {
            Locale savedLocale = Session.getCurrentLocale();
            Session.clear();
            Stage stage = (Stage) page.getScene().getWindow();
            stage.setScene(new LoginPage(stage, savedLocale).getScene());
        });

        AnchorPane bottomBar = new AnchorPane();
        bottomBar.setPadding(new Insets(15));

        AnchorPane.setLeftAnchor(btnBack, 20.0);
        AnchorPane.setBottomAnchor(btnBack, 10.0);

        AnchorPane.setRightAnchor(btnLogout, 20.0);
        AnchorPane.setBottomAnchor(btnLogout, 10.0);

        bottomBar.getChildren().addAll(btnBack, btnLogout);
        page.setBottom(bottomBar);

        StackPane root = new StackPane(page);
        root.setPadding(new Insets(10));
        root.getStyleClass().add("page-bg");

        root.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/student-dashboard.css")).toExternalForm()
        );

        return root;
    }
}