package ooga.model.powerups;

import java.util.Map;
import ooga.model.PacmanPowerupEvent;

import static ooga.model.PacmanPowerupEvent.*;

public class PointBonusPowerUp extends TimerBasedPowerUp {

  public PointBonusPowerUp() {
    super(Map.of(0.0, POINT_BONUS_ACTIVATED, DEFAULT_POWERUP_DURATION,
        POINT_BONUS_DEACTIVATED));
  }

}
