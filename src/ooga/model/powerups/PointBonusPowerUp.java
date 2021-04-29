package ooga.model.powerups;

import static ooga.model.GameEvent.POINT_BONUS_ACTIVATED;
import static ooga.model.GameEvent.POINT_BONUS_DEACTIVATED;

import java.util.Map;

/**
 * Powerup that gives a point bonus for a fixed duration.
 *
 * @author George Hong
 */
public class PointBonusPowerUp extends TimerBasedPowerUp {

  /**
   * Construct a point bonus powerup.
   */
  public PointBonusPowerUp() {
    super(Map.of(0.0, POINT_BONUS_ACTIVATED, DEFAULT_POWERUP_DURATION, POINT_BONUS_DEACTIVATED));
  }
}
