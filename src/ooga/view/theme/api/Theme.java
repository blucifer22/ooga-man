package ooga.view.theme.api;

public interface Theme {
  Costume getCostumeForObjectOfType(String type);
  String getStylesheet();
  String getName();
}
