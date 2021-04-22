package ooga.view.theme.serialized;

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
      sounds.put(key, new Media(description.getAudioFilePaths().get(key)));
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
