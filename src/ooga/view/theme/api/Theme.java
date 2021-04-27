package ooga.view.theme.api;

import javafx.scene.media.Media;

/**
 * A container representing various styled aspects of a view. A mini-service that allows for
 * (usually indirectly, through a {@link ooga.view.uiservice.UIServiceProvider}'s {@link ThemeService})
 * referencing view to query for {@link Media}, {@link Costume}s, and stylesheets.
 *
 * @author David Coffman
 */
public interface Theme {

  /**
   * Returns a themed piece of audio.
   *
   * @param identifier the identifier for the requested audio
   * @return the audio, wrapped by a {@link Media} instance
   */
  Media getSoundByIdentifier(String identifier);

  /**
   * Returns the {@link Costume} associated with an object type.
   *
   * @param type the object's {@link String} type
   * @return the {@link Costume} associated with the parameter object type
   */
  Costume getCostumeForObjectOfType(String type);

  /**
   * Returns the location of this theme's stylesheet.
   *
   * @return the location of this theme's stylesheet
   */
  String getStylesheet();

  /**
   * Returns this theme's name.
   *
   * @return this theme's name
   */
  String getName();
}
