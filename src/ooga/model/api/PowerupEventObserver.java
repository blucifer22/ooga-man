package ooga.model.api;

import ooga.model.PacmanPowerupEvent;

/**
 * This interface allows implementing objects to respond to the activation and deactivation of
 * power-up effects.
 */
public interface PowerupEventObserver {

  void respondToPowerEvent(PacmanPowerupEvent event);
}