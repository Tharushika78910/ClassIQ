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
import java.util.ResourceBundle;

import java.util.Locale;

public class StudentMyInfoPage {

    private final StudentDashboard dashboard;
    private final ResourceBundle bundle;

    public StudentMyInfoPage(StudentDashboard dashboard) {
        this.dashboard = dashboard;
        this.bundle = ResourceBundle.getBundle("messages", Session.getCurrentLocale());
    }

    public Parent getView() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-bg");

        root.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/student-dashboard.css")).toExternalForm()
        );

        Student s = Session.getCurrentStudent();

        StackPane center = new StackPane();
        center.setAlignment(Pos.CENTER);

        if (s == null) {
            Label err = new Label(bundle.getString("student.myinfo.error.noSession"));
            err.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");
            center.getChildren().add(err);
            root.setCenter(center);
        } else {

            String fullName = s.getFullName();

            VBox leftContent = new VBox(40);
            leftContent.setAlignment(Pos.TOP_LEFT);

            Label title = new Label(bundle.getString("student.myInfo"));
            title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: black;");

            GridPane grid = new GridPane();
            grid.setVgap(22);
            grid.setHgap(60);
            grid.setAlignment(Pos.TOP_LEFT);

            String labelStyle = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black;";
            String valueStyle = "-fx-font-size: 20px; -fx-text-fill: black;";

            Label lblName = new Label(bundle.getString("student.myinfo.fullName"));
            lblName.setStyle(labelStyle);
            Label valName = new Label(fullName);
            valName.setStyle(valueStyle);

            Label lblStudentNo = new Label(bundle.getString("student.myinfo.studentNumber"));
            lblStudentNo.setStyle(labelStyle);
            Label valStudentNo = new Label(s.getStudentNumber());
            valStudentNo.setStyle(valueStyle);

            Label lblEmail = new Label(bundle.getString("student.myinfo.email"));
            lblEmail.setStyle(labelStyle);
            Label valEmail = new Label(s.getEmail());
            valEmail.setStyle(valueStyle);

            Label lblClass = new Label(bundle.getString("student.myinfo.class"));
            lblClass.setStyle(labelStyle);
            Label valClass = new Label("10A");
            valClass.setStyle(valueStyle);

            grid.add(lblName, 0, 0);      grid.add(valName, 1, 0);
            grid.add(lblStudentNo, 0, 1); grid.add(valStudentNo, 1, 1);
            grid.add(lblEmail, 0, 2);     grid.add(valEmail, 1, 2);
            grid.add(lblClass, 0, 3);     grid.add(valClass, 1, 3);

            leftContent.getChildren().addAll(title, grid);

            Separator divider = new Separator(Orientation.VERTICAL);
            divider.setPrefHeight(540);
            divider.setStyle("-fx-opacity: 0.75;");

            VBox rightContent = new VBox(14);
            rightContent.setAlignment(Pos.TOP_LEFT);

            String sectionTitleStyle = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black;";
            String smallTextStyle = "-fx-font-size: 14px; -fx-text-fill: black;";

            Label termTitle = new Label(bundle.getString("student.myinfo.termDates"));
            termTitle.setStyle(sectionTitleStyle);

            Label termStart = new Label(bundle.getString("student.myinfo.termStart"));
            termStart.setStyle(smallTextStyle);

            Label termEnd = new Label(bundle.getString("student.myinfo.termEnd"));
            termEnd.setStyle(smallTextStyle);

            Separator termLine = new Separator();
            termLine.setStyle("-fx-opacity: 0.8;");

            Label holidaysTitle = new Label(bundle.getString("student.myinfo.holidays"));
            holidaysTitle.setStyle(sectionTitleStyle);

            Label hol1 = new Label(bundle.getString("student.myinfo.holiday.1"));
            hol1.setStyle(smallTextStyle);
            hol1.setWrapText(true);

            Label hol2 = new Label(bundle.getString("student.myinfo.holiday.2"));
            hol2.setStyle(smallTextStyle);
            hol2.setWrapText(true);

            Label hol3 = new Label(bundle.getString("student.myinfo.holiday.3"));
            hol3.setStyle(smallTextStyle);
            hol3.setWrapText(true);

            Label rulesTitle = new Label(bundle.getString("student.myinfo.rulesTitle"));
            rulesTitle.setStyle(sectionTitleStyle);

            Text rulesText = new Text(bundle.getString("student.myinfo.rulesText"));
            rulesText.setStyle(smallTextStyle);
            TextFlow rulesFlow = new TextFlow(rulesText);
            rulesFlow.setMaxWidth(460);

            Label clubsTitle = new Label(bundle.getString("student.myinfo.clubsTitle"));
            clubsTitle.setStyle(sectionTitleStyle);

            Text clubsText = new Text(bundle.getString("student.myinfo.clubsText"));
            clubsText.setStyle(smallTextStyle);
            TextFlow clubsFlow = new TextFlow(clubsText);
            clubsFlow.setMaxWidth(460);

            Label campTitle = new Label(bundle.getString("student.myinfo.campTitle"));
            campTitle.setStyle(sectionTitleStyle);

            Text campText = new Text(bundle.getString("student.myinfo.campText"));
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

            center.getChildren().add(main);
            root.setCenter(center);
        }

        Button btnBack = dashboard.createStudentBackButton(() -> dashboard.showHome());

        Button btnLogout = dashboard.createStudentLogoutButton(() -> {
            Locale savedLocale = Session.getCurrentLocale();
            Session.clear();
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new LoginPage(stage, savedLocale).getScene());
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