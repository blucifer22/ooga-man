package ooga.view.theme.serialized;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.UIServicedException;
import ooga.view.theme.api.Costume;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;

/**
 * Concrete implementation of {@link Theme}. A container representing various styled aspects of a
 * view. A mini-service that allows for (usually indirectly, through a {@link
 * ooga.view.uiservice.UIServiceProvider}'s {@link ThemeService}) referencing view to query for
 * {@link Media}, {@link Costume}s, and stylesheets.
 *
 * @author David Coffman
 */
public class SerializedTheme implements Theme {

  private final Map<String, Costume> costumes;
  private final Map<String, Media> sounds;
  private final String stylesheet;
  private final String name;
  private final ExceptionService exceptionService;

  /**
   * Constructor for a {@link SerializedTheme}. Requires an {@link ExceptionService} to service
   * file-not-found errors.
   *
   * @param description      the {@link ThemeDescription} data source
   * @param exceptionService the {@link ExceptionService} to which errors should be directed
   */
  protected SerializedTheme(ThemeDescription description, ExceptionService exceptionService) {
    this.stylesheet = description.getStylesheet();
    this.exceptionService = exceptionService;
    this.costumes = new HashMap<>();
    this.sounds = new HashMap<>();
    this.name = description.getName();

    for (String key : description.getCostumes().keySet()) {
      costumes.put(key, description.getCostumes().get(key).toCostume());
    }

    for (String key : description.getAudioFilePaths().keySet()) {
      String encoded = null;
      try {
        /*
         * getAbsoluteFile().getAbsolutePath() doesn't actually return the absolute path!
         * It (for whatever internal buggy Java reason) omits the /data from the filepath!
         * The Media class also ~requires~ an absolute filepath (why, Java?!)
         */

        encoded =
            (new File(description.getAudioFilePaths().get(key)).toURI().toString())
                .replace("/themes/", "/data/themes/");

        sounds.put(key, new Media(encoded));
      } catch (Exception e) {
        exceptionService.handleWarning(new UIServicedException("badAudioFileError", name, encoded));
      }
    }
  }

  /**
   * Returns a themed piece of audio.
   *
   * @param identifier the identifier for the requested audio
   * @return the audio, wrapped by a {@link Media} instance
   */
  @Override
  public Media getSoundByIdentifier(String identifier) {
    if (sounds.get(identifier) == null) {
      exceptionService.handleWarning(
          new UIServicedException("missingAudioFileError", name, identifier));
      return null;
    } else {
      return sounds.get(identifier);
    }
  }

  /**
   * Returns the {@link Costume} associated with an object type.
   *
   * @param type the object's {@link String} type
   * @return the {@link Costume} associated with the parameter object type
   */
  @Override
  public Costume getCostumeForObjectOfType(String type) {
    Costume ret = costumes.get(type);

    if (ret == null) {
      exceptionService.handleWarning(new UIServicedException("missingCostumeError", name, type));
      return new Costume() {
        @Override
        public Paint getFill() {
          return Color.ORANGE;
        }

        @Override
        public double getScale() {
          return 0.5;
        }

        @Override
        public boolean isBottomHeavy() {
          return true;
        }

        @Override
        public boolean isRotatable() {
          return true;
        }
      };
    } else {
      return ret;
    }
  }

  /**
   * Returns the location of this theme's stylesheet.
   *
   * @return the location of this theme's stylesheet
   */
  @Override
  public String getStylesheet() {
    return this.stylesheet;
  }

  /**
   * Returns this theme's name.
   *
   * @return this theme's name
   */
  @Override
  public String getName() {
    return this.name;
  }
}
