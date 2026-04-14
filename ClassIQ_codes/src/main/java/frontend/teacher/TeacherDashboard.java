package frontend.teacher;

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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class TeacherDashboard extends BorderPane {

    private static final String DEFAULT_LANGUAGE = "en";
    private static final String BG_IMAGE = "/Homepage.png";
    private static final String MARK_IMAGE = "/images/MarkSheet.png";
    private static final String GRADING_IMAGE = "/images/GradingCriteria.png";
    private static final String STUDENT_IMAGE = "/images/StudentInfo.png";

    private static final String STYLE_BOLD = "-fx-font-weight: bold;";
    private static final String STYLE_FONT_14 = "-fx-font-size: 14px;";
    private static final String STYLE_RADIUS_18 = "-fx-background-radius: 18;";
    private static final String STYLE_PADDING_8_22 = "-fx-padding: 8 22 8 22;";
    private static final String PILL_SHARED_STYLE =
            STYLE_BOLD +
                    STYLE_FONT_14 +
                    STYLE_RADIUS_18 +
                    STYLE_PADDING_8_22;

    private static final String PILL_NORMAL =
            "-fx-background-color: rgba(255,255,255,0.92);" +
                    "-fx-text-fill: #2E6F62;" +
                    PILL_SHARED_STYLE +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10,0,0,2);";

    private static final String PILL_HOVER =
            "-fx-background-color: #9AC4B7;" +
                    "-fx-text-fill: white;" +
                    PILL_SHARED_STYLE;

    private final StackPane contentArea = new StackPane();

    private final String teacherName;
    private final String teacherEmail;
    private final String teacherProfileImagePath;

    public TeacherDashboard(String name, String email, String profileImagePath) {
        this.teacherName = name;
        this.teacherEmail = email;
        this.teacherProfileImagePath = profileImagePath;

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
        ResourceBundle messages = getMessages();

        StackPane root = new StackPane();
        root.setNodeOrientation(getCurrentOrientation());
        root.getStyleClass().add("figma-root");
        applyBackgroundIfPresent(root, BG_IMAGE);

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        HBox teacherBox = buildTeacherInfo(name, email, profileImagePath);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(12, spacer, teacherBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        layout.setTop(topBar);

        VBox markBlock = buildTopicBlock(messages.getString("teacher.markSheet"), MARK_IMAGE);
        VBox gradingBlock = buildTopicBlock(messages.getString("teacher.gradingCriteria"), GRADING_IMAGE);
        VBox studentBlock = buildTopicBlock(messages.getString("teacher.studentFeedback"), STUDENT_IMAGE);

        Button markBtn = (Button) markBlock.getChildren().get(1);
        Button gradingBtn = (Button) gradingBlock.getChildren().get(1);
        Button studentBtn = (Button) studentBlock.getChildren().get(1);

        markBtn.setOnAction(event -> showTeacherMarkSheetPage());
        gradingBtn.setOnAction(event -> showTeacherMyGradesPage());
        studentBtn.setOnAction(event -> showTeacherStudentsInfoPage());

        GridPane grid = new GridPane();
        grid.setHgap(120);
        grid.setVgap(80);
        grid.setAlignment(Pos.CENTER);

        grid.add(markBlock, 0, 0);
        grid.add(gradingBlock, 1, 0);
        grid.add(studentBlock, 0, 1, 2, 1);
        GridPane.setHalignment(studentBlock, javafx.geometry.HPos.CENTER);

        StackPane centerWrap = new StackPane(grid);
        centerWrap.setAlignment(Pos.CENTER);
        layout.setCenter(centerWrap);

        Button btnBack = new Button(messages.getString("common.back"));
        btnBack.setStyle(PILL_NORMAL);
        btnBack.setOnMouseEntered(event -> btnBack.setStyle(PILL_HOVER));
        btnBack.setOnMouseExited(event -> btnBack.setStyle(PILL_NORMAL));
        btnBack.setOnAction(event -> {
            Locale savedLocale = Session.getCurrentLocale();
            Session.clear();
            Stage stage = (Stage) root.getScene().getWindow();
            LoginPage loginPage = new LoginPage(stage, savedLocale);
            stage.getScene().setRoot(loginPage.getView());
        });

        Button btnLogout = new Button(messages.getString("common.logout"));
        btnLogout.setStyle(PILL_NORMAL);
        btnLogout.setOnMouseEntered(event -> btnLogout.setStyle(PILL_HOVER));
        btnLogout.setOnMouseExited(event -> btnLogout.setStyle(PILL_NORMAL));
        btnLogout.setOnAction(event -> {
            Locale savedLocale = Session.getCurrentLocale();
            Session.clear();
            Stage stage = (Stage) getScene().getWindow();
            LoginPage loginPage = new LoginPage(stage, savedLocale);
            stage.getScene().setRoot(loginPage.getView());
        });

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

        ImageView imageView = new ImageView();
        loadImageIfPresent(imageView, imagePath);

        imageView.setFitWidth(140);
        imageView.setFitHeight(140);
        imageView.setPreserveRatio(false);

        Circle clip = new Circle(70, 70, 70);
        imageView.setClip(clip);

        StackPane circleHolder = new StackPane(imageView);
        circleHolder.setMinSize(140, 140);
        circleHolder.setMaxSize(140, 140);
        circleHolder.getStyleClass().add("circle-holder");

        Button button = new Button(title);
        button.getStyleClass().add("topic-btn");
        button.setPrefSize(160, 32);

        box.getChildren().addAll(circleHolder, button);
        return box;
    }

    private HBox buildTeacherInfo(String name, String email, String profileImagePath) {
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

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("teacher-name");

        Label emailLabel = new Label(email);
        emailLabel.getStyleClass().add("teacher-email");

        info.getChildren().addAll(nameLabel, emailLabel);
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
        } catch (IOException exception) {
            // Image is optional. Leave the ImageView empty if the resource cannot be read.
        }
    }

    private void applyBackgroundIfPresent(StackPane root, String imagePath) {
        try (InputStream stream = getClass().getResourceAsStream(imagePath)) {
            if (stream == null) {
                return;
            }

            BackgroundImage backgroundImage = new BackgroundImage(
                    new Image(stream),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(backgroundImage));
        } catch (IOException exception) {
            // Background image is optional. Keep the default background if the resource cannot be read.
        }
    }

    private ResourceBundle getMessages() {
        Locale locale = Session.getCurrentLocale() != null
                ? Session.getCurrentLocale()
                : new Locale(DEFAULT_LANGUAGE);
        return ResourceBundle.getBundle("messages", locale);
    }

    private NodeOrientation getCurrentOrientation() {
        Locale locale = Session.getCurrentLocale();
        return locale != null && "ar".equals(locale.getLanguage())
                ? NodeOrientation.RIGHT_TO_LEFT
                : NodeOrientation.LEFT_TO_RIGHT;
    }

    private Node wrapWithNavigation(Node pageContent, Runnable onBack) {
        ResourceBundle messages = getMessages();

        BorderPane wrapper = new BorderPane();
        wrapper.setNodeOrientation(getCurrentOrientation());
        wrapper.setCenter(pageContent);

        Button btnBack = new Button(messages.getString("common.back"));
        btnBack.setStyle(PILL_NORMAL);
        btnBack.setOnMouseEntered(event -> btnBack.setStyle(PILL_HOVER));
        btnBack.setOnMouseExited(event -> btnBack.setStyle(PILL_NORMAL));
        btnBack.setOnAction(event -> onBack.run());

        Button btnLogout = new Button(messages.getString("common.logout"));
        btnLogout.setStyle(PILL_NORMAL);
        btnLogout.setOnMouseEntered(event -> btnLogout.setStyle(PILL_HOVER));
        btnLogout.setOnMouseExited(event -> btnLogout.setStyle(PILL_NORMAL));
        btnLogout.setOnAction(event -> {
            Locale savedLocale = Session.getCurrentLocale();
            Session.clear();
            Stage stage = (Stage) getScene().getWindow();
            LoginPage loginPage = new LoginPage(stage, savedLocale);
            stage.getScene().setRoot(loginPage.getView());
        });

        AnchorPane bottomBar = new AnchorPane();
        bottomBar.setPadding(new Insets(15));

        AnchorPane.setLeftAnchor(btnBack, 20.0);
        AnchorPane.setBottomAnchor(btnBack, 10.0);

        AnchorPane.setRightAnchor(btnLogout, 20.0);
        AnchorPane.setBottomAnchor(btnLogout, 10.0);

        bottomBar.getChildren().addAll(btnBack, btnLogout);
        wrapper.setBottom(bottomBar);

        return wrapper;
    }

    public void showPage(Node node) {
        contentArea.getChildren().setAll(node);
    }

    public void showTeacherMarkSheetPage() {
        showPage(wrapWithNavigation(
                new TeacherMarkSheetPage(this).getView(),
                this::showHome
        ));
    }

    public void showTeacherMyGradesPage() {
        showPage(wrapWithNavigation(
                new TeacherMyGradesPage().getView(),
                this::showHome
        ));
    }

    public void showTeacherStudentsInfoPage() {
        showPage(wrapWithNavigation(
                new TeacherStudentsInfoPage(this).getView(),
                this::showHome
        ));
    }

    public void showTeacherStudentDetailsPage(String studentNumber) {
        showPage(wrapWithNavigation(
                new TeacherStudentDetailsPage(studentNumber).getView(),
                this::showTeacherStudentsInfoPage
        ));
    }
}