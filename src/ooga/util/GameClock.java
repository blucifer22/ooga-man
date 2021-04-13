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
  private double dt;

  /**
   * Initializes a game clock where each tick increments the time by dt
   *
   * @param dt 1 / (frame rate)
   */
  public GameClock(double dt) {
    this(0, dt);
  }

  public GameClock(double startingTime, double frameRate) {
    this.dt = frameRate;
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
   * Sets the timestep to a new value
   *
   * @param dt time step
   */
  public void setTimeStep(double dt) {
    this.dt = dt;
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
   *
   * @param gameState state of the game that may need to be modified.
   */
  public void checkTimerStatus(MutableGameState gameState) {
    for (Timer timer : activeTimers) {
      if (seconds >= timer.getExpirationTime()) {
        activeTimers.remove(timer);
        timer.execute(gameState);
      } else {
        break;
      }
    }
  }

  /**
   * Increment this clock by a time step
   */
  public void tick(MutableGameState gameState) {
    seconds += dt;
    checkTimerStatus(gameState);
  }
}
