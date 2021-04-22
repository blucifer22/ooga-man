package ooga.view.uiservice;

import ooga.view.audio.AudioService;
import ooga.view.internal_api.ViewStackManager;
import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;

public class ServiceProvider implements UIServiceProvider {

  private final ThemeService themeService;
  private final LanguageService languageService;
  private final ViewStackManager viewStackManager;
  private final AudioService audioService;

  public ServiceProvider(AudioService audioService, ThemeService themeService,
      LanguageService languageService, ViewStackManager viewStackManager) {
    this.audioService = audioService;
    this.themeService = themeService;
    this.languageService = languageService;
    this.viewStackManager = viewStackManager;

    if (themeService == null || languageService == null || viewStackManager == null) {
      throw new NullPointerException();
    }
  }

  @Override
  public AudioService audioService() {
    return this.audioService;
  }

  @Override
  public ThemeService themeService() {
    return this.themeService;
  }

  @Override
  public LanguageService languageService() {
    return this.languageService;
  }

  @Override
  public ViewStackManager viewStackManager() {
    return this.viewStackManager;
  }
}
