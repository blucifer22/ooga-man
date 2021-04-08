package ooga.view.theme;

import java.util.HashSet;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import ooga.view.theme.api.Costume;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

public class ConcreteThemeService implements ThemeService {

  private final HashSet<ThemedObject> observers;

  public ConcreteThemeService() {
    observers = new HashSet<>();
  }

  @Override
  public Costume getCostumeForObjectOfType(String type) {
    return switch (type) {
      case "pacman" -> new Costume() {

        @Override
        public Paint getFill() {
          return new ImagePattern(new Image("resources/themes/classic/images/pacman.png"));
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
      case "ghost" -> new Costume() {
        @Override
        public Paint getFill() {
          return Color.RED;
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
      case "tile" -> new Costume() {
        @Override
        public Paint getFill() {
          return Color.BLACK;
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
      default -> new Costume() {
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
    };
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

