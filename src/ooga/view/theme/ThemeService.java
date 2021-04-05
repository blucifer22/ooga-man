package ooga.view.theme;

import javafx.scene.paint.Paint;

public interface ThemeService {
  Paint fillForObjectOfType(String type);
  void addThemeChangeRefreshable(ThemeChangeRefreshable refreshable);
}
