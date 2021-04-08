package ooga.view.theme;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashSet;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import ooga.view.theme.api.Costume;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.theme.serialized.ThemeDescription;

public class ConcreteThemeService implements ThemeService {

  private static final String THEME_PATH = "data/themes/";
  private final HashSet<ThemedObject> observers;
  private Theme theme;

  public ConcreteThemeService() {
    observers = new HashSet<>();
    try {
      this.theme =
          (new ObjectMapper()).readValue(new File("data/themes/classic/theme.json"),
              ThemeDescription.class).toTheme();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public Costume getCostumeForObjectOfType(String type) {
    Costume ret = this.theme.getCostumeForObjectOfType(type);

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

  @Override
  public Theme getTheme() {
    return this.theme;
  }

  @Override
  public void addThemedObject(ThemedObject themedObject) {
    this.observers.add(themedObject);
  }

  public void setTheme() {
    // TODO: change state to reflect new theme
    for (ThemedObject observer : observers) {
      observer.onThemeChange();
    }
  }
}

