package ooga.model.powerups;

import ooga.model.MutableGameState;
import ooga.model.PacmanPowerupEvent;
import ooga.util.Timer;

import java.util.Map;

abstract class TimerBasedPowerUp implements PowerUp {
    private final Map<Double, PacmanPowerupEvent> eventSchedule;

    protected final double DEFAULT_POWERUP_DURATION = 9.0;

    public TimerBasedPowerUp(Map<Double, PacmanPowerupEvent> schedule) {
        eventSchedule = schedule;
    }

    @Override
    public void executePowerUp(MutableGameState state) {
      eventSchedule
          .entrySet()
          .forEach(
              (delay, event) ->
              state.getClock()
                   .addTimer(new Timer(delay, state.notifyPowerupListeners(event)))
                   );
    }
}
