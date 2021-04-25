package ooga.model.powerups;

import java.util.Map;

import static ooga.model.GameEvent.*;

/**
 * @author George Hong
 */
public class GhostSlowdownPowerUp extends TimerBasedPowerUp {

  public GhostSlowdownPowerUp() {
    super(Map.of(0.0, GHOST_SLOWDOWN_ACTIVATED, DEFAULT_POWERUP_DURATION,
        GHOST_SLOWDOWN_DEACTIVATED));
  }
}
