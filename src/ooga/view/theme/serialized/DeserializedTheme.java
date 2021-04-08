package ooga.view.theme.serialized;

import java.util.HashMap;
import java.util.Map;
import ooga.view.theme.api.Costume;
import ooga.view.theme.api.Theme;

public class DeserializedTheme implements Theme {
  private Map<String, Costume> costumes;

  protected DeserializedTheme(ThemeDescription description) {
    this.costumes = new HashMap<>();

    for (String key: description.getCostumes().keySet()) {
      costumes.put(key, description.getCostumes().get(key).toCostume());
    }
  }

  @Override
  public Costume getCostumeForObjectOfType(String type) {
    return costumes.get(type);
  }
}
