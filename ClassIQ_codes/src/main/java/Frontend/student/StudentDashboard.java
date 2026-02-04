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
        // ✅ background image is applied to this StackPane
        getStyleClass().add("student-root");

        // layout on top of background
        getChildren().add(layout);

        layout.setPadding(new Insets(10));
        layout.setTop(buildHeader());
        layout.setLeft(buildSidebar(name, email, profileImagePath));
        layout.setCenter(contentArea);

        // ✅ show background around content
        BorderPane.setMargin(contentArea, new Insets(20));

        // ✅ default page = Home (small card)
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

        // profile box (image + name + email under)
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

        btnHome.setOnAction(e -> { setActive(btnHome); showPage(buildHomeView()); });
        btnMyInfo.setOnAction(e -> { setActive(btnMyInfo); showPage(wrapCard(new StudentMyInfoPage().getView(), 820, 520)); });
        btnMyGrades.setOnAction(e -> { setActive(btnMyGrades); showPage(buildMyGradesView()); });
        btnReportCard.setOnAction(e -> { setActive(btnReportCard); showPage(buildReportCardView()); });
        btnFeedback.setOnAction(e -> { setActive(btnFeedback); showPage(buildFeedbackView()); });
        btnLogout.setOnAction(e -> { setActive(btnLogout); showPage(buildPlaceholderView("Logged out (placeholder)")); });

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

    // ✅ HOME = small card (does not cover background)
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

    // ✅ My Grades = medium card
    private Node buildMyGradesView() {
        VBox card = new VBox(10);
        card.getStyleClass().add("student-page-bg");
        card.setPadding(new Insets(20));

        Label t = new Label("My Grades");
        t.getStyleClass().add("title-xl");

        Label s = new Label("Grades page (placeholder).");

        card.getChildren().addAll(t, s);

        return wrapCard(card, 720, 380);
    }

    // ✅ Feedback = medium card
    private Node buildFeedbackView() {
        VBox card = new VBox(10);
        card.getStyleClass().add("student-page-bg");
        card.setPadding(new Insets(20));

        Label t = new Label("Feedback");
        t.getStyleClass().add("title-xl");

        Label s = new Label("Feedback page (placeholder).");

        card.getChildren().addAll(t, s);

        return wrapCard(card, 720, 320);
    }

    // ✅ Report Card = bigger card + student picture, but still not full screen
    private Node buildReportCardView() {
        BorderPane card = new BorderPane();
        card.getStyleClass().add("student-page-bg");
        card.setPadding(new Insets(25));

        VBox left = new VBox(14);

        Label title = new Label("My Profile");
        title.getStyleClass().add("title-xl");

        left.getChildren().addAll(
                title,
                new Label("Student Number : 00001111123"),
                new Label("Class          : 12E")
        );

        GridPane grid = new GridPane();
        grid.getStyleClass().add("marks-grid");

        addRow(grid, 0, "Math", "9");
        addRow(grid, 1, "English", "9");
        addRow(grid, 2, "Science", "8");
        addRow(grid, 3, "Language", "8");
        addRow(grid, 4, "Geography","8");
        addRow(grid, 5, "Total\nMarks", "42");
        addRow(grid, 6, "Average", "8.4");

        left.getChildren().add(grid);

        ImageView illus = new ImageView();
        try {
            illus.setImage(new Image(getClass().getResourceAsStream("/Student.png")));
        } catch (Exception ignored) {}
        illus.setFitWidth(220);
        illus.setPreserveRatio(true);

        card.setLeft(left);
        card.setRight(illus);
        BorderPane.setMargin(illus, new Insets(60, 10, 0, 0));

        return wrapCard(card, 860, 520);
    }

    private void addRow(GridPane grid, int row, String label, String value) {
        Label l = new Label(label);
        l.getStyleClass().add("marks-cell");

        Label v = new Label(value);
        v.getStyleClass().add("marks-cell");

        grid.add(l, 0, row);
        grid.add(v, 1, row);
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
     * ✅ Wrap any content into a centered card-sized container
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
