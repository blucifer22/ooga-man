package ooga.view.internal_api;

import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;

public interface UIServiceProvider {
  ThemeService themeService();
  LanguageService languageService();
  ViewStackManager viewStackManager();
}
