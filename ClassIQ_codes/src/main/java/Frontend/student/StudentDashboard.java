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

    // ✅ Images (CHANGE PATHS if yours are different)
    private static final String BG_IMAGE      = "/Homepage.png";
    private static final String INFO_IMAGE    = "/images/studentInfo.png";
    private static final String GRADING_IMAGE = "/images/GradingCriteria.png";
    private static final String REPORT_IMAGE  = "/images/Report Card.png"; // change to your real file

    public StudentDashboard(String name, String email, String profileImagePath) {

        this.studentName = name;
        this.studentEmail = email;
        this.studentProfileImagePath = profileImagePath;

        // ✅ Use same CSS as teacher (so circle-holder, topic-btn, logout-btn, teacher-info styles work)
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

        StackPane root = new StackPane();
        root.getStyleClass().add("figma-root");


        try {
            BackgroundImage bg = new BackgroundImage(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream(BG_IMAGE))),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(
                            BackgroundSize.AUTO, BackgroundSize.AUTO,
                            false, false,
                            true,  // contain
                            true   // cover  ✅ important
                    )
            );
            root.setBackground(new Background(bg));
        } catch (Exception ignored) {}

        AnchorPane layer = new AnchorPane();
        layer.setPadding(new Insets(30));

        //  Top-right student info
        HBox studentBox = buildStudentInfo(name, email, profileImagePath);
        AnchorPane.setTopAnchor(studentBox, 20.0);
        AnchorPane.setRightAnchor(studentBox, 20.0);

        //  My Info block
        VBox infoBlock = buildTopicBlock("My Info", INFO_IMAGE);
        AnchorPane.setTopAnchor(infoBlock, 120.0);
        AnchorPane.setLeftAnchor(infoBlock, 280.0);

        //  Grading Criteria block (opens StudentMyGradesPage)
        VBox gradingBlock = buildTopicBlock("Grading Criteria", GRADING_IMAGE);
        AnchorPane.setTopAnchor(gradingBlock, 120.0);
        AnchorPane.setLeftAnchor(gradingBlock, 700.0);

        // Report Card block
        VBox reportBlock = buildTopicBlock("Report Card", REPORT_IMAGE);
        AnchorPane.setTopAnchor(reportBlock, 380.0);
        AnchorPane.setLeftAnchor(reportBlock, 480.0);

        // Bottom-right logout
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("logout-btn");
        AnchorPane.setRightAnchor(logoutBtn, 20.0);
        AnchorPane.setBottomAnchor(logoutBtn, 20.0);

        // ===== Actions =====
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

        logoutBtn.setOnAction(e -> showPage(simplePlaceholder("Logged out (placeholder)")));

        layer.getChildren().addAll(
                studentBox,
                infoBlock,
                gradingBlock,
                reportBlock,
                logoutBtn
        );

        root.getChildren().add(layer);
        return root;
    }

    //  same circle + button as teacher
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

    //  top-right info box (reuse teacher-info css)
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
