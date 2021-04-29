package ooga.model.powerups;

import static ooga.model.GameEvent.SPEED_UP_ACTIVATED;
import static ooga.model.GameEvent.SPEED_UP_DEACTIVATED;

import java.util.Map;

/**
 * Powerup that speeds up pacman for the default Power-Up duration.
 *
 * @author George Hong.
 */
public class SpeedUpPowerUp extends TimerBasedPowerUp {

  /**
   * Construct a speed-up powerup for Pac-Man.
   */
  public SpeedUpPowerUp() {
    super(Map.of(0.0, SPEED_UP_ACTIVATED, DEFAULT_POWERUP_DURATION, SPEED_UP_DEACTIVATED));
  }
}
