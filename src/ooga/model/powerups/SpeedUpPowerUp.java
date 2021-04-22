package ooga.model.powerups;

import java.util.Map;
import ooga.model.PacmanPowerupEvent;

import static ooga.model.PacmanPowerupEvent.*;

public class SpeedUpPowerUp extends TimerBasedPowerUp {

  public SpeedUpPowerUp() {
    super(Map.of(0.0, SPEED_UP_ACTIVATED, DEFAULT_POWERUP_DURATION, SPEED_UP_DEACTIVATED));
  }
}
