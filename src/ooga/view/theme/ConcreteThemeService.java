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
  public Paint fillForObjectOfType(String type) {
    return Color.BLUE;
  }

  @Override
  public void addThemeChangeRefreshable(ThemeChangeRefreshable refreshable) {
    this.observers.add(refreshable);
  }

  public void setTheme() {
    for(ThemeChangeRefreshable observer: observers) {
      observer.onThemeChange();
    }
  }
}

