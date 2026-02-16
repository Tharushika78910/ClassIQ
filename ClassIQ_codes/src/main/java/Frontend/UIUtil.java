package Frontend;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class UIUtil {
    public static ImageView loadImageView(Class<?> cls, String resourcePath) {
        try {
            URL url = cls.getResource(resourcePath);
            if (url != null) {
                ImageView iv = new ImageView(new Image(url.toExternalForm()));
                return iv;
            }
            System.err.println("Resource not found: " + resourcePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
