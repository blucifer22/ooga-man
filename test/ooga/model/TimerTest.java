package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.util.Clock;
import ooga.util.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimerTest {

  public static final double DT = 1 / 60.;
  private Clock clock;
  private PacmanGameState state;

  @BeforeEach
  public void setUp() {
    clock = new Clock(DT);
    state = new PacmanGameState();
  }

  @Test
  public void afterOneSecondTest() {
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

  @Test
  public void orderTest() {
    clock.addTimer(new Timer(1, x -> {
      x.incrementScore(1);
    }));
    clock.addTimer(new Timer(0.75, x -> {
      x.incrementScore(5);
    }));
    for (int k = 0; k < 46; k++) {
      clock.step(DT, state);
    }
    assertEquals(5, state.getScore());
  }

}
