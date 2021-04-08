package ooga.view.theme.api;

public interface ThemeService {

  Costume getCostumeForObjectOfType(String type);

  void addThemedObject(ThemedObject themedObject);
}
