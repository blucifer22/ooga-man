package ooga.view.language.api;

import java.util.Map;
import java.util.Set;

public interface LanguageSelectionService {

  void setLanguage(String languageName);
  Map<String, String> getAvailableLanguages();
}
