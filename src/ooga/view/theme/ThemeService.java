package ooga.view.theme;

import javafx.scene.paint.Paint;

public interface ThemeService {

  Costume getCostumeForObjectOfType(String type);

  void addThemedObject(ThemedObject themedObject);
}
