package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
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
    state.setPlayers(new Player(1, new HumanInputManager(KeybindingType.PLAYER_1)), null);
  }

  @Test
  public void afterOneSecondTest() {
    clock.addTimer(
        new Timer(
            1,
            x -> {
              x.incrementScore(1);
            }));
    for (int k = 0; k < 60; k++) {
      clock.step(DT, state);
    }
    assertEquals(1, state.getScore());

    clock.addTimer(
        new Timer(
            1,
            x -> {
              x.incrementScore(5);
            }));
    for (int k = 0; k < 61; k++) {
      clock.step(DT, state);
    }
    assertEquals(6, state.getScore());
  }

  @Test
  public void orderTest() {
    clock.addTimer(
        new Timer(
            1,
            x -> {
              x.incrementScore(1);
            }));
    clock.addTimer(
        new Timer(
            0.75,
            x -> {
              x.incrementScore(5);
            }));
    for (int k = 0; k < 46; k++) {
      clock.step(DT, state);
    }
    assertEquals(5, state.getScore());
  }

  @Test
  public void testClearTimer() {
    clock.addTimer(
        new Timer(
            4,
            x -> {
              x.incrementScore(1);
            }));
    clock.clear();
    for (int k = 0; k < 360; k++) {
      clock.step(DT, state);
    }
    assertEquals(0, state.getScore());
  }

  @Test
  public void testResetTimer() {
    for (int k = 0; k < 60; k++) {
      clock.step(DT, state);
    }
    clock.addTimer(
        new Timer(
            1,
            x -> {
              x.incrementScore(1);
            }));
    clock.reset();
    for (int k = 0; k < 30; k++) {
      clock.step(DT, state);
    }
    clock.reset();
    for (int k = 0; k < 30; k++) {
      clock.step(DT, state);
    }
    clock.reset();
    for (int k = 0; k < 30; k++) {
      clock.step(DT, state);
    }
    assertEquals(0, state.getScore());
  }
}
