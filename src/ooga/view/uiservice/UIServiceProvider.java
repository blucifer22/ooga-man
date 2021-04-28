package ooga.view.uiservice;

import ooga.view.audio.AudioService;
import ooga.view.exceptions.ExceptionService;
import ooga.view.internal_api.ViewStackService;
import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;

/**
 * A standardized container for UI services. Allows passing one consistent set of UI services (the
 * overwhelmingly most common use case) instead of five separate servicing instances (one for each
 * service interface) to dependent views.
 *
 * @author David Coffman
 */
public interface UIServiceProvider {

  /**
   * Provides the referencing view with an {@link AudioService}.
   *
   * @return the {@link UIServiceProvider}'s {@link AudioService}.
   */
  AudioService audioService();

  /**
   * Provides the referencing view with an {@link ExceptionService}.
   *
   * @return the {@link UIServiceProvider}'s {@link ExceptionService}.
   */
  ExceptionService exceptionService();

  /**
   * Provides the referencing view with a {@link ThemeService}.
   *
   * @return the {@link UIServiceProvider}'s {@link ThemeService}.
   */
  ThemeService themeService();

  /**
   * Provides the referencing view with a {@link LanguageService}.
   *
   * @return the {@link UIServiceProvider}'s {@link LanguageService}.
   */
  LanguageService languageService();

  /**
   * Provides the referencing view with a {@link ViewStackService}.
   *
   * @return the {@link UIServiceProvider}'s {@link ViewStackService}.
   */
  ViewStackService viewStackManager();
}
