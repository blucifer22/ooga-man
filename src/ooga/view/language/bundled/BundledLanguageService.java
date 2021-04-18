package ooga.view.language.bundled;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ooga.view.language.api.LanguageSelectionService;
import ooga.view.language.api.LanguageService;

public class BundledLanguageService implements LanguageService, LanguageSelectionService {

  private static final String DEFAULT_LANGUAGE_ROOT = "resources.languages/";
  private static final String DEFAULT_LANGUAGE = "english";

  private final TreeMap<String, String> availableLanguages;
  private final HashMap<String, StringProperty> strings;
  private String languageName;

  public BundledLanguageService() {
    this.strings = new HashMap<>();
    this.availableLanguages = new TreeMap<>();
    this.loadDefaultLanguage();
  }

  @Override
  public void setLanguage(String languageName) {
    updatePropertyValues(languageName, false);
  }

  @Override
  public Map<String, String> getAvailableLanguages() {
    updateAvailableLanguages();
    return Collections.unmodifiableMap(availableLanguages);
  }

  private void updateAvailableLanguages() {
    ResourceBundle languageManifest = ResourceBundle.getBundle(DEFAULT_LANGUAGE_ROOT + "manifest");
    availableLanguages.clear();

    for (String key : languageManifest.keySet()) {
      availableLanguages.put(key, languageManifest.getString(key));
    }
  }

  private void loadDefaultLanguage() {
    updatePropertyValues(DEFAULT_LANGUAGE, true);
  }

  private void updatePropertyValues(String languageName, boolean initial) {
    ResourceBundle newLang = ResourceBundle.getBundle(DEFAULT_LANGUAGE_ROOT + "/" + languageName);

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

    for (String key : newLang.keySet()) {
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
