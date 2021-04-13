package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.util.GameClock;
import ooga.util.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimerTest {

  public static final double DT = 1 / 60.;
  private GameClock clock;
  private PacmanGameState state;

  @BeforeEach
  public void setUp() {
    clock = new GameClock(DT);
    state = new PacmanGameState();
  }

  @Test
  public void afterOneSecond() {
    clock.addTimer(new Timer(1, x -> {
      x.incrementScore(1);
    }));
    for (int k = 0; k < 60; k++) {
      clock.step(DT, state);
    }
    assertEquals(1, state.getScore());

    clock.addTimer(new Timer(1, x -> {
      x.incrementScore(5);
    }));
    for (int k = 0; k < 61; k++) {
      clock.step(DT, state);
    }
    assertEquals(6, state.getScore());
  }

}
