package ooga.model;

/**
 * PacmanPowerupEvent correspond to Power-pill abilities that affect multiple Sprites and have limited
 * duration.  They include both activate and deactivate corresponding to when an effect ends.
 */
public enum PacmanPowerupEvent {
  //    POWER_PILL_ACTIVATED,
//    POWER_PILL_DEACTIVATED,
  SPEED_UP_ACTIVATED,
  SPEED_UP_DEACTIVATED,
  GHOST_SLOWDOWN_ACTIVATED,
  GHOST_SLOWDOWN_DEACTIVATED,
  FRIGHTEN_ACTIVATED,
  FRIGHTEN_DEACTIVATED,
  FRIGHTEN_WARNING,
  POINT_BONUS_ACTIVATED,
  POINT_BONUS_DEACTIVATED,
}
