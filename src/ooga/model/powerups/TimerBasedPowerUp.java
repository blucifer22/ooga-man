package ooga.model.powerups;

import java.util.Map;
import ooga.model.GameEvent;
import ooga.model.MutableGameState;
import ooga.util.Timer;

/**
 * A power-up that works by sending time-delayed game events, which are received
 * by sprites.
 */
abstract class TimerBasedPowerUp implements PowerUp {

  /**
   * Default length of a powerup.
   */
  protected static final double DEFAULT_POWERUP_DURATION = 9.0;
  private final Map<Double, GameEvent> eventSchedule;

  /**
   * Create a timer-based powerup.
   * @param schedule Schedule of events to send.
   */
  public TimerBasedPowerUp(Map<Double, GameEvent> schedule) {
    eventSchedule = schedule;
  }

  /**
   * Execute this power-up.
   * @param state State on which to act.
   */
  @Override
  public void executePowerUp(MutableGameState state) {
    eventSchedule
        .entrySet()
        .forEach(
            entry ->
                state
                    .getClock()
                    .addTimer(
                        new Timer(
                            entry.getKey(), (pgs) -> state.broadcastEvent(entry.getValue()))));
  }
}
