package ooga.view.theme.serialized;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ooga.view.theme.api.Costume;
import ooga.view.theme.api.Theme;

public class DeserializedTheme implements Theme {
  private Map<String, Costume> costumes;

  protected DeserializedTheme(ThemeDescription description) {
    this.costumes = new HashMap<>();

    for (String key: description.getCostumes().keySet()) {
      costumes.put(key, description.getCostumes().get(key).toCostume());
    }
  }

  @Override
  public Costume getCostumeForObjectOfType(String type) {
    Costume ret = costumes.get(type);

    if (ret == null) {
      return new Costume() {
        @Override
        public Paint getFill() {
          return Color.NAVY;
        }

        @Override
        public double getScale() {
          return 1;
        }

        @Override
        public boolean isBottomHeavy() {
          return true;
        }
      };
    } else {
      return ret;
    }
  }
}
