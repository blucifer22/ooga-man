package ooga.view.uiservice;

import ooga.view.audio.AudioService;
import ooga.view.exceptions.ExceptionService;
import ooga.view.internal_api.ViewStackService;
import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;

public interface UIServiceProvider {

  AudioService audioService();

  ExceptionService exceptionService();

  ThemeService themeService();

  LanguageService languageService();

  ViewStackService viewStackManager();
}
