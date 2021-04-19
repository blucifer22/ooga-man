package ooga.view.uiservice;

import ooga.view.language.api.LanguageSelectionService;
import ooga.view.theme.api.ThemeSelectionService;

public class PreferenceService implements UIPreferenceService {

  private final ThemeSelectionService themeSelectionService;
  private final LanguageSelectionService languageSelectionService;

  public PreferenceService(ThemeSelectionService themeSelectionService,
      LanguageSelectionService languageSelectionService) {
    this.themeSelectionService = themeSelectionService;
    this.languageSelectionService = languageSelectionService;

    if (languageSelectionService == null || themeSelectionService == null) {
      throw new NullPointerException();
    }
  }

  @Override
  public ThemeSelectionService themeSelectionService() {
    return this.themeSelectionService;
  }

  @Override
  public LanguageSelectionService languageSelectionService() {
    return this.languageSelectionService;
  }
}
