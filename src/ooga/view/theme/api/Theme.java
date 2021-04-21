package ooga.view.theme.api;

import javafx.scene.media.Media;

public interface Theme {

  Media getSoundFromIdentifier(String identifier);

  Costume getCostumeForObjectOfType(String type);

  String getStylesheet();

  String getName();
}
