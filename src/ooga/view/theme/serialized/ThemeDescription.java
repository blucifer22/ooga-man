package ooga.view.theme.serialized;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import ooga.model.leveldescription.JSONDescription;
import ooga.view.exceptions.ExceptionService;
import ooga.view.theme.api.Costume;
import ooga.view.theme.api.Theme;

/**
 * Serialization description implementation for a {@link Theme}, as part of the
 * serialization/deserialization scheme in {@link ooga.view.theme.serialized}. Equates all
 * {@link Theme} parameters to primitives and {@link CostumeDescription}s.
 *
 * @author David Coffman
 */
public class ThemeDescription extends JSONDescription {

  private final Map<String, CostumeDescription> costumes;
  private final Map<String, String> audioFilePaths;
  private final String stylesheet;
  private final String name;

  /**
   * Sole constructor. Called only during deserialization.
   *
   * @param name the name of the represented {@link Theme}
   * @param audioFilePaths a {@link Map} mapping audio asset names to their file paths
   * @param costumes a {@link Map} mapping costume names to their serialized descriptions
   * @param stylesheet the file path of the represented {@link Theme}'s stylesheet
   */
  public ThemeDescription(
      @JsonProperty("name") String name,
      @JsonProperty("sounds") Map<String, String> audioFilePaths,
      @JsonProperty("costumes") Map<String, CostumeDescription> costumes,
      @JsonProperty("stylesheet") String stylesheet) {
    this.name = name;
    this.audioFilePaths = (audioFilePaths == null) ? new HashMap<>() : audioFilePaths;
    this.costumes = (costumes == null) ? new HashMap<>() : costumes;
    this.stylesheet = stylesheet;
  }

  /**
   * Returns the name of the represented {@link Theme}.
   *
   * @return the name of the represented {@link Theme}.
   */
  @JsonGetter("name")
  public String getName() {
    return this.name;
  }

  /**
   * Returns the {@link Costume}s of the represented {@link Theme}.
   *
   * @return the {@link Costume}s of the represented {@link Theme}.
   */
  @JsonGetter("costumes")
  public Map<String, CostumeDescription> getCostumes() {
    return costumes;
  }

  /**
   * Returns the audio file paths of the represented {@link Theme}.
   *
   * @return the audio file paths of the represented {@link Theme}.
   */
  @JsonGetter("sounds")
  public Map<String, String> getAudioFilePaths() {
    return audioFilePaths;
  }

  /**
   * Returns the path to the stylesheet of the represented {@link Theme}.
   *
   * @return the path to the stylesheet of the represented {@link Theme}.
   */
  @JsonGetter("stylesheet")
  public String getStylesheet() {
    return this.stylesheet;
  }

  /**
   * Converts the {@link ThemeDescription} to a concrete implementation of {@link Theme}
   * represented by this {@link ThemeDescription}.
   *
   * @param exceptionService the {@link ExceptionService} to which exceptions should be directed
   * @return a concrete implementation of {@link Theme} represented by this {@link ThemeDescription}
   */
  public Theme toTheme(ExceptionService exceptionService) {
    return new SerializedTheme(this, exceptionService);
  }
}
