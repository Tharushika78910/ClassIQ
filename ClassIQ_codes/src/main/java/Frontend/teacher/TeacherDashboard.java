package Frontend.teacher;

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
import Frontend.student.StudentMyGradesPage;


import java.util.Objects;

public class TeacherDashboard extends BorderPane {

    private final StackPane contentArea = new StackPane();

    // Update image paths if needed
    private static final String BG_IMAGE = "/Login.png";
    private static final String MARK_IMAGE = "/images/Mark sheet.png";
    private static final String GRADING_IMAGE = "/images/Grading.png";
    private static final String STUDENT_IMAGE = "/images/studentinfo.png";

    public TeacherDashboard(String name, String email, String profileImagePath) {

        getStyleClass().add("teacher-root");

        // Load dashboard CSS
        getStylesheets().add(
                getClass().getResource("/css/teacher-dashboard.css").toExternalForm()
        );

        setCenter(contentArea);

        // Show dashboard home (like before)
        showPage(buildHomeView(name, email, profileImagePath));
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

        // ===== Teacher Info (Top Right) =====
        HBox teacherBox = buildTeacherInfo(name, email, profileImagePath);
        AnchorPane.setTopAnchor(teacherBox, 20.0);
        AnchorPane.setRightAnchor(teacherBox, 20.0);

        // ===== Mark Sheet =====
        VBox markBlock = buildTopicBlock("Mark sheet", MARK_IMAGE);
        AnchorPane.setTopAnchor(markBlock, 100.0);
        AnchorPane.setLeftAnchor(markBlock, 140.0);

        // ===== Grading Criteria =====
        VBox gradingBlock = buildTopicBlock("Grading Criteria", GRADING_IMAGE);
        AnchorPane.setTopAnchor(gradingBlock, 100.0);
        AnchorPane.setLeftAnchor(gradingBlock, 520.0);

        // ===== Student Info =====
        VBox studentBlock = buildTopicBlock("Student Info", STUDENT_IMAGE);
        AnchorPane.setTopAnchor(studentBlock, 330.0);
        AnchorPane.setLeftAnchor(studentBlock, 330.0);

        // ===== Logout (Bottom Left) =====
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("logout-btn");
        AnchorPane.setRightAnchor(logoutBtn, 20.0);
        AnchorPane.setBottomAnchor(logoutBtn, 20.0);

        // ===== Button Actions (Same as Before) =====
        Button markBtn = (Button) markBlock.getChildren().get(0);
        Button gradingBtn = (Button) gradingBlock.getChildren().get(0);
        Button studentBtn = (Button) studentBlock.getChildren().get(0);

        markBtn.setOnAction(e -> showPage(new TeacherMarkSheetPage().getView()));
        gradingBtn.setOnAction(e -> showPage(new StudentMyGradesPage().getView()));
        studentBtn.setOnAction(e -> showPage(simplePlaceholder("Students Info (placeholder)")));

        logoutBtn.setOnAction(e -> showPage(simplePlaceholder("Logged out (placeholder)")));

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

    private VBox buildTopicBlock(String title, String imagePath) {

        VBox box = new VBox(15);
        box.setAlignment(Pos.TOP_CENTER);

        Button btn = new Button(title);
        btn.getStyleClass().add("topic-btn");

        btn.setMinSize(220, 44);
        btn.setMaxSize(220, 44);

        ImageView iv = new ImageView();
        try {
            iv.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
        } catch (Exception ignored) {}

        iv.setFitWidth(180);
        iv.setFitHeight(180);
        iv.setPreserveRatio(false);

        Circle clip = new Circle(90, 90, 90);
        iv.setClip(clip);

        StackPane circleHolder = new StackPane(iv);
        circleHolder.getStyleClass().add("circle-holder");

        box.getChildren().addAll(btn, circleHolder);
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

    private void showPage(Node node) {
        contentArea.getChildren().setAll(node);
    }
}
