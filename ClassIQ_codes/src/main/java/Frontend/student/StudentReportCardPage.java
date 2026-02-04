package Frontend.student;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class StudentReportCardPage {

    public Node getView() {
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

        return card;
    }

    private void addRow(GridPane grid, int row, String label, String value) {
        Label l = new Label(label);
        l.getStyleClass().add("marks-cell");

        Label v = new Label(value);
        v.getStyleClass().add("marks-cell");

        grid.add(l, 0, row);
        grid.add(v, 1, row);
    }
}
