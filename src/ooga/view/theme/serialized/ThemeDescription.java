package ooga.view.theme.serialized;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import ooga.model.leveldescription.JSONDescription;
import ooga.view.theme.api.Theme;

public class ThemeDescription extends JSONDescription {

  private final Map<String, CostumeDescription> costumes;
  private final String stylesheet;
  private final String name;

  public ThemeDescription(
      @JsonProperty("name") String name,
      @JsonProperty("costumes") Map<String, CostumeDescription> costumes,
      @JsonProperty("stylesheet") String stylesheet
  ) {
    this.name = name;
    this.costumes = costumes;
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

  @JsonGetter("stylesheet")
  public String getStylesheet() { return this.stylesheet; }

  public Theme toTheme() {
    return new SerializedTheme(this);
  }
}
