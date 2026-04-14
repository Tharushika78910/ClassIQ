package frontend;

import backend.model.dao.impl.AppUserDaoImpl;
import backend.model.dao.impl.StudentDaoImpl;
import backend.model.dao.impl.UserProfileDaoImpl;
import backend.model.entity.Student;
import frontend.student.StudentDashboard;
import frontend.teacher.TeacherDashboard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginPage {

    private static final String BG_IMAGE = "/Login.png";
    private static final String LOGO = "/Logoline.png";
    private static final String STUDENT_AVATAR = "/Student.png";
    private static final String TEACHER_AVATAR = "/Teacher.png";

    private static final String MESSAGE_BUNDLE = "messages";
    private static final String RTL_TEXT_FIELD_STYLE =
            "-fx-text-alignment: right; -fx-alignment: CENTER_RIGHT;";
    private static final Locale DEFAULT_LOCALE = new Locale("en", "US");

    private final Stage stage;
    private Locale currentLocale = DEFAULT_LOCALE;

    public LoginPage(Stage stage) {
        this.stage = stage;
    }

    public LoginPage(Stage stage, Locale locale) {
        this.stage = stage;
        this.currentLocale = locale == null ? DEFAULT_LOCALE : locale;
        Session.setCurrentLocale(this.currentLocale);
    }

    public Scene getScene() {
        return new Scene(getView(), Main.APP_WIDTH, Main.APP_HEIGHT);
    }

    public Parent getView() {
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGE_BUNDLE, currentLocale);

        StackPane root = new StackPane();
        root.getChildren().add(createBackgroundView());

        VBox mainBox = createMainBox();
        ImageView logo = loadImageViewOrEmpty(LOGO);
        configureLogo(logo);

        HBox cards = createCardsBox();
        VBox teacherCard = buildLoginCard(bundle.getString("i.am.teacher"), TEACHER_AVATAR, "TEACHER");
        VBox studentCard = buildLoginCard(bundle.getString("i.am.student"), STUDENT_AVATAR, "STUDENT");
        cards.getChildren().addAll(teacherCard, studentCard);

        mainBox.getChildren().addAll(logo, cards);
        root.getChildren().add(mainBox);

        Button backButton = createBackButton(bundle);
        positionBackButton(backButton);
        root.getChildren().add(backButton);

        return root;
    }

    private VBox createMainBox() {
        VBox mainBox = new VBox(25);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        mainBox.setBackground(Background.EMPTY);
        mainBox.setStyle("-fx-background-color: transparent;");
        mainBox.setTranslateY(-25);
        return mainBox;
    }

    private HBox createCardsBox() {
        HBox cards = new HBox(50);
        cards.setAlignment(Pos.CENTER);
        cards.setBackground(Background.EMPTY);
        cards.setStyle("-fx-background-color: transparent;");
        cards.setTranslateY(-60);
        return cards;
    }

    private void configureLogo(ImageView logo) {
        logo.setPreserveRatio(true);
        logo.setSmooth(true);
        logo.setFitWidth(350);
        VBox.setMargin(logo, new Insets(20, 0, 0, 0));
    }

    private Button createBackButton(ResourceBundle bundle) {
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

        Button btnBack = new Button(bundle.getString("back") + (isRTL(currentLocale) ? " →" : " ←"));
        btnBack.setStyle(pillNormal);
        btnBack.setOnMouseEntered(e -> btnBack.setStyle(pillHover));
        btnBack.setOnMouseExited(e -> btnBack.setStyle(pillNormal));
        btnBack.setOnAction(e -> {
            HomePage home = new HomePage(currentLocale);
            stage.setScene(home.getScene(stage, currentLocale));
        });
        return btnBack;
    }

    private void positionBackButton(Button btnBack) {
        if (isRTL(currentLocale)) {
            StackPane.setAlignment(btnBack, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(btnBack, new Insets(0, 25, 25, 0));
        } else {
            StackPane.setAlignment(btnBack, Pos.BOTTOM_LEFT);
            StackPane.setMargin(btnBack, new Insets(0, 0, 25, 25));
        }
    }

    private VBox buildLoginCard(String titleText, String avatarPath, String roleType) {
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGE_BUNDLE, currentLocale);

        VBox card = createCardContainer();
        ImageView avatar = loadImageViewOrEmpty(avatarPath);
        avatar.setFitWidth(90);
        avatar.setPreserveRatio(true);

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField tfUser = new TextField();
        tfUser.setPromptText(bundle.getString("username"));
        tfUser.setPrefWidth(240);

        PasswordField tfPass = new PasswordField();
        tfPass.setPromptText(bundle.getString("password"));
        tfPass.setPrefWidth(240);

        applyRtlFieldStyleIfNeeded(tfUser, tfPass);

        Hyperlink forgot = new Hyperlink(bundle.getString("forgot.password"));
        forgot.setStyle("-fx-text-fill: #2F6DAA; -fx-font-size: 13px;");
        forgot.setOnAction(e -> showForgotPasswordDialog());

        Label error = new Label();
        error.setTextFill(Color.DARKRED);

        Button loginBtn = new Button(bundle.getString("log.in"));
        loginBtn.setPrefWidth(240);
        loginBtn.setOnAction(e -> handleLogin(roleType, tfUser, tfPass, error, bundle));

        card.getChildren().addAll(avatar, title, tfUser, tfPass, forgot, error, loginBtn);
        return card;
    }

    private VBox createCardContainer() {
        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(28));
        card.setPrefWidth(300);
        card.setStyle(
                "-fx-background-color: rgba(156,196,183,0.85);" +
                        "-fx-background-radius: 20;"
        );
        return card;
    }

    private void applyRtlFieldStyleIfNeeded(TextField tfUser, PasswordField tfPass) {
        if (isRTL(currentLocale)) {
            tfUser.setStyle(RTL_TEXT_FIELD_STYLE);
            tfPass.setStyle(RTL_TEXT_FIELD_STYLE);
        }
    }

    private void handleLogin(
            String roleType,
            TextField tfUser,
            PasswordField tfPass,
            Label error,
            ResourceBundle bundle
    ) {
        error.setText("");

        String username = tfUser.getText() == null ? "" : tfUser.getText().trim();
        String password = tfPass.getText() == null ? "" : tfPass.getText();

        if (username.isEmpty() || password.isEmpty()) {
            error.setText(bundle.getString("error.credentials.required"));
            return;
        }

        AppUserDaoImpl auth = new AppUserDaoImpl();
        AppUserDaoImpl.LoginResult result = auth.login(username, password);

        if (result == null) {
            error.setText(bundle.getString("error.credentials.invalid"));
            return;
        }

        if (!roleType.equalsIgnoreCase(result.role)) {
            error.setText(bundle.getString("error.role.mismatch"));
            return;
        }

        UserProfileDaoImpl profileDao = new UserProfileDaoImpl();
        if ("STUDENT".equalsIgnoreCase(result.role)) {
            handleStudentLogin(result, profileDao, error, bundle);
        } else {
            handleTeacherLogin(result, profileDao, error, bundle);
        }
    }

    private void handleStudentLogin(
            AppUserDaoImpl.LoginResult result,
            UserProfileDaoImpl profileDao,
            Label error,
            ResourceBundle bundle
    ) {
        var studentProfile = profileDao.findStudentByUserId(result.userId);
        if (studentProfile == null) {
            error.setText(bundle.getString("error.student.notfound"));
            return;
        }

        StudentDaoImpl studentDao = new StudentDaoImpl();
        Student fullStudent;

        try {
            fullStudent = studentDao.findById(studentProfile.studentId, currentLocale.getLanguage());
        } catch (Exception exception) {
            exception.printStackTrace();
            error.setText(bundle.getString("error.student.load.failed"));
            return;
        }

        if (fullStudent == null) {
            error.setText(bundle.getString("error.student.missing"));
            return;
        }

        Session.setRole(Session.Role.STUDENT);
        Session.setUserId(result.userId);
        Session.setCurrentStudent(fullStudent);
        Session.setCurrentLocale(currentLocale);

        String fullName = fullStudent.getFirstName() + " " + fullStudent.getLastName();
        StudentDashboard dash = new StudentDashboard(fullName, fullStudent.getEmail(), STUDENT_AVATAR);
        stage.getScene().setRoot(new StackPane(createBackgroundView(), dash));
    }

    private void handleTeacherLogin(
            AppUserDaoImpl.LoginResult result,
            UserProfileDaoImpl profileDao,
            Label error,
            ResourceBundle bundle
    ) {
        String languageCode = currentLocale != null ? currentLocale.getLanguage() : null;
        var teacherProfile = profileDao.findTeacherByUserId(result.userId, languageCode);

        if (teacherProfile == null) {
            error.setText(bundle.getString("error.teacher.notfound"));
            return;
        }

        Session.setRole(Session.Role.TEACHER);
        Session.setUserId(result.userId);
        Session.setTeacherId(teacherProfile.teacherId);
        Session.setTeacherSubject(teacherProfile.subject);
        Session.setCurrentLocale(currentLocale);

        TeacherDashboard dash = new TeacherDashboard(
                teacherProfile.name,
                teacherProfile.email,
                TEACHER_AVATAR
        );
        stage.getScene().setRoot(new StackPane(createBackgroundView(), dash));
    }

    private boolean isRTL(Locale locale) {
        return "ar".equals(locale.getLanguage());
    }

    private void showForgotPasswordDialog() {
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGE_BUNDLE, currentLocale);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("reset.password.title"));

        ButtonType resetBtn = new ButtonType(bundle.getString("reset"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType(bundle.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(resetBtn, cancelBtn);

        ForgotPasswordFields fields = createForgotPasswordFields(bundle);

        GridPane grid = createForgotPasswordGrid(bundle, fields);
        dialog.getDialogPane().setContent(grid);

        Button resetButton = (Button) dialog.getDialogPane().lookupButton(resetBtn);
        resetButton.addEventFilter(javafx.event.ActionEvent.ACTION, event ->
                handlePasswordReset(event, bundle, fields)
        );

        dialog.showAndWait();
    }

    private ForgotPasswordFields createForgotPasswordFields(ResourceBundle bundle) {
        TextField username = new TextField();
        username.setPromptText(bundle.getString("username"));

        TextField email = new TextField();
        email.setPromptText(bundle.getString("registered.email"));

        PasswordField newPass = new PasswordField();
        newPass.setPromptText(bundle.getString("new.password.label"));

        PasswordField confirm = new PasswordField();
        confirm.setPromptText(bundle.getString("confirm.password"));

        if (isRTL(currentLocale)) {
            username.setStyle(RTL_TEXT_FIELD_STYLE);
            email.setStyle(RTL_TEXT_FIELD_STYLE);
            newPass.setStyle(RTL_TEXT_FIELD_STYLE);
            confirm.setStyle(RTL_TEXT_FIELD_STYLE);
        }

        Label msg = new Label();
        msg.setTextFill(Color.RED);

        return new ForgotPasswordFields(username, email, newPass, confirm, msg);
    }

    private GridPane createForgotPasswordGrid(ResourceBundle bundle, ForgotPasswordFields fields) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label(bundle.getString("username.label")), 0, 0);
        grid.add(fields.username(), 1, 0);
        grid.add(new Label(bundle.getString("email.label")), 0, 1);
        grid.add(fields.email(), 1, 1);
        grid.add(new Label(bundle.getString("new.password.label")), 0, 2);
        grid.add(fields.newPass(), 1, 2);
        grid.add(new Label(bundle.getString("confirm.password.label")), 0, 3);
        grid.add(fields.confirm(), 1, 3);
        grid.add(fields.msg(), 1, 4);

        return grid;
    }

    private void handlePasswordReset(
            javafx.event.ActionEvent event,
            ResourceBundle bundle,
            ForgotPasswordFields fields
    ) {
        fields.msg().setText("");

        String username = fields.username().getText() == null ? "" : fields.username().getText().trim();
        String email = fields.email().getText() == null ? "" : fields.email().getText().trim();
        String password1 = fields.newPass().getText() == null ? "" : fields.newPass().getText();
        String password2 = fields.confirm().getText() == null ? "" : fields.confirm().getText();

        if (username.isEmpty() || email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            fields.msg().setText(bundle.getString("error.fields.required"));
            event.consume();
            return;
        }

        if (!password1.equals(password2)) {
            fields.msg().setText(bundle.getString("error.password.mismatch"));
            event.consume();
            return;
        }

        AppUserDaoImpl dao = new AppUserDaoImpl();
        Integer userId = dao.findUserIdByUsernameAndEmail(username, email);

        if (userId == null) {
            fields.msg().setText(bundle.getString("error.username.email.invalid"));
            event.consume();
            return;
        }

        boolean updated = dao.updatePassword(userId, password1);
        if (!updated) {
            fields.msg().setText(bundle.getString("error.reset.failed"));
            event.consume();
            return;
        }

        new Alert(Alert.AlertType.INFORMATION, bundle.getString("password.reset.success")).showAndWait();
    }

    private ImageView createBackgroundView() {
        ImageView bg = loadImageViewOrEmpty(BG_IMAGE);
        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setSmooth(true);
        return bg;
    }

    private ImageView loadImageViewOrEmpty(String resourcePath) {
        ImageView imageView = new ImageView();
        try (InputStream stream = getClass().getResourceAsStream(resourcePath)) {
            if (stream != null) {
                imageView.setImage(new Image(stream));
            }
        } catch (Exception exception) {
            // Resource is optional here. Keep the ImageView empty when the image cannot be loaded.
        }
        return imageView;
    }

    private record ForgotPasswordFields(
            TextField username,
            TextField email,
            PasswordField newPass,
            PasswordField confirm,
            Label msg
    ) {
    }
}