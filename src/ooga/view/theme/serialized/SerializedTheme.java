package ooga.view.theme.serialized;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ooga.view.theme.api.Costume;
import ooga.view.theme.api.Theme;

public class SerializedTheme implements Theme {

  private final Map<String, Costume> costumes;
  private final Map<String, Media> sounds;
  private final String stylesheet;
  private final String name;

  protected SerializedTheme(ThemeDescription description) {
    this.stylesheet = description.getStylesheet();
    this.costumes = new HashMap<>();
    this.sounds = new HashMap<>();
    this.name = description.getName();

    for (String key : description.getCostumes().keySet()) {
      costumes.put(key, description.getCostumes().get(key).toCostume());
    }

    for (String key : description.getAudioFilePaths().keySet()) {
      /*
       * getAbsoluteFile().getAbsolutePath() doesn't actually return the absolute path!
       * It (for whatever internal buggy Java reason) omits the /data from the filepath!
       * The Media class also ~requires~ an absolute filepath (why, Java?!)
       */

      String os = System.getProperty("os.name").toLowerCase();
      System.out.println(os);

      String encoded = "file://"+
          new File(description.getAudioFilePaths().get(key)).getAbsoluteFile().getAbsolutePath()
              .replace(" ", "%20") // Java doesn't attempt to URI-encode these :(
              .replace("/themes", "/data/themes") // workaround for Java bug
              .replace("\\", "/"); // fix for Windows systems :(

      if (os.contains("win")) { // yet another required fix for Windows systems
        encoded = encoded.replace("C:/", "/C:/");
      }

      sounds.put(key, new Media(encoded));
    }
  }

  @Override
  public Media getSoundByIdentifier(String identifier) {
    return sounds.get(identifier);
  }

  @Override
  public Costume getCostumeForObjectOfType(String type) {
    Costume ret = costumes.get(type);

    if (ret == null) {
      return new Costume() {
        @Override
        public Paint getFill() {
          return Color.ORANGE;
        }

        @Override
        public double getScale() {
          return 0.5;
        }

        @Override
        public boolean isBottomHeavy() {
          return true;
        }

        @Override
        public boolean isRotatable() {
          return true;
        }
      };
    } else {
      return ret;
    }
  }

  @Override
  public String getStylesheet() {
    return this.stylesheet;
  }

  @Override
  public String getName() {
    return this.name;
  }
}
