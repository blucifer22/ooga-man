package ooga.model.powerups;

import ooga.model.MutableGameState;
import static ooga.model.PacmanPowerupEvent.FRIGHTEN_ACTIVATED;

class FrightenPowerUp extends TimerBasedPowerUp {
  public FrightenPowerUp() {
    super(Map.of(0, FRIGHTEN_ACTIVATED,
                 2/3.0 * DEFAULT_POWERUP_DURATION, FRIGHTEN_WARNING,
                 DEFAULT_POWERUP_DURATION, FRIGHTEN_DEACTIVATED));
  }
}
