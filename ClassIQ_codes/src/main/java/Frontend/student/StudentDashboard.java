package Frontend.student;

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

import java.util.Objects;

public class StudentDashboard extends BorderPane {

    private final StackPane contentArea = new StackPane();

    private final String studentName;
    private final String studentEmail;
    private final String studentProfileImagePath;

    // ✅ Images
    private static final String BG_IMAGE      = "/Homepage.png";
    private static final String INFO_IMAGE    = "/images/studentInfo.png";
    private static final String GRADING_IMAGE = "/images/GradingCriteria.png";
    private static final String REPORT_IMAGE  = "/images/Report Card.png"; // keep your real file name

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

    // ✅ UPDATED: Responsive layout (center stays centered when maximize)
    private Node buildHomeView(String name, String email, String profileImagePath) {

        StackPane root = new StackPane();
        root.getStyleClass().add("figma-root");

        // Background
        try {
            BackgroundImage bg = new BackgroundImage(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream(BG_IMAGE))),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(
                            BackgroundSize.AUTO, BackgroundSize.AUTO,
                            false, false,
                            true,
                            true
                    )
            );
            root.setBackground(new Background(bg));
        } catch (Exception ignored) {}

        // Use BorderPane for responsive zones
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(30));

        // ===== TOP RIGHT Student Info =====
        HBox studentBox = buildStudentInfo(name, email, profileImagePath);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(spacer, studentBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        layout.setTop(topBar);

        // ===== CENTER: 3 circles always centered =====
        VBox infoBlock = buildTopicBlock("My Info", INFO_IMAGE);
        VBox gradingBlock = buildTopicBlock("Grading Criteria", GRADING_IMAGE);
        VBox reportBlock = buildTopicBlock("Report Card", REPORT_IMAGE);

        // Actions (same as before)
        Button infoBtn = (Button) infoBlock.getChildren().get(1);
        Button gradingBtn = (Button) gradingBlock.getChildren().get(1);
        Button reportBtn = (Button) reportBlock.getChildren().get(1);

        infoBtn.setOnAction(e -> showPage(new StudentMyInfoPage().getView()));

        gradingBtn.setOnAction(e -> {
            StudentMyGradesPage page = new StudentMyGradesPage(
                    this::showHome, // Back goes to Home
                    () -> showPage(simplePlaceholder("Logged out (placeholder)")),
                    null
            );
            showPage(page.getView());
        });

        reportBtn.setOnAction(e -> showPage(new StudentReportCardPage().getView()));

        // Grid: 2 top, 1 bottom centered
        GridPane grid = new GridPane();
        grid.setHgap(120);
        grid.setVgap(80);
        grid.setAlignment(Pos.CENTER);

        grid.add(infoBlock, 0, 0);
        grid.add(gradingBlock, 1, 0);

        grid.add(reportBlock, 0, 1, 2, 1); // span 2 columns
        GridPane.setHalignment(reportBlock, javafx.geometry.HPos.CENTER);

        StackPane centerWrap = new StackPane(grid);
        centerWrap.setAlignment(Pos.CENTER);
        layout.setCenter(centerWrap);

        // ===== BOTTOM RIGHT Logout =====
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("logout-btn");
        logoutBtn.setOnAction(e -> showPage(simplePlaceholder("Logged out (placeholder)")));

        HBox bottom = new HBox(logoutBtn);
        bottom.setAlignment(Pos.BOTTOM_RIGHT);
        bottom.setPadding(new Insets(10, 0, 0, 0));
        layout.setBottom(bottom);

        root.getChildren().add(layout);
        return root;
    }

    // Same circle + button style
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

    // Top-right info box
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
