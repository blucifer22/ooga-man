package ooga.view.language.api;

import java.util.Map;

/**
 * Configurator-facing interface on a localization support class. Allows a referencing object to
 * set the current language, as well as view available languages and their localized names.
 *
 * @author David Coffman
 */
public interface LanguageSelectionService {

  /**
   * Sets a new language.
   *
   * @param languageName the language <em>bundle</em> name, stored as a key in the return from
   * {@link LanguageSelectionService#getAvailableLanguages()}
   */
  void setLanguage(String languageName);

  /**
   * Returns a map of available languages in the form {BUNDLE_IDENTIFIER: LOCALIZED_NAME} -- i.e.
   * {"english" -> "English", "spanish" -> "Español"}.
   *
   * @return a map of available language bundle names to their localized names
   */
  Map<String, String> getAvailableLanguages();
}
