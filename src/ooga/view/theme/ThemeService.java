package ooga.view.theme;

import javafx.scene.paint.Paint;

public interface ThemeService {
  Paint getFillForObjectOfType(String type);
  void addThemedObject(ThemedObject themedObject);
}
