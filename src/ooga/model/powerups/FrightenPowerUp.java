package ooga.model.powerups;

import java.util.Map;

import static ooga.model.GameEvent.*;

class FrightenPowerUp extends TimerBasedPowerUp {

  public FrightenPowerUp() {
    super(Map.of(0.0, FRIGHTEN_ACTIVATED,
        2 / 3.0 * DEFAULT_POWERUP_DURATION, FRIGHTEN_WARNING,
        DEFAULT_POWERUP_DURATION, FRIGHTEN_DEACTIVATED));
  }
}
