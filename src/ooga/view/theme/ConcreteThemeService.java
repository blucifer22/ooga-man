package ooga.view.theme;

import java.util.HashSet;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ConcreteThemeService implements ThemeService {

  private final HashSet<ThemedObject> observers;

  public ConcreteThemeService() {
    observers = new HashSet<>();
  }

  @Override
  public Paint getFillForObjectOfType(String type) {
    // TODO: use Marc's JSON parsing to determine how themes should work
    // TODO: implement asset loading
    // TODO: change switch statement to a map <String, Paint> with assetType -> background
    return switch (type) {
      case "pacman" -> Color.YELLOW;
      case "ghost" -> Color.RED;
      case "tile" -> Color.GAINSBORO;
      default -> Color.BLUE;
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

