package ooga.view.uiservice;

import ooga.view.language.api.LanguageSelectionService;
import ooga.view.theme.api.ThemeSelectionService;

/**
 * A standardized container for UI preference selection services. Allows passing one consistent set
 * of UI preference selection services (the overwhelmingly most common use case) instead of two
 * separate servicing instances (one for each service interface) to dependent views.
 *
 * @author David Coffman
 */
public interface UIPreferenceService {

  /**
   * Provides the referencing view with a {@link LanguageSelectionService}.
   *
   * @return the {@link UIServiceProvider}'s {@link LanguageSelectionService}.
   */
  LanguageSelectionService languageSelectionService();

  /**
   * Provides the referencing view with a {@link ThemeSelectionService}.
   *
   * @return the {@link UIServiceProvider}'s {@link ThemeSelectionService}.
   */
  ThemeSelectionService themeSelectionService();
}
