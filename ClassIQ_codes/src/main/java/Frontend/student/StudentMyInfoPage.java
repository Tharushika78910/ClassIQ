package Frontend.student;

import Backend.model.entity.Student;
import Frontend.LoginPage;
import Frontend.Session;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.Objects;

public class StudentMyInfoPage {

    private final StudentDashboard dashboard;

    public StudentMyInfoPage(StudentDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Parent getView() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-bg");

        // Add CSS stylesheet
        root.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/student-dashboard.css")).toExternalForm()
        );

        Student s = Session.getCurrentStudent();

        // CENTER CONTENT
        StackPane center = new StackPane();
        center.setAlignment(Pos.CENTER);

        if (s == null) {
            Label err = new Label("No student session found. Please log in again.");
            err.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");
            center.getChildren().add(err);
            root.setCenter(center);
        } else {

            String fullName = s.getFirstName() + " " + s.getLastName();


            VBox leftContent = new VBox(40);
            leftContent.setAlignment(Pos.TOP_LEFT);

            Label title = new Label("My Info");
            title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: black;");

            GridPane grid = new GridPane();
            grid.setVgap(22);
            grid.setHgap(60);
            grid.setAlignment(Pos.TOP_LEFT);

            String labelStyle = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black;";
            String valueStyle = "-fx-font-size: 20px; -fx-text-fill: black;";

            Label lblName = new Label("Full Name"); lblName.setStyle(labelStyle);
            Label valName = new Label(fullName); valName.setStyle(valueStyle);

            Label lblStudentNo = new Label("Student Number"); lblStudentNo.setStyle(labelStyle);
            Label valStudentNo = new Label(s.getStudentNumber()); valStudentNo.setStyle(valueStyle);

            Label lblEmail = new Label("Email"); lblEmail.setStyle(labelStyle);
            Label valEmail = new Label(s.getEmail()); valEmail.setStyle(valueStyle);

            Label lblClass = new Label("Class"); lblClass.setStyle(labelStyle);
            Label valClass = new Label("10A"); valClass.setStyle(valueStyle);

            grid.add(lblName, 0, 0);       grid.add(valName, 1, 0);
            grid.add(lblStudentNo, 0, 1);  grid.add(valStudentNo, 1, 1);
            grid.add(lblEmail, 0, 2);      grid.add(valEmail, 1, 2);
            grid.add(lblClass, 0, 3);      grid.add(valClass, 1, 3);

            leftContent.getChildren().addAll(title, grid);

            // =========================
            // MIDDLE DIVIDER
            // =========================
            Separator divider = new Separator(Orientation.VERTICAL);
            divider.setPrefHeight(540);
            divider.setStyle("-fx-opacity: 0.75;");

            // =========================
            // RIGHT SIDE CONTENT (SHORT) -> BOX + SCROLL
            // =========================
            VBox rightContent = new VBox(14);
            rightContent.setAlignment(Pos.TOP_LEFT);

            String sectionTitleStyle = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black;";
            String smallTextStyle = "-fx-font-size: 14px; -fx-text-fill: black;";

            // Term Dates
            Label termTitle = new Label("Term Dates");
            termTitle.setStyle(sectionTitleStyle);

            Label termStart = new Label("Term Start Date: 15.01.2026");
            termStart.setStyle(smallTextStyle);

            Label termEnd = new Label("Term End Date: 12.06.2026");
            termEnd.setStyle(smallTextStyle);

            Separator termLine = new Separator();
            termLine.setStyle("-fx-opacity: 0.8;");

            // Holidays
            Label holidaysTitle = new Label("Holidays");
            holidaysTitle.setStyle(sectionTitleStyle);

            Label hol1 = new Label("01.01.2026 – 14.01.2026 : Starting Holidays");
            hol1.setStyle(smallTextStyle);
            hol1.setWrapText(true);

            Label hol2 = new Label("15.02.2026 – 22.02.2026 : Winter Break / Ski Holiday");
            hol2.setStyle(smallTextStyle);
            hol2.setWrapText(true);

            Label hol3 = new Label("13.06.2026 – 14.08.2026 : Summer Holiday");
            hol3.setStyle(smallTextStyle);
            hol3.setWrapText(true);

            // Rules (4 only)
            Label rulesTitle = new Label("Rules to Follow");
            rulesTitle.setStyle(sectionTitleStyle);

            Text rulesText = new Text(
                    "1. Be on time and attend regularly.\n" +
                            "2. Respect teachers and classmates.\n" +
                            "3. Phones only with teacher permission.\n" +
                            "4. Submit homework on time."
            );
            rulesText.setStyle(smallTextStyle);
            TextFlow rulesFlow = new TextFlow(rulesText);
            rulesFlow.setMaxWidth(460);

            // Clubs
            Label clubsTitle = new Label("Clubs & Extra Activities");
            clubsTitle.setStyle(sectionTitleStyle);

            Text clubsText = new Text(
                    "• Sports Club\n" +
                            "• Music Club\n" +
                            "• Art Club\n" +
                            "• Coding Club\n" +
                            "• Science Club"
            );
            clubsText.setStyle(smallTextStyle);
            TextFlow clubsFlow = new TextFlow(clubsText);
            clubsFlow.setMaxWidth(460);

            // Summer Camp (short)
            Label campTitle = new Label("Summer Camp");
            campTitle.setStyle(sectionTitleStyle);

            Text campText = new Text(
                    "• July (dates announced later)\n" +
                            "• Outdoor games and sports\n" +
                            "• Registration required"
            );
            campText.setStyle(smallTextStyle);
            TextFlow campFlow = new TextFlow(campText);
            campFlow.setMaxWidth(460);

            rightContent.getChildren().addAll(
                    termTitle, termStart, termEnd, termLine,
                    holidaysTitle, hol1, hol2, hol3,
                    rulesTitle, rulesFlow,
                    clubsTitle, clubsFlow,
                    campTitle, campFlow
            );

            // BOX for right side
            VBox rightBox = new VBox();
            rightBox.setPadding(new Insets(18));
            rightBox.getChildren().add(rightContent);

            rightBox.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.85);" +
                            "-fx-background-radius: 16;" +
                            "-fx-border-radius: 16;" +
                            "-fx-border-color: rgba(0,0,0,0.18);" +
                            "-fx-border-width: 1;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 10, 0.20, 0, 4);"
            );

            // SCROLL for right side
            ScrollPane rightScroll = new ScrollPane(rightBox);
            rightScroll.setFitToWidth(true);
            rightScroll.setPannable(true);
            rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            rightScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            rightScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
            rightScroll.setPrefViewportHeight(520);


            HBox main = new HBox(25);
            main.setAlignment(Pos.TOP_LEFT);


            main.setPadding(new Insets(120, 60, 40, 150));

            leftContent.setPrefWidth(520);
            rightScroll.setPrefWidth(520);

            main.getChildren().addAll(leftContent, divider, rightScroll);
           // HBox.setMargin(rightScroll, new Insets(0, 0, 0, 20));

            center.getChildren().add(main);
            root.setCenter(center);
        }


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

        Button btnBack = dashboard.createStudentBackButton(() -> dashboard.showHome());

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle(pillNormal);
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle(pillHover));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle(pillNormal));
        btnLogout.setOnAction(e -> {
            Session.clear();
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new LoginPage(stage).getScene());
        });

        AnchorPane bottomBar = new AnchorPane();
        bottomBar.setPadding(new Insets(15));

        AnchorPane.setLeftAnchor(btnBack, 20.0);
        AnchorPane.setBottomAnchor(btnBack, 10.0);

        AnchorPane.setRightAnchor(btnLogout, 20.0);
        AnchorPane.setBottomAnchor(btnLogout, 10.0);

        bottomBar.getChildren().addAll(btnBack, btnLogout);
        root.setBottom(bottomBar);

        return root;
    }
}