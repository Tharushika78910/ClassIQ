package Frontend.teacher;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class TeacherDashboard extends BorderPane {

    private final StackPane contentArea = new StackPane();

    public TeacherDashboard(String name, String email, String profileImagePath) {

        getStyleClass().add("teacher-root");

        setPadding(new Insets(10));
        setTop(buildHeader(name, email, profileImagePath));
        setLeft(buildSidebar());
        setCenter(contentArea);


        showPage(buildHomeView());
    }

    private Node buildHeader(String name, String email, String profileImagePath) {
        HBox header = new HBox(12);
        header.getStyleClass().add("header-bar");

        Label title = new Label("Teacher Dashboard");
        title.getStyleClass().add("header-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ImageView avatar = new ImageView();
        try {
            avatar.setImage(new Image(getClass().getResourceAsStream(profileImagePath)));
        } catch (Exception ignored) {}
        avatar.setFitWidth(44);
        avatar.setFitHeight(44);
        avatar.setPreserveRatio(true);

        VBox info = new VBox(2);
        Label nameLbl = new Label(name);
        nameLbl.setFont(Font.font(14));
        Label emailLbl = new Label(email);
        emailLbl.getStyleClass().add("muted-text");
        info.getChildren().addAll(nameLbl, emailLbl);

        header.getChildren().addAll(title, spacer, avatar, info);
        return header;
    }

    private Node buildSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(220);
        sidebar.getStyleClass().add("teacher-sidebar");
        sidebar.setPadding(new Insets(15));

        Label role = new Label("TEACHER");
        role.getStyleClass().add("sidebar-title");

        Button btnHome = new Button("Home");
        Button btnMarkSheets = new Button("Mark Sheets");
        Button btnGradingCriteria = new Button("Grading Criteria");
        Button btnStudentsInfo = new Button("Students Info");
        Button btnRemedial = new Button("Remedial Practices");
        Button btnLogout = new Button("Logout");

        btnHome.getStyleClass().add("teacher-menu-btn");
        btnMarkSheets.getStyleClass().add("teacher-menu-btn");
        btnGradingCriteria.getStyleClass().add("teacher-menu-btn");
        btnStudentsInfo.getStyleClass().add("teacher-menu-btn");
        btnRemedial.getStyleClass().add("teacher-menu-btn");
        btnLogout.getStyleClass().add("teacher-menu-btn");


        btnHome.setOnAction(e -> showPage(buildHomeView()));
        btnMarkSheets.setOnAction(e -> showPage(new TeacherMarkSheetPage().getView()));
        btnGradingCriteria.setOnAction(e -> showPage(simplePlaceholder("Grading Criteria (placeholder)")));
        btnStudentsInfo.setOnAction(e -> showPage(simplePlaceholder("Students Info (placeholder)")));
        btnRemedial.setOnAction(e -> showPage(simplePlaceholder("Remedial Practices (placeholder)")));
        btnLogout.setOnAction(e -> showPage(simplePlaceholder("Logged out (placeholder)")));

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        sidebar.getChildren().addAll(
                role,
                btnHome,
                btnMarkSheets,
                btnGradingCriteria,
                btnStudentsInfo,
                btnRemedial,
                grow,
                btnLogout
        );

        return sidebar;
    }

    //  Home screen layout
    private Node buildHomeView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("teacher-home-bg"); // style in teacher.css

        // Small rectangle at top
        Region topBox = new Region();
        topBox.setPrefHeight(36);
        topBox.setMaxWidth(240);
        topBox.getStyleClass().add("home-small-box");

        // Two big cards
        HBox cardsRow = new HBox(20);

        Region cardLeft = new Region();
        cardLeft.setPrefSize(360, 220);
        cardLeft.getStyleClass().add("home-card");

        Region cardRight = new Region();
        cardRight.setPrefSize(360, 220);
        cardRight.getStyleClass().add("home-card");

        cardsRow.getChildren().addAll(cardLeft, cardRight);

        root.getChildren().addAll(topBox, cardsRow);
        return root;
    }

    private Node simplePlaceholder(String text) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.getStyleClass().add("page-bg");

        Label lbl = new Label(text);
        lbl.getStyleClass().add("title-xl");

        box.getChildren().add(lbl);
        return box;
    }

    private void showPage(Node node) {
        contentArea.getChildren().setAll(node);
    }
}
