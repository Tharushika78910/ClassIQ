package Frontend.student;

import Frontend.LoginPage;
import Frontend.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class StudentDashboard extends BorderPane {

    private final StackPane contentArea = new StackPane();

    private final String studentName;
    private final String studentEmail;
    private final String studentProfileImagePath;

    // Images
    private static final String BG_IMAGE      = "/HomepageS.png";
    private static final String INFO_IMAGE    = "/images/StudentInfoS.png";
    private static final String GRADING_IMAGE = "/images/MarkSheetS.png";
    private static final String REPORT_IMAGE  = "/images/Report CardS.png";

    public StudentDashboard(String name, String email, String profileImagePath) {

        this.studentName = name;
        this.studentEmail = email;
        this.studentProfileImagePath = profileImagePath;

        getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/student-dashboard.css")).toExternalForm()
        );

        setCenter(contentArea);
        showHome();
    }

    public void showHome() {
        showPage(buildHomeView(studentName, studentEmail, studentProfileImagePath));
    }

    private Node buildHomeView(String name, String email, String profileImagePath) {

        // Background is now handled by CSS
        StackPane root = new StackPane();
        root.getStyleClass().add("figma-root");

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(30));

        // TOP RIGHT Student Info
        HBox studentBox = buildStudentInfo(name, email, profileImagePath);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(spacer, studentBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        layout.setTop(topBar);

        // CENTER: 3 circles
        VBox infoBlock = buildTopicBlock("My Info", INFO_IMAGE);
        VBox gradingBlock = buildTopicBlock("Grading Criteria", GRADING_IMAGE);
        VBox reportBlock = buildTopicBlock("Report Card", REPORT_IMAGE);

        Button infoBtn = (Button) infoBlock.getChildren().get(1);
        Button btnGradingCriteria = (Button) gradingBlock.getChildren().get(1);
        Button reportBtn = (Button) reportBlock.getChildren().get(1);

        infoBtn.setOnAction(e -> showPage(new StudentMyInfoPage(this).getView()));

        // after removed buttons from StudentMyGradesPage,
        // creating it using the no-args constructor
        btnGradingCriteria.setOnAction(e ->
                showPage(new StudentMyGradesPage().getView())
        );

        reportBtn.setOnAction(e -> showPage(new StudentReportCardPage(this).getView()));

        GridPane grid = new GridPane();
        grid.setHgap(120);
        grid.setVgap(80);
        grid.setAlignment(Pos.CENTER);

        grid.add(infoBlock, 0, 0);
        grid.add(gradingBlock, 1, 0);

        grid.add(reportBlock, 0, 1, 2, 1);
        GridPane.setHalignment(reportBlock, javafx.geometry.HPos.CENTER);

        StackPane centerWrap = new StackPane(grid);
        centerWrap.setAlignment(Pos.CENTER);
        layout.setCenter(centerWrap);

        // =========================
        // BOTTOM LEFT Back (ADDED)
        // =========================
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

        Button btnBack = new Button("← Back");
        btnBack.setStyle(pillNormal);
        btnBack.setOnMouseEntered(e -> btnBack.setStyle(pillHover));
        btnBack.setOnMouseExited(e -> btnBack.setStyle(pillNormal));

        // Same Back behavior as TeacherDashboard: back to Login
        btnBack.setOnAction(e -> {
            Session.clear();
            Stage stage = (Stage) root.getScene().getWindow();
            LoginPage loginPage = new LoginPage(stage);
            stage.getScene().setRoot(loginPage.getView());
        });

        // BOTTOM RIGHT Logout (KEEP EXACTLY AS YOU HAD)
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(pillNormal);
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(pillHover));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle(pillNormal));

        logoutBtn.setOnAction(e -> {
            Session.clear();
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new LoginPage(stage).getScene());
        });

        // Put Back (left) + Logout (right) without changing logout behavior
        AnchorPane bottomBar = new AnchorPane();
        bottomBar.setPadding(new Insets(15));

        AnchorPane.setLeftAnchor(btnBack, 20.0);
        AnchorPane.setBottomAnchor(btnBack, 10.0);

        AnchorPane.setRightAnchor(logoutBtn, 20.0);
        AnchorPane.setBottomAnchor(logoutBtn, 10.0);

        bottomBar.getChildren().addAll(btnBack, logoutBtn);
        layout.setBottom(bottomBar);

        root.getChildren().add(layout);
        return root;
    }

    private VBox buildTopicBlock(String title, String imagePath) {

        VBox box = new VBox(16);
        box.setAlignment(Pos.TOP_CENTER);

        ImageView iv = new ImageView();
        try {
            iv.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
        } catch (Exception ignored) {}

        iv.setFitWidth(140);
        iv.setFitHeight(140);
        iv.setPreserveRatio(false);

        Circle clip = new Circle(70, 70, 70);
        iv.setClip(clip);

        StackPane circleHolder = new StackPane(iv);
        circleHolder.setMinSize(140, 140);
        circleHolder.setMaxSize(140, 140);
        circleHolder.getStyleClass().add("circle-holder");

        Button btn = new Button(title);
        btn.getStyleClass().add("topic-btn");
        btn.setPrefSize(160, 32);

        box.getChildren().addAll(circleHolder, btn);
        return box;
    }

    private HBox buildStudentInfo(String name, String email, String profileImagePath) {

        HBox wrap = new HBox(12);
        wrap.setAlignment(Pos.CENTER_RIGHT);
        wrap.getStyleClass().add("teacher-info");

        ImageView avatar = new ImageView();
        try {
            avatar.setImage(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(profileImagePath))
            ));
        } catch (Exception ignored) {}

        avatar.setFitWidth(52);
        avatar.setFitHeight(52);
        avatar.setPreserveRatio(false);

        Circle clip = new Circle(26, 26, 26);
        avatar.setClip(clip);

        VBox info = new VBox(2);

        Label nameLbl = new Label(name);
        nameLbl.getStyleClass().add("teacher-name");

        Label emailLbl = new Label(email);
        emailLbl.getStyleClass().add("teacher-email");

        info.getChildren().addAll(nameLbl, emailLbl);
        wrap.getChildren().addAll(avatar, info);

        return wrap;
    }

    private Node simplePlaceholder(String text) {
        VBox box = new VBox(20);
        box.setPadding(new Insets(40));
        Label lbl = new Label(text);
        lbl.setFont(Font.font(18));
        box.getChildren().add(lbl);
        return box;
    }

    public void showPage(Node node) {
        contentArea.getChildren().setAll(node);
    }
}