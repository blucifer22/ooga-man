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

/**
 * Implementation of {@link LanguageService} and {@link LanguageSelectionService}. Uses resource
 * bundles referenced by the application language manifest and language files from the
 * application language resources folder. See {@link LanguageService} and {@link LanguageService}
 * for usage details.
 *
 * @authod David Coffman
 */
public class BundledLanguageService implements LanguageService, LanguageSelectionService {

  private static final String DEFAULT_LANGUAGE_ROOT = "resources.languages/";
  private static final String DEFAULT_LANGUAGE = "english";
  private final ExceptionService exceptionService;
  private final TreeMap<String, String> availableLanguages;
  private final HashMap<String, StringProperty> strings;
  private String languageName;

  /**
   * Sole constructor for {@link BundledLanguageService}.
   *
   * @param exceptionService the {@link ExceptionService} to which errors should be directed
   */
  public BundledLanguageService(ExceptionService exceptionService) {
    this.strings = new HashMap<>();
    this.availableLanguages = new TreeMap<>();
    this.loadDefaultLanguage();
    this.exceptionService = exceptionService;
  }

  /**
   * Sets a new language.
   *
   * @param languageName the language <em>bundle</em> name, stored as a key in the return from
   * {@link LanguageSelectionService#getAvailableLanguages()}
   */
  @Override
  public void setLanguage(String languageName) {
    updatePropertyValues(languageName, false);
  }

  /**
   * Returns a map of available languages in the form {BUNDLE_IDENTIFIER: LOCALIZED_NAME} -- i.e.
   * {"english" -> "English", "spanish" -> "Español"}.
   *
   * @return a map of available language bundle names to their localized names
   */
  @Override
  public Map<String, String> getAvailableLanguages() {
    updateAvailableLanguages();
    return Collections.unmodifiableMap(availableLanguages);
  }

  // Refreshes the list of available languages by searching the language manifest.
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

  // Loads the default language.
  private void loadDefaultLanguage() {
    updatePropertyValues(DEFAULT_LANGUAGE, true);
  }

  // Updates each StringProperty when the language is updated.
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
        exceptionService.handleWarning(
            new UIServicedException("missingValError", languageName, keysToUpdate.toString()));
      }
    }

    for (String key : newLang.keySet()) {
      strings.putIfAbsent(key, new SimpleStringProperty());
      strings.get(key).setValue(newLang.getString(key));
    }

    this.languageName = languageName;
  }

  /**
   * Returns a {@link ReadOnlyStringProperty} whose value is bound to the current language's
   * translation for the key parameter. The {@link ReadOnlyStringProperty} is updated whenever
   * the application language changes.s
   *
   * @param s the key to search for
   * @return a {@link ReadOnlyStringProperty} whose value is bound to the current language's
   * translation for the key parameter, or a {@link ReadOnlyStringProperty} if no such key exists.
   */
  @Override
  public ReadOnlyStringProperty getLocalizedString(String s) {
    if (!strings.containsKey(s)) {
      strings.put(s, new SimpleStringProperty(""));
      exceptionService.handleWarning(new UIServicedException("missingValError", languageName, s));
    }
    return strings.get(s);
  }
}
