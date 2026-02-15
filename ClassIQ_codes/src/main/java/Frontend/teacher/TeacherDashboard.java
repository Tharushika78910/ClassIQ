package Frontend.teacher;

import Frontend.student.StudentMyGradesPage;
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

public class TeacherDashboard extends BorderPane {

    private final StackPane contentArea = new StackPane();

    private final String teacherName;
    private final String teacherEmail;
    private final String teacherProfileImagePath;

    // Images
    private static final String BG_IMAGE = "/Homepage.png";
    private static final String MARK_IMAGE = "/images/MarkSheet.png";
    private static final String GRADING_IMAGE = "/images/GradingCriteria.png";
    private static final String STUDENT_IMAGE = "/images/studentInfo.png";

    public TeacherDashboard(String name, String email, String profileImagePath) {

        this.teacherName = name;
        this.teacherEmail = email;
        this.teacherProfileImagePath = profileImagePath;

        getStyleClass().add("teacher-root");

        getStylesheets().add(
                getClass().getResource("/css/teacher-dashboard.css").toExternalForm()
        );

        setCenter(contentArea);

        showHome();
    }

    public void showHome() {
        showPage(buildHomeView(teacherName, teacherEmail, teacherProfileImagePath));
    }

    private Node buildHomeView(String name, String email, String profileImagePath) {

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

        AnchorPane layer = new AnchorPane();
        layer.setPadding(new Insets(30));

        // Teacher info (Top Right)
        HBox teacherBox = buildTeacherInfo(name, email, profileImagePath);
        AnchorPane.setTopAnchor(teacherBox, 20.0);
        AnchorPane.setRightAnchor(teacherBox, 20.0);

        // Mark Sheet
        VBox markBlock = buildTopicBlock("Mark sheet", MARK_IMAGE);
        AnchorPane.setTopAnchor(markBlock, 120.0);
        AnchorPane.setLeftAnchor(markBlock, 150.0);

        // Grading Criteria
        VBox gradingBlock = buildTopicBlock("Grading Criteria", GRADING_IMAGE);
        AnchorPane.setTopAnchor(gradingBlock, 120.0);
        AnchorPane.setLeftAnchor(gradingBlock, 600.0);

        // Student Info
        VBox studentBlock = buildTopicBlock("Student Info", STUDENT_IMAGE);
        AnchorPane.setTopAnchor(studentBlock, 380.0);
        AnchorPane.setLeftAnchor(studentBlock, 380.0);

        // Logout (Bottom Right)
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("logout-btn");
        AnchorPane.setRightAnchor(logoutBtn, 20.0);
        AnchorPane.setBottomAnchor(logoutBtn, 20.0);

        // ===== Button Actions =====
        // (IMPORTANT: button is index 1 because image is index 0)
        Button markBtn = (Button) markBlock.getChildren().get(1);
        Button gradingBtn = (Button) gradingBlock.getChildren().get(1);
        Button studentBtn = (Button) studentBlock.getChildren().get(1);

        markBtn.setOnAction(e ->
                showPage(new TeacherMarkSheetPage(this).getView())
        );

        gradingBtn.setOnAction(e -> {
            StudentMyGradesPage page = new StudentMyGradesPage(
                    this::showHome,
                    () -> showPage(simplePlaceholder("Logged out (placeholder)")),
                    "/Frontend/images/Login.png"
            );
            showPage(page.getView());
        });

        studentBtn.setOnAction(e ->
                showPage(new TeacherStudentsInfoPage(this).getView())
        );

        logoutBtn.setOnAction(e ->
                showPage(simplePlaceholder("Logged out (placeholder)"))
        );

        layer.getChildren().addAll(
                teacherBox,
                markBlock,
                gradingBlock,
                studentBlock,
                logoutBtn
        );

        root.getChildren().add(layer);
        return root;
    }

    // ✅ SMALLER CIRCLES + SPACE SO THEY DON'T TOUCH BUTTONS
    private VBox buildTopicBlock(String title, String imagePath) {

        VBox box = new VBox(16);          // ✅ more spacing between circle and button
        box.setAlignment(Pos.TOP_CENTER);

        // ----- Image -----
        ImageView iv = new ImageView();
        try {
            iv.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
        } catch (Exception ignored) {}

        // ✅ Smaller circle size
        iv.setFitWidth(140);
        iv.setFitHeight(140);
        iv.setPreserveRatio(false);

        Circle clip = new Circle(70, 70, 70);
        iv.setClip(clip);

        StackPane circleHolder = new StackPane(iv);
        circleHolder.setMinSize(140, 140);
        circleHolder.setMaxSize(140, 140);
        circleHolder.getStyleClass().add("circle-holder");

        // ----- Button -----
        Button btn = new Button(title);
        btn.getStyleClass().add("topic-btn");

        // ✅ Smaller button
        btn.setPrefSize(160, 32);

        // ✅ Image first, button under
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

    public void showPage(Node node) {
        contentArea.getChildren().setAll(node);
    }
}
