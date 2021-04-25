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
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.UIServicedException;
import ooga.view.language.api.LanguageSelectionService;
import ooga.view.language.api.LanguageService;

public class BundledLanguageService implements LanguageService, LanguageSelectionService {

  private static final String DEFAULT_LANGUAGE_ROOT = "resources.languages/";
  private static final String DEFAULT_LANGUAGE = "english";
  private final ExceptionService exceptionService;
  private final TreeMap<String, String> availableLanguages;
  private final HashMap<String, StringProperty> strings;
  private String languageName;

  public BundledLanguageService(ExceptionService exceptionService) {
    this.strings = new HashMap<>();
    this.availableLanguages = new TreeMap<>();
    this.loadDefaultLanguage();
    this.exceptionService = exceptionService;
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
    availableLanguages.clear();

    ResourceBundle languageManifest;
    try {
      languageManifest = ResourceBundle.getBundle(DEFAULT_LANGUAGE_ROOT + "manifest");
    } catch (MissingResourceException e) {
      exceptionService.handleFatalError(new UIServicedException("langManifestError"));
      return;
    }

    for (String key : languageManifest.keySet()) {
      availableLanguages.put(key, languageManifest.getString(key));
    }
  }

  private void loadDefaultLanguage() {
    updatePropertyValues(DEFAULT_LANGUAGE, true);
  }

  private void updatePropertyValues(String languageName, boolean initial) {
    ResourceBundle newLang;

    try {
      newLang = ResourceBundle.getBundle(DEFAULT_LANGUAGE_ROOT + "/" + languageName);
    } catch (MissingResourceException e) {
      exceptionService.handleWarning(new UIServicedException("missingLangError", languageName));
      return;
    }

    if (!initial) {
      HashSet<String> keysToUpdate = new HashSet<>(strings.keySet());
      keysToUpdate.removeAll(newLang.keySet());

      if (!keysToUpdate.isEmpty()) {
        exceptionService.handleWarning(new UIServicedException("missingValError", languageName,
            keysToUpdate.toString()));
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
    if (!strings.containsKey(s)) {
      strings.put(s, new SimpleStringProperty(""));
      exceptionService.handleWarning(new UIServicedException("missingValError", languageName, s));
    }
    return strings.get(s);
  }
}
