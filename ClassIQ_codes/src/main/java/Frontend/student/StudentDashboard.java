package Frontend.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class StudentDashboard extends StackPane {

    private final BorderPane layout = new BorderPane();
    private final StackPane contentArea = new StackPane();

    private Button btnHome, btnMyInfo, btnMyGrades, btnReportCard, btnFeedback, btnLogout;

    public StudentDashboard(String name, String email, String profileImagePath) {
        getStyleClass().add("student-root");

        getChildren().add(layout);

        layout.setPadding(new Insets(10));
        layout.setTop(buildHeader());
        layout.setLeft(buildSidebar(name, email, profileImagePath));
        layout.setCenter(contentArea);

        BorderPane.setMargin(contentArea, new Insets(20));

        setActive(btnHome);
        showPage(buildHomeView());
    }

    private Node buildHeader() {
        HBox header = new HBox();
        header.getStyleClass().add("header-bar");
        header.setPadding(new Insets(10));
        return header;
    }

    private Node buildSidebar(String name, String email, String profileImagePath) {
        VBox sidebar = new VBox(14);
        sidebar.setPrefWidth(240);
        sidebar.getStyleClass().add("student-sidebar");
        sidebar.setPadding(new Insets(15));

        VBox profileBox = new VBox(6);
        profileBox.setAlignment(Pos.CENTER);
        profileBox.getStyleClass().add("student-profile-box");
        profileBox.setPadding(new Insets(12));

        ImageView avatar = new ImageView();
        try {
            avatar.setImage(new Image(getClass().getResourceAsStream(profileImagePath)));
        } catch (Exception ignored) {}
        avatar.setFitWidth(42);
        avatar.setFitHeight(42);
        avatar.setPreserveRatio(true);

        Label nameLbl = new Label(name);
        nameLbl.getStyleClass().add("profile-name");

        Label emailLbl = new Label(email);
        emailLbl.getStyleClass().add("profile-email");

        profileBox.getChildren().addAll(avatar, nameLbl, emailLbl);

        // buttons
        btnHome = new Button("Home");
        btnMyInfo = new Button("My Info");
        btnMyGrades = new Button("My Grades");
        btnReportCard = new Button("Report Card");
        btnFeedback = new Button("Feedback");
        btnLogout = new Button("Log out");

        btnHome.getStyleClass().add("student-menu-btn");
        btnMyInfo.getStyleClass().add("student-menu-btn");
        btnMyGrades.getStyleClass().add("student-menu-btn");
        btnReportCard.getStyleClass().add("student-menu-btn");
        btnFeedback.getStyleClass().add("student-menu-btn");
        btnLogout.getStyleClass().add("student-menu-btn");

        // ✅ Actions (NOW using separate page files)
        btnHome.setOnAction(e -> { setActive(btnHome); showPage(buildHomeView()); });

        btnMyInfo.setOnAction(e -> {
            setActive(btnMyInfo);
            showPage(wrapCard(new StudentMyInfoPage().getView(), 820, 520));
        });

        btnMyGrades.setOnAction(e -> {
            setActive(btnMyGrades);
            showPage(wrapCard(new StudentMyGradesPage().getView(), 720, 380));
        });

        btnReportCard.setOnAction(e -> {
            setActive(btnReportCard);
            showPage(wrapCard(new StudentReportCardPage().getView(), 860, 520));
        });

        btnFeedback.setOnAction(e -> {
            setActive(btnFeedback);
            showPage(wrapCard(new StudentFeedbackPage().getView(), 720, 320));
        });

        btnLogout.setOnAction(e -> {
            setActive(btnLogout);
            showPage(buildPlaceholderView("Logged out (placeholder)"));
        });

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        sidebar.getChildren().addAll(
                profileBox,
                btnHome,
                btnMyInfo,
                btnMyGrades,
                btnReportCard,
                btnFeedback,
                grow,
                btnLogout
        );

        return sidebar;
    }

    private Node buildHomeView() {
        VBox card = new VBox(10);
        card.getStyleClass().add("student-page-bg");
        card.setPadding(new Insets(20));

        Label t = new Label("Student Home");
        t.getStyleClass().add("title-xl");

        Label s = new Label("Welcome! Use the menu to navigate.");

        card.getChildren().addAll(t, s);

        return wrapCard(card, 560, 260);
    }

    private Node buildPlaceholderView(String text) {
        VBox card = new VBox(10);
        card.getStyleClass().add("student-page-bg");
        card.setPadding(new Insets(20));
        Label t = new Label(text);
        t.getStyleClass().add("title-xl");
        card.getChildren().add(t);
        return wrapCard(card, 640, 260);
    }

    /**
     * Wrap content into a centered card-sized container
     * so the background image stays visible.
     */
    private Node wrapCard(Node card, double maxW, double maxH) {
        if (card instanceof Region r) {
            r.setMaxWidth(maxW);
            r.setMaxHeight(maxH);
        }

        StackPane wrapper = new StackPane(card);
        wrapper.setAlignment(Pos.TOP_LEFT);
        wrapper.setPadding(new Insets(40));
        return wrapper;
    }

    private void setActive(Button active) {
        btnHome.getStyleClass().remove("student-menu-active");
        btnMyInfo.getStyleClass().remove("student-menu-active");
        btnMyGrades.getStyleClass().remove("student-menu-active");
        btnReportCard.getStyleClass().remove("student-menu-active");
        btnFeedback.getStyleClass().remove("student-menu-active");
        btnLogout.getStyleClass().remove("student-menu-active");
        active.getStyleClass().add("student-menu-active");
    }

    private void showPage(Node node) {
        contentArea.getChildren().setAll(node);
    }
}
