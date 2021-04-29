package ooga.model.api;

import ooga.model.GameEvent;

/**
 * This interface allows implementing objects to respond to the activation and deactivation of
 * power-up effects, and other game-wide events, such as the initial sprite unfreeze, and
 * pacman's death.
 */
public interface GameEventObserver {

  /**
   * Called on a game event.
   * @param event Event sent.
   */
  void onGameEvent(GameEvent event);
}
