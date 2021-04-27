package ooga.view.theme.api;

/**
 * The view-facing interface of a theme management class. Allows for {@link ThemedObject}s to
 * register for theme-change updates, as well as to query for the current {@link Theme}
 * (typically in response to updates received following the former).
 *
 * @author David Coffman
 */
public interface ThemeService {

  /**
   * Returns the active {@link Theme}.
   *
   * @return the active {@link Theme}.
   */
  Theme getTheme();

  /**
   * Registers a {@link ThemedObject} for theme change updates.
   *
   * @param themedObject the {@link ThemedObject} to register for theme change updates
   */
  void addThemedObject(ThemedObject themedObject);
}
