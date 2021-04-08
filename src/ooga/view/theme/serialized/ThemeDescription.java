package ooga.view.theme.serialized;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import ooga.model.leveldescription.JSONDescription;
import ooga.view.theme.api.Theme;

public class ThemeDescription extends JSONDescription {

  private final Map<String, CostumeDescription> costumes;

  public ThemeDescription() {
    this.costumes = new HashMap<>();
    costumes.put("pacman", new CostumeDescription("themes/classic/images/pacman.png",
        true, 1.0, true, true));
    costumes.put("ghost", new CostumeDescription("RED", false, 1.0, false, true));
    costumes.put("tile", new CostumeDescription("BLACK", false, 1.0, false, true));
  }

  public ThemeDescription(
      @JsonProperty("costumes") Map<String, CostumeDescription> costumes
  ) {
    this.costumes = costumes;
  }

  @JsonGetter("costumes")
  public Map<String, CostumeDescription> getCostumes() {
    return costumes;
  }

  public Theme toTheme() {
    return new SerializedTheme(this);
  }
}
