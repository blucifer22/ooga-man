package ooga.model.powerups;

import java.util.Map;

import static ooga.model.GameEvent.*;

/**
 * @author George Hong
 */
public class PointBonusPowerUp extends TimerBasedPowerUp {

  public PointBonusPowerUp() {
    super(Map.of(0.0, POINT_BONUS_ACTIVATED, DEFAULT_POWERUP_DURATION,
        POINT_BONUS_DEACTIVATED));
  }

}
