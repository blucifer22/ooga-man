package ooga.view.uiservice;

import ooga.view.language.api.LanguageSelectionService;
import ooga.view.theme.api.ThemeSelectionService;

public interface UIPreferenceService {

  LanguageSelectionService languageSelectionService();

  ThemeSelectionService themeSelectionService();
}
