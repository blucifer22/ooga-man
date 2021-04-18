package ooga.view.language.api;

import java.util.Map;

public interface LanguageSelectionService {

  void setLanguage(String languageName);

  Map<String, String> getAvailableLanguages();
}
