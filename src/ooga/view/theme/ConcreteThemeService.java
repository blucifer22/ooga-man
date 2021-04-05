package ooga.view.theme;

import java.util.HashSet;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ConcreteThemeService implements ThemeService {

  private final HashSet<ThemeChangeRefreshable> observers;

  public ConcreteThemeService() {
    observers = new HashSet<>();
  }

  @Override
  public Paint getFillForObjectOfType(String type) {
    // TODO: use Marc's JSON parsing to determine how themes should work
    // TODO: implement asset loading
    return Color.BLUE;
  }

  @Override
  public void addThemeChangeRefreshable(ThemeChangeRefreshable refreshable) {
    this.observers.add(refreshable);
  }

  public void setTheme() {
    // TODO: change state to reflect new theme
    for(ThemeChangeRefreshable observer: observers) {
      observer.onThemeChange();
    }
  }
}

