package Frontend.student;

import Backend.model.entity.Student;
import Frontend.LoginPage;
import Frontend.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StudentMyInfoPage {

    private final StudentDashboard dashboard;

    public StudentMyInfoPage(StudentDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Parent getView() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("page-bg");

        Student s = Session.getCurrentStudent();

        // CENTER CONTENT (My Info block)
        StackPane center = new StackPane();
        center.setAlignment(Pos.CENTER);

        if (s == null) {
            Label err = new Label("No student session found. Please log in again.");
            err.setStyle("-fx-font-size: 18px;");
            center.getChildren().add(err);
            root.setCenter(center);
        } else {

            String fullName = s.getFirstName() + " " + s.getLastName();

            VBox content = new VBox(40);
            content.setAlignment(Pos.TOP_LEFT);
            content.setMaxWidth(650);

            // Position block
            content.setTranslateX(90);
            content.setTranslateY(110);

            Label title = new Label("My Info");
            title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold;");

            GridPane grid = new GridPane();
            grid.setVgap(22);
            grid.setHgap(60);
            grid.setAlignment(Pos.TOP_LEFT);

            String labelStyle = "-fx-font-size: 20px; -fx-font-weight: bold;";
            String valueStyle = "-fx-font-size: 20px;";

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

            content.getChildren().addAll(title, grid);
            center.getChildren().add(content);
            root.setCenter(center);
        }

        // BOTTOM BAR
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

        Button btnBack = new Button("← Back");
        btnBack.setStyle(pillNormal);
        btnBack.setOnMouseEntered(e -> btnBack.setStyle(pillHover));
        btnBack.setOnMouseExited(e -> btnBack.setStyle(pillNormal));
        btnBack.setOnAction(e -> {
            if (dashboard != null) dashboard.showHome();
        });

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