package ooga.view.theme.serialized;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import ooga.model.leveldescription.JSONDescription;
import ooga.view.exceptions.ExceptionService;
import ooga.view.theme.api.Theme;

public class ThemeDescription extends JSONDescription {

  private final Map<String, CostumeDescription> costumes;
  private final Map<String, String> audioFilePaths;
  private final String stylesheet;
  private final String name;

  public ThemeDescription(
      @JsonProperty("name") String name,
      @JsonProperty("sounds") Map<String, String> audioFilePaths,
      @JsonProperty("costumes") Map<String, CostumeDescription> costumes,
      @JsonProperty("stylesheet") String stylesheet
  ) {
    this.name = name;
    this.audioFilePaths = (audioFilePaths == null) ? new HashMap<>() : audioFilePaths;
    this.costumes = (costumes == null) ? new HashMap<>() : costumes;
    this.stylesheet = stylesheet;
  }

  @JsonGetter("name")
  public String getName() {
    return this.name;
  }

  @JsonGetter("costumes")
  public Map<String, CostumeDescription> getCostumes() {
    return costumes;
  }

  @JsonGetter("sounds")
  public Map<String, String> getAudioFilePaths() {
    return audioFilePaths;
  }

  @JsonGetter("stylesheet")
  public String getStylesheet() {
    return this.stylesheet;
  }

  public Theme toTheme(ExceptionService exceptionService) {
    return new SerializedTheme(this, exceptionService);
  }
}
