package frontend;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UIUtil {

    private static final Logger LOGGER = Logger.getLogger(UIUtil.class.getName());

    private UIUtil() {
        // Utility class
    }

    public static ImageView loadImageView(Class<?> cls, String resourcePath) {
        try {
            URL url = cls.getResource(resourcePath);
            if (url != null) {
                return new ImageView(new Image(url.toExternalForm()));
            }

            LOGGER.warning(() -> String.format("Resource not found: %s", resourcePath));
        } catch (Exception exception) {
            LOGGER.log(
                    Level.WARNING,
                    exception,
                    () -> String.format("Failed to load image resource: %s", resourcePath)
            );
        }

        return null;
    }
}