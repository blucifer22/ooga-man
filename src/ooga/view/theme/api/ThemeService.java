package ooga.view.theme.api;

public interface ThemeService {

  Costume getCostumeForObjectOfType(String type);

  Theme getTheme();

  void addThemedObject(ThemedObject themedObject);
}
