package ooga.model.powerups;

import static ooga.model.GameEvent.GHOST_SLOWDOWN_ACTIVATED;
import static ooga.model.GameEvent.GHOST_SLOWDOWN_DEACTIVATED;

import java.util.Map;

/**
 * Power-up that slows down ghosts.  The Power-up occurs for the default duration of powerups and
 * occurs immediately
 *
 * @author George Hong
 */
public class GhostSlowdownPowerUp extends TimerBasedPowerUp {

  /**
   * Construct a powerup that slows down ghosts.
   */
  public GhostSlowdownPowerUp() {
    super(
        Map.of(
            0.0, GHOST_SLOWDOWN_ACTIVATED, DEFAULT_POWERUP_DURATION, GHOST_SLOWDOWN_DEACTIVATED));
  }
}
