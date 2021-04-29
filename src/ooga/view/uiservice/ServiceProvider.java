package ooga.view.uiservice;

import ooga.view.audio.AudioService;
import ooga.view.exceptions.ExceptionService;
import ooga.view.internal_api.ViewStackService;
import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;

/**
 * A simple implementation of {@link UIServiceProvider}. Allows passing one consistent set of UI
 * services (the overwhelmingly most common use case) instead of five separate servicing instances
 * (one for each service interface) to dependent views. Views may then reference the {@link
 * ServiceProvider} as a source of UI services.
 *
 * @author David Coffman
 */
public class ServiceProvider implements UIServiceProvider {

  private final ExceptionService exceptionService;
  private final ThemeService themeService;
  private final LanguageService languageService;
  private final ViewStackService viewStackService;
  private final AudioService audioService;

  /**
   * Sole {@link ServiceProvider} constructor.
   *
   * @param exceptionService an {@link ExceptionService} for use by referencing views
   * @param audioService     an {@link AudioService} for use by referencing views
   * @param themeService     a {@link ThemeService} for use by referencing views
   * @param languageService  a {@link LanguageService} for use by referencing views
   * @param viewStackService an {@link ViewStackService} for use by referencing views
   * @throws NullPointerException if any of the input parameters are null (views expect to be able
   *                              to call provided services)
   */
  public ServiceProvider(
      ExceptionService exceptionService,
      AudioService audioService,
      ThemeService themeService,
      LanguageService languageService,
      ViewStackService viewStackService) {
    this.exceptionService = exceptionService;
    this.audioService = audioService;
    this.themeService = themeService;
    this.languageService = languageService;
    this.viewStackService = viewStackService;

    if (exceptionService == null
        || audioService == null
        || themeService == null
        || languageService == null
        || viewStackService == null) {
      throw new NullPointerException();
    }
  }

  /**
   * Provides the referencing view with an {@link AudioService}.
   *
   * @return the {@link UIServiceProvider}'s {@link AudioService}.
   */
  @Override
  public AudioService audioService() {
    return this.audioService;
  }

  /**
   * Provides the referencing view with an {@link ExceptionService}.
   *
   * @return the {@link UIServiceProvider}'s {@link ExceptionService}.
   */
  @Override
  public ExceptionService exceptionService() {
    return this.exceptionService;
  }

  /**
   * Provides the referencing view with a {@link ThemeService}.
   *
   * @return the {@link UIServiceProvider}'s {@link ThemeService}.
   */
  @Override
  public ThemeService themeService() {
    return this.themeService;
  }

  /**
   * Provides the referencing view with a {@link LanguageService}.
   *
   * @return the {@link UIServiceProvider}'s {@link LanguageService}.
   */
  @Override
  public LanguageService languageService() {
    return this.languageService;
  }

  /**
   * Provides the referencing view with a {@link ViewStackService}.
   *
   * @return the {@link UIServiceProvider}'s {@link ViewStackService}.
   */
  @Override
  public ViewStackService viewStackManager() {
    return this.viewStackService;
  }
}
