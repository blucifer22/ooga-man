package ooga.view.language.bundled;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ooga.view.language.LanguageService;

public class BundledLanguageService implements LanguageService {

  private static final String LANGUAGE_ROOT = "resources.languages";
  HashMap<String, StringProperty> strings;

  public BundledLanguageService() {
    this.strings = new HashMap<>();
    this.loadDefaultLanguage();
  }

  private void loadDefaultLanguage() {
    updatePropertyValues("english", true);
  }

  public void setLanguage(String languageName) {
    updatePropertyValues(languageName, false);
  }

  private void updatePropertyValues(String languageName, boolean initial) {
    ResourceBundle newLang = ResourceBundle.getBundle(LANGUAGE_ROOT+"/"+languageName);

    for (String key: newLang.keySet()) {
      strings.putIfAbsent(key, new SimpleStringProperty());
      strings.get(key).setValue(newLang.getString(key));
    }

    if (!initial) {
      HashSet<String> keysToUpdate = new HashSet<>(strings.keySet());
      keysToUpdate.removeAll(newLang.keySet());

      if (!keysToUpdate.isEmpty()) {
        throw new IllegalArgumentException(String.format(newLang.getString("langvalerror"),
            languageName, keysToUpdate));
      }
    }
  }



  @Override
  public ReadOnlyStringProperty getLocalizedString(String s) {
    return strings.get(s);
  }
}
