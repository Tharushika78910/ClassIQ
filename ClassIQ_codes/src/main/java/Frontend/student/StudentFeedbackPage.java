package Frontend.student;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StudentFeedbackPage {

    public Node getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("student-page-bg");

        Label title = new Label("Feedback");
        title.getStyleClass().add("title-xl");

        Label text = new Label("Coming soon...");

        root.getChildren().addAll(title, text);
        return root;
    }
}
