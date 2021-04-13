package ooga.util;

/**
 * The Game Clock is designed for use with a game loop update system to keep track of the current
 * time and provides utility for events
 */
public class GameClock {

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
   * Increment this clock by a time step
   */
  public void tick() {
    seconds += dt;
  }
}
