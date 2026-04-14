package frontend.student;

import frontend.LoginPage;
import frontend.Session;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudentDashboard extends BorderPane {

    private final StackPane contentArea = new StackPane();

    private final String studentName;
    private final String studentEmail;
    private final String studentProfileImagePath;
    private final ResourceBundle bundle;

    private static final String INFO_IMAGE = "/images/StudentInfoS.png";
    private static final String GRADING_IMAGE = "/images/MarkSheetS.png";
    private static final String REPORT_IMAGE = "/images/Report CardS.png";

    private static final String STUDENT_BACK_NORMAL =
            "-fx-background-color: rgba(210,230,255,0.95);" +
                    "-fx-text-fill: #1E4F9A;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-background-radius: 18;" +
                    "-fx-padding: 8 22 8 22;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10,0,0,2);";

    private static final String STUDENT_BACK_HOVER =
            "-fx-background-color: #7FB3FF;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-background-radius: 18;" +
                    "-fx-padding: 8 22 8 22;";

    private static final String LOGOUT_NORMAL =
            "-fx-background-color: rgba(210,230,255,0.95);" +
                    "-fx-text-fill: #1E4F9A;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-background-radius: 18;" +
                    "-fx-padding: 8 22 8 22;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10,0,0,2);";

    private static final String LOGOUT_HOVER =
            "-fx-background-color: #7FB3FF;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-background-radius: 18;" +
                    "-fx-padding: 8 22 8 22;";

    public StudentDashboard(String name, String email, String profileImagePath) {
        this.studentName = name;
        this.studentEmail = email;
        this.studentProfileImagePath = profileImagePath;
        this.bundle = ResourceBundle.getBundle("messages", Session.getCurrentLocale());

        getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/student-dashboard.css")).toExternalForm()
        );

        if ("ar".equals(Session.getCurrentLocale().getLanguage())) {
            setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }

        setCenter(contentArea);
        showHome();
    }

    public void showHome() {
        showPage(buildHomeView(studentName, studentEmail, studentProfileImagePath));
    }

    public Button createStudentBackButton(Runnable action) {
        Button btn = new Button("← " + bundle.getString("common.back"));
        btn.setStyle(STUDENT_BACK_NORMAL);
        btn.setOnMouseEntered(e -> btn.setStyle(STUDENT_BACK_HOVER));
        btn.setOnMouseExited(e -> btn.setStyle(STUDENT_BACK_NORMAL));
        btn.setOnAction(e -> action.run());
        return btn;
    }

    public Button createStudentLogoutButton(Runnable action) {
        Button btn = new Button(bundle.getString("student.dashboard.logout"));
        btn.setStyle(LOGOUT_NORMAL);
        btn.setOnMouseEntered(e -> btn.setStyle(LOGOUT_HOVER));
        btn.setOnMouseExited(e -> btn.setStyle(LOGOUT_NORMAL));
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private Node buildHomeView(String name, String email, String profileImagePath) {
        StackPane root = new StackPane();
        root.getStyleClass().add("figma-root");

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(30));

        HBox studentBox = buildStudentInfo(name, email, profileImagePath);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(spacer, studentBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        layout.setTop(topBar);

        VBox infoBlock = buildTopicBlock(bundle.getString("student.myInfo"), INFO_IMAGE);
        VBox gradingBlock = buildTopicBlock(bundle.getString("student.gradingCriteria"), GRADING_IMAGE);
        VBox reportBlock = buildTopicBlock(bundle.getString("student.reportCard"), REPORT_IMAGE);

        Button infoBtn = (Button) infoBlock.getChildren().get(1);
        Button btnGradingCriteria = (Button) gradingBlock.getChildren().get(1);
        Button reportBtn = (Button) reportBlock.getChildren().get(1);

        infoBtn.setOnAction(e -> showPage(new StudentMyInfoPage(this).getView()));
        btnGradingCriteria.setOnAction(e -> showPage(new StudentMyGradesPage(this).getView()));
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

        Button btnBack = createStudentBackButton(() -> {
            Locale savedLocale = Session.getCurrentLocale();
            Session.clear();
            Stage stage = (Stage) root.getScene().getWindow();
            LoginPage loginPage = new LoginPage(stage, savedLocale);
            stage.getScene().setRoot(loginPage.getView());
        });

        Button logoutBtn = createStudentLogoutButton(() -> {
            Locale savedLocale = Session.getCurrentLocale();
            Session.clear();
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new LoginPage(stage, savedLocale).getScene());
        });

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
        loadImageIfPresent(iv, imagePath);

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
        loadImageIfPresent(avatar, profileImagePath);

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

    private void loadImageIfPresent(ImageView imageView, String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return;
        }

        try (InputStream stream = getClass().getResourceAsStream(imagePath)) {
            if (stream != null) {
                imageView.setImage(new Image(stream));
            }
        } catch (java.io.IOException exception) {
            // Image is optional. Leave the ImageView empty if the resource cannot be read.
        }
    }

    public void showPage(Node node) {
        contentArea.getChildren().setAll(node);
    }
}