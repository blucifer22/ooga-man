package ooga.model.powerups;

import java.util.Map;
import ooga.model.GameEvent;
import ooga.model.MutableGameState;
import ooga.util.Timer;

abstract class TimerBasedPowerUp implements PowerUp {

  protected static final double DEFAULT_POWERUP_DURATION = 9.0;
  private final Map<Double, GameEvent> eventSchedule;

  public TimerBasedPowerUp(Map<Double, GameEvent> schedule) {
    eventSchedule = schedule;
  }

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
