package ooga.model.powerups;

import ooga.model.MutableGameState;
import ooga.model.PacmanPowerupEvent;
import ooga.util.Timer;

import java.util.Map;

abstract class TimerBasedPowerUp implements PowerUp {

  protected static final double DEFAULT_POWERUP_DURATION = 9.0;
  private final Map<Double, PacmanPowerupEvent> eventSchedule;

  public TimerBasedPowerUp(Map<Double, PacmanPowerupEvent> schedule) {
    eventSchedule = schedule;
  }

  @Override
  public void executePowerUp(MutableGameState state) {
    eventSchedule
        .entrySet()
        .forEach(
            entry ->
                state.getClock()
                    .addTimer(new Timer(entry.getKey(),
                        (pgs) -> state.notifyPowerupListeners(entry.getValue())))
        );
  }
}
