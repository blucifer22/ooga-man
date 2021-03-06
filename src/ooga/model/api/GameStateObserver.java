package ooga.model.api;

/**
 * Observer interface exposed by game states.
 *
 * @author George Hong
 */
public interface GameStateObserver {

  /**
   * Called when a game state change occurs, such as when the score or pacman lives remaining
   * changes
   *
   * @param state contains information about the Pac-Man game state
   */
  void onGameStateUpdate(GameStateObservable state);
}
