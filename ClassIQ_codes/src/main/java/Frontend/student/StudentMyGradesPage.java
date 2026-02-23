package Frontend.student;

import Frontend.LoginPage;
import Frontend.Session;
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

import java.util.Objects;

public class StudentMyGradesPage {

    private final StudentDashboard dashboard;

    public StudentMyGradesPage(StudentDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public StudentMyGradesPage() {
        this.dashboard = null;
    }

    public Node getView() {

        // =========================
        // CONTENT
        // =========================
        VBox content = new VBox(18);
        content.setPadding(new Insets(22));
        content.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Grades");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label criteriaTitle = new Label("Grading Criteria");
        criteriaTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        TextFlow criteriaText = new TextFlow();
        criteriaText.setLineSpacing(5);
        criteriaText.setMaxWidth(720);

        // All text black
        String black = "-fx-fill: black;";

        Text t1 = new Text("The "); t1.setStyle(black);
        Text t2 = new Text("Total mark "); t2.setFont(Font.font("System", FontWeight.BOLD, 14)); t2.setStyle(black);
        Text t3 = new Text("for this course is "); t3.setStyle(black);
        Text t4 = new Text("100 marks "); t4.setFont(Font.font("System", FontWeight.BOLD, 14)); t4.setStyle(black);
        Text t5 = new Text("and it is calculated based on three assessment components.\n\n"); t5.setStyle(black);

        Text t7 = new Text("Assignments "); t7.setFont(Font.font("System", FontWeight.BOLD, 14)); t7.setStyle(black);
        Text t8 = new Text("contribute "); t8.setStyle(black);
        Text t9 = new Text("20 marks "); t9.setFont(Font.font("System", FontWeight.BOLD, 14)); t9.setStyle(black);
        Text t10 = new Text("and are used to evaluate continuous learning and understanding of the subject. The "); t10.setStyle(black);

        Text t11 = new Text("project "); t11.setFont(Font.font("System", FontWeight.BOLD, 14)); t11.setStyle(black);
        Text t12 = new Text("carries "); t12.setStyle(black);
        Text t13 = new Text("30 marks "); t13.setFont(Font.font("System", FontWeight.BOLD, 14)); t13.setStyle(black);
        Text t14 = new Text("and assesses practical implementation skills and problem-solving ability. The "); t14.setStyle(black);

        Text t15 = new Text("final examination "); t15.setFont(Font.font("System", FontWeight.BOLD, 14)); t15.setStyle(black);
        Text t16 = new Text("contributes "); t16.setStyle(black);
        Text t17 = new Text("50 marks "); t17.setFont(Font.font("System", FontWeight.BOLD, 14)); t17.setStyle(black);
        Text t18 = new Text("and measures the overall knowledge gained throughout the course.\n\n"); t18.setStyle(black);

        Text t19 = new Text("The final result is calculated by combining the marks obtained from all three components.");
        t19.setStyle(black);

        criteriaText.getChildren().addAll(
                t1, t2, t3, t4, t5,
                t7, t8, t9, t10,
                t11, t12, t13, t14, t15,
                t16, t17, t18,
                t19
        );

        Label gradeScaleTitle = new Label("Grade Scale");
        gradeScaleTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        GridPane gradeTable = new GridPane();
        gradeTable.setVgap(10);
        gradeTable.setHgap(60);
        gradeTable.setPadding(new Insets(8, 0, 0, 0));

        String labelBlack = "-fx-text-fill: black;";

        Label l1 = new Label("0 - 34"); l1.setStyle(labelBlack);
        Label l2 = new Label("F"); l2.setStyle(labelBlack);
        Label l3 = new Label("35 - 54"); l3.setStyle(labelBlack);
        Label l4 = new Label("S"); l4.setStyle(labelBlack);
        Label l5 = new Label("55 - 64"); l5.setStyle(labelBlack);
        Label l6 = new Label("C"); l6.setStyle(labelBlack);
        Label l7 = new Label("65 - 74"); l7.setStyle(labelBlack);
        Label l8 = new Label("B"); l8.setStyle(labelBlack);
        Label l9 = new Label("75 - 100"); l9.setStyle(labelBlack);
        Label l10 = new Label("A"); l10.setStyle(labelBlack);

        gradeTable.add(l1, 0, 0); gradeTable.add(l2, 1, 0);
        gradeTable.add(l3, 0, 1); gradeTable.add(l4, 1, 1);
        gradeTable.add(l5, 0, 2); gradeTable.add(l6, 1, 2);
        gradeTable.add(l7, 0, 3); gradeTable.add(l8, 1, 3);
        gradeTable.add(l9, 0, 4); gradeTable.add(l10, 1, 4);

        content.getChildren().addAll(title, criteriaTitle, criteriaText, gradeScaleTitle, gradeTable);

        // =========================
        // SCROLL
        // =========================
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent;");
        scroll.setBackground(Background.EMPTY);

        // =========================
        // LIGHT BLUE CARD
        // =========================
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


        String pillNormal =
                "-fx-background-color: rgba(255,255,255,0.92);" +
                        "-fx-text-fill: #2E6F62;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10,0,0,2);";

        String pillHover =
                "-fx-background-color: #9AC4B7;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22 8 22;";

        Button btnBack = dashboard.createStudentBackButton(() -> dashboard.showHome());

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle(pillNormal);
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle(pillHover));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle(pillNormal));
        btnLogout.setOnAction(e -> {
            Session.clear();
            Stage stage = (Stage) page.getScene().getWindow();
            stage.setScene(new LoginPage(stage).getScene());
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