package Frontend.teacher;

import Frontend.LoginPage;
import Frontend.Session;
import Frontend.teacher.TeacherMyGradesPage;
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

public class TeacherDashboard extends BorderPane {

    private final StackPane contentArea = new StackPane();

    private final String teacherName;
    private final String teacherEmail;
    private final String teacherProfileImagePath;

    // Images
    private static final String BG_IMAGE = "/Homepage.png";
    private static final String MARK_IMAGE = "/images/MarkSheet.png";
    private static final String GRADING_IMAGE = "/images/GradingCriteria.png";
    private static final String STUDENT_IMAGE = "/images/StudentInfo.png";

    public TeacherDashboard(String name, String email, String profileImagePath) {

        this.teacherName = name;
        this.teacherEmail = email;
        this.teacherProfileImagePath = profileImagePath;

        // general + teacher styles
        getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/css/app.css")
        ).toExternalForm());

        getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/css/teacher.css")
        ).toExternalForm());

        getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/css/teacher-dashboard.css")
        ).toExternalForm());

        getStyleClass().add("teacher-root");

        setCenter(contentArea);
        showHome();
    }

    public void showHome() {
        showPage(buildHomeView(teacherName, teacherEmail, teacherProfileImagePath));
    }

    private Node buildHomeView(String name, String email, String profileImagePath) {

        // Root with background
        StackPane root = new StackPane();
        root.getStyleClass().add("figma-root");

        // Background image
        try {
            BackgroundImage bg = new BackgroundImage(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream(BG_IMAGE))),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(bg));
        } catch (Exception ignored) {}

        // Main layout overlay
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // TOP BAR (Teacher info )
        HBox teacherBox = buildTeacherInfo(name, email, profileImagePath);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(12, spacer, teacherBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 10, 10, 10));

        layout.setTop(topBar);

        // CENTER (3 blocks always centered)
        VBox markBlock = buildTopicBlock("Mark sheet", MARK_IMAGE);
        VBox gradingBlock = buildTopicBlock("Grading Criteria", GRADING_IMAGE);
        VBox studentBlock = buildTopicBlock("Student Feedback", STUDENT_IMAGE);

        // Button Actions
        Button markBtn = (Button) markBlock.getChildren().get(1);
        Button gradingBtn = (Button) gradingBlock.getChildren().get(1);
        Button studentBtn = (Button) studentBlock.getChildren().get(1);

        // MarkSheet page
        markBtn.setOnAction(e ->
                showPage(new TeacherMarkSheetPage(this).getView())
        );

        // Wrap grading criteria page with TeacherDashboard back pill
        gradingBtn.setOnAction(e ->
                showPage(wrapWithBackPill(new TeacherMyGradesPage().getView()))
        );

        // Wrap student feedback page with TeacherDashboard back pill
        studentBtn.setOnAction(e ->
                showPage(wrapWithBackPill(new TeacherStudentsInfoPage(this).getView()))
        );

        // Grid: 2 blocks top row, student block centered below spanning 2 columns
        GridPane grid = new GridPane();
        grid.setHgap(120);
        grid.setVgap(80);
        grid.setAlignment(Pos.CENTER);

        grid.add(markBlock, 0, 0);
        grid.add(gradingBlock, 1, 0);

        grid.add(studentBlock, 0, 1, 2, 1); // span 2 columns
        GridPane.setHalignment(studentBlock, javafx.geometry.HPos.CENTER);

        // Wrap center grid
        StackPane centerWrap = new StackPane(grid);
        centerWrap.setAlignment(Pos.CENTER);

        layout.setCenter(centerWrap);

        // BOTTOM BAR
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
        // Back

        btnBack.setOnAction(e -> {
            Session.clear();
            Stage stage = (Stage) root.getScene().getWindow();
            LoginPage loginPage = new LoginPage(stage);
            stage.getScene().setRoot(loginPage.getView());
        });

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle(pillNormal);
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle(pillHover));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle(pillNormal));
        // placeholder

        btnLogout.setOnAction(e ->
                showPage(simplePlaceholder("Logged out (placeholder)"))
        );

        AnchorPane bottomBar = new AnchorPane();
        bottomBar.setPadding(new Insets(15));

        AnchorPane.setLeftAnchor(btnBack, 20.0);
        AnchorPane.setBottomAnchor(btnBack, 10.0);

        AnchorPane.setRightAnchor(btnLogout, 20.0);
        AnchorPane.setBottomAnchor(btnLogout, 10.0);

        bottomBar.getChildren().addAll(btnBack, btnLogout);
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

    private HBox buildTeacherInfo(String name, String email, String profileImagePath) {

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

    // Back -> showHome for inside pages
    private Node wrapWithBackPill(Node pageContent) {

        BorderPane wrapper = new BorderPane();
        wrapper.setCenter(pageContent);

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
        btnBack.setOnAction(e -> showHome());

        AnchorPane bottomBar = new AnchorPane();
        bottomBar.setPadding(new Insets(15));
        AnchorPane.setLeftAnchor(btnBack, 20.0);
        AnchorPane.setBottomAnchor(btnBack, 10.0);
        bottomBar.getChildren().add(btnBack);

        wrapper.setBottom(bottomBar);
        return wrapper;
    }

    public void showPage(Node node) {
        contentArea.getChildren().setAll(node);
    }
}