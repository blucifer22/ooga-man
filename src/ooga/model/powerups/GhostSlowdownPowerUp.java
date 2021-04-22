package ooga.model.powerups;

import java.util.Map;
import ooga.model.PacmanPowerupEvent;

import static ooga.model.PacmanPowerupEvent.*;

public class GhostSlowdownPowerUp extends TimerBasedPowerUp {

  public GhostSlowdownPowerUp() {
    super(Map.of(0.0, GHOST_SLOWDOWN_ACTIVATED, DEFAULT_POWERUP_DURATION,
        GHOST_SLOWDOWN_DEACTIVATED));
  }
}
