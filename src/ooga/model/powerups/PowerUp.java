package ooga.model.powerups;

import ooga.model.MutableGameState;

/**
 * Interface implemented by a power-up.
 *
 * @author Franklin Wei
 */
public interface PowerUp {

  /**
   * Execute this powerup.
   *
   * @param state State on which to act.
   */
  void executePowerUp(MutableGameState state);
}
