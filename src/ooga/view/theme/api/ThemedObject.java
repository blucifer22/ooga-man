package ooga.view.theme.api;

/**
 * An observer interface responsible for notifying themed views when the theme changes, in order to
 * allow them to change their appearance.
 *
 * @author David Coffman
 */
public interface ThemedObject {

  /**
   * Observer callback. Called when the theme changes. Implementing classes should re-query the
   * {@link ThemeService} for a new {@link Theme} when this method is called.
   */
  void onThemeChange();
}
