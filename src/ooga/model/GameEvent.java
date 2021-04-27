package ooga.model;

/**
 * GameEvent correspond to Power-pill abilities that affect multiple Sprites and have limited
 * duration. They include both activate and deactivate corresponding to when an effect ends.
 */
public enum GameEvent {
  //    POWER_PILL_ACTIVATED,
  //    POWER_PILL_DEACTIVATED,
  /**
   * Activation of a speed-up.
   */
  SPEED_UP_ACTIVATED,
  /**
   * Deactivation of a speed-up.
   */
  SPEED_UP_DEACTIVATED,
  /**
   * Activation of ghost slowdown.
   */
  GHOST_SLOWDOWN_ACTIVATED,
  /**
   * Deactivation of ghost slowdown.
   */
  GHOST_SLOWDOWN_DEACTIVATED,
  /**
   * Frighten activation.
   */
  FRIGHTEN_ACTIVATED,
  /**
   * Frigthen deactivation.
   */
  FRIGHTEN_DEACTIVATED,
  /**
   * Frighten warning (starts blinking).
   */
  FRIGHTEN_WARNING,
  /**
   * Activate point bonus.
   */
  POINT_BONUS_ACTIVATED,
  /**
   * Deactivate point bonus.
   */
  POINT_BONUS_DEACTIVATED,
  /**
   * End of sprite freeze.
   */
  SPRITES_UNFROZEN,
  /**
   * Pacman death.
   */
  PACMAN_DEATH,
}
