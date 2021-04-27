package ooga.view.uiservice;

import ooga.view.language.api.LanguageSelectionService;
import ooga.view.theme.api.ThemeSelectionService;

/**
 * A concrete implementation of {@link UIPreferenceService}. Allows passing one consistent
 * set of UI preference selection services (the overwhelmingly most common use case) instead of
 * two separate servicing instances (one for each service interface) to dependent views.
 *
 * @author David Coffman
 */
public class PreferenceService implements UIPreferenceService {

  private final ThemeSelectionService themeSelectionService;
  private final LanguageSelectionService languageSelectionService;

  /**
   * Sole {@link PreferenceService} constructor.
   *
   * @param themeSelectionService a {@link ThemeSelectionService} for use by referencing views
   * @param languageSelectionService a {@link LanguageSelectionService} for use by refrerencing
   *                                 views
   * @throws NullPointerException if any of the input parameters are null (views expect to be able
   *                              to call provided services)
   */
  public PreferenceService(
      ThemeSelectionService themeSelectionService,
      LanguageSelectionService languageSelectionService) {
    this.themeSelectionService = themeSelectionService;
    this.languageSelectionService = languageSelectionService;

    if (languageSelectionService == null || themeSelectionService == null) {
      throw new NullPointerException();
    }
  }

  /**
   * Provides the referencing view with a {@link ThemeSelectionService}.
   *
   * @return the {@link UIServiceProvider}'s {@link ThemeSelectionService}.
   */
  @Override
  public ThemeSelectionService themeSelectionService() {
    return this.themeSelectionService;
  }

  /**
   * Provides the referencing view with a {@link LanguageSelectionService}.
   *
   * @return the {@link UIServiceProvider}'s {@link LanguageSelectionService}.
   */
  @Override
  public LanguageSelectionService languageSelectionService() {
    return this.languageSelectionService;
  }
}
