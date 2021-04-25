package ooga.model.api;

import ooga.model.GameEvent;

/**
 * This interface allows implementing objects to respond to the activation and deactivation of
 * power-up effects.
 */
public interface GameEventObserver {

  void onGameEvent(GameEvent event);
}
