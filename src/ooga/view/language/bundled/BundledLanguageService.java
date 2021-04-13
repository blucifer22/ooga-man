package ooga.view.language.bundled;

import java.util.HashMap;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ooga.view.language.api.LanguageService;

public class BundledLanguageService implements LanguageService {

  private static final String DEFAULT_LANGUAGE_ROOT = "resources.languages";
  private static final String DEFAULT_LANGUAGE = "spanish";

  private final String languageRoot;
  private String languageName;
  private final HashMap<String, StringProperty> strings;

  public BundledLanguageService() {
    this.strings = new HashMap<>();
    this.languageRoot = DEFAULT_LANGUAGE_ROOT;
    this.loadDefaultLanguage();
  }

  public BundledLanguageService(String languageRoot) {
    this.strings = new HashMap<>();
    this.languageRoot = languageRoot;
    this.loadDefaultLanguage();
  }

  private void loadDefaultLanguage() {
    updatePropertyValues(DEFAULT_LANGUAGE, true);
  }

  public void setLanguage(String languageName) {
    updatePropertyValues(languageName, false);
  }

  private void updatePropertyValues(String languageName, boolean initial) {
    ResourceBundle newLang = ResourceBundle.getBundle(languageRoot +"/"+languageName);

    if (!initial) {
      HashSet<String> keysToUpdate = new HashSet<>(strings.keySet());
      keysToUpdate.removeAll(newLang.keySet());

      if (!keysToUpdate.isEmpty()) {
        try {
          throw new IllegalArgumentException(String.format(newLang.getString("missingvalerror"),
              languageName, keysToUpdate));
        } catch (MissingResourceException e) {
          // THIS VALUE MUST BE HARD-CODED: the EXCEPTION here is that resource bundle error
          // messages are are missing
          throw new IllegalArgumentException(String.format("Error while handling error: corrupted "
              + "or incomplete resource bundle: %s.", languageName));
        }
      }
    }

    for (String key: newLang.keySet()) {
      strings.putIfAbsent(key, new SimpleStringProperty());
      strings.get(key).setValue(newLang.getString(key));
    }

    this.languageName = languageName;
  }

  @Override
  public ReadOnlyStringProperty getLocalizedString(String s) {
    try {
      if (!strings.containsKey(s)) {
        throw new IllegalArgumentException(String.format(strings.get("missingvalerror").getValue(),
            languageName, s));
      }
    } catch (MissingResourceException m) {
      // THIS VALUE MUST BE HARD-CODED: the EXCEPTION here is that resource bundle error
      // messages are are missing
      throw new IllegalArgumentException(String.format("Error while handling error: corrupted "
          + "or incomplete resource bundle: %s.", languageName));
    }

    return strings.get(s);
  }
}
