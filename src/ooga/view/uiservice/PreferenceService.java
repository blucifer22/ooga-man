package ooga.view.uiservice;

import ooga.view.language.api.LanguageSelectionService;
import ooga.view.theme.api.ThemeSelectionService;

public class PreferenceService implements UIPreferenceService {

  private final LanguageSelectionService languageSelectionService;
  private final ThemeSelectionService themeSelectionService;

  public PreferenceService(LanguageSelectionService languageSelectionService,
      ThemeSelectionService themeSelectionService) {
    this.languageSelectionService = languageSelectionService;
    this.themeSelectionService = themeSelectionService;

    if (languageSelectionService == null || themeSelectionService == null) {
      throw new NullPointerException();
    }
  }
  @Override
  public LanguageSelectionService languageSelectionService() {
    return this.languageSelectionService;
  }

  @Override
  public ThemeSelectionService themeSelectionService() {
    return this.themeSelectionService;
  }
}
