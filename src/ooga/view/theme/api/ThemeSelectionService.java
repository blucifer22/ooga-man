package ooga.view.theme.api;

import java.util.Set;

/**
 * Configurator-facing interface on a theme management class. Allows a referencing object to set the
 * current theme, as well as view available themes.
 *
 * @author David Coffman
 */
public interface ThemeSelectionService {

  /**
   * Sets a new theme.
   *
   * @param name the name of the {@link Theme} to enable
   */
  void setTheme(String name);

  /**
   * Returns a {@link Set<String>} containing the names of all available {@link Theme}s.
   *
   * @return a {@link Set<String>} containing the names of all available {@link Theme}s.
   */
  Set<String> getAvailableThemes();
}
