package ooga.view.internal_api;

/**
 * Primary interface designed to abstract out the primary dependency for a main menu view (need
 * not be a specific menu view, though this is implemented concretely in
 * {@link ooga.view.views.sceneroots.MenuView}) --- a class that must start the game, open the
 * level builder, and open the preferences view in response to user interaction.
 */
public interface MainMenuResponder {

  /**
   * Menu view callback for a "start game" user interaction.
   */
  void startGame();

  /**
   * Menu view callback for an "open level builder" user interaction.
   */
  void openLevelBuilder();

  /**
   * Menu view callback for an "open preferences" user interaction.
   */
  void openPreferences();
}
