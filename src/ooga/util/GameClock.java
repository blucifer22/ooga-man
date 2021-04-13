package ooga.util;

import java.util.ArrayList;
import java.util.PriorityQueue;
import ooga.model.MutableGameState;

/**
 * The Game Clock is designed for use with a game loop update system to keep track of the current
 * time and provides utility for events
 */
public class GameClock {

  private final PriorityQueue<Timer> activeTimers;
  private double seconds;

  /**
   * Initializes a game clock where each tick increments the time by dt
   */
  public GameClock() {
    this(0);
  }

  public GameClock(double startingTime) {
    this.seconds = startingTime;
    this.activeTimers = new PriorityQueue<>();
  }

  /**
   * Adds a timer for execution
   *
   * @param timer
   */
  public void addTimer(Timer timer) {
    timer.setInstantiationTimeStamp(seconds);
    activeTimers.add(timer);
  }


  /**
   * Returns the number of seconds that have elapsed
   *
   * @return time in seconds
   */
  public double getTime() {
    return seconds;
  }

  /**
   * Checks to see Timers that should be executed.
   *
   * @param gameState state of the game that may need to be modified.
   */
  public void checkTimerStatus(MutableGameState gameState) {
    while (!activeTimers.isEmpty()){
      Timer timer = activeTimers.peek();
      if (seconds >= timer.getExpirationTime()) {
        activeTimers.remove();
        timer.execute(gameState);
      } else {
        break;
      }
    }
  }

  /**
   * @param dt        time step
   * @param gameState
   */
  public void step(double dt, MutableGameState gameState) {
    seconds += dt;
    checkTimerStatus(gameState);
  }
}
