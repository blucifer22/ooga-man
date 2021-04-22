package ooga.view.uiservice;

import ooga.view.audio.AudioService;
import ooga.view.internal_api.ViewStackManager;
import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;

public interface UIServiceProvider {
  AudioService audioService();
  ThemeService themeService();
  LanguageService languageService();
  ViewStackManager viewStackManager();
}
