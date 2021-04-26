package ooga.model.powerups;

import static ooga.model.GameEvent.FRIGHTEN_ACTIVATED;
import static ooga.model.GameEvent.FRIGHTEN_DEACTIVATED;
import static ooga.model.GameEvent.FRIGHTEN_WARNING;

import java.util.Map;

/** @author George Hong */
public class FrightenPowerUp extends TimerBasedPowerUp {

  public FrightenPowerUp() {
    super(
        Map.of(
            0.0,
            FRIGHTEN_ACTIVATED,
            2 / 3.0 * DEFAULT_POWERUP_DURATION,
            FRIGHTEN_WARNING,
            DEFAULT_POWERUP_DURATION,
            FRIGHTEN_DEACTIVATED));
  }
}
