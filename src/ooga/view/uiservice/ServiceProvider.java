package ooga.view.uiservice;

import ooga.view.audio.AudioService;
import ooga.view.exceptions.ExceptionService;
import ooga.view.internal_api.ViewStackService;
import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;

public class ServiceProvider implements UIServiceProvider {

  private final ExceptionService exceptionService;
  private final ThemeService themeService;
  private final LanguageService languageService;
  private final ViewStackService viewStackService;
  private final AudioService audioService;

  public ServiceProvider(ExceptionService exceptionService,
      AudioService audioService, ThemeService themeService,
      LanguageService languageService, ViewStackService viewStackService) {
    this.exceptionService = exceptionService;
    this.audioService = audioService;
    this.themeService = themeService;
    this.languageService = languageService;
    this.viewStackService = viewStackService;

    if (themeService == null || languageService == null || viewStackService == null) {
      throw new NullPointerException();
    }
  }

  @Override
  public AudioService audioService() {
    return this.audioService;
  }

  @Override
  public ExceptionService exceptionService() {
    return this.exceptionService;
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
  public ViewStackService viewStackManager() {
    return this.viewStackService;
  }
}
