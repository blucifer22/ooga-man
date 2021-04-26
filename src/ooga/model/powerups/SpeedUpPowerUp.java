package ooga.model.powerups;

import static ooga.model.GameEvent.SPEED_UP_ACTIVATED;
import static ooga.model.GameEvent.SPEED_UP_DEACTIVATED;

import java.util.Map;

public class SpeedUpPowerUp extends TimerBasedPowerUp {

  public SpeedUpPowerUp() {
    super(Map.of(0.0, SPEED_UP_ACTIVATED, DEFAULT_POWERUP_DURATION, SPEED_UP_DEACTIVATED));
  }
}
