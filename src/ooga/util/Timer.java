package ooga.util;

import java.util.function.Consumer;
import ooga.model.MutableGameState;
import org.jetbrains.annotations.NotNull;

/**
 * Timer object that depends on the GameClock to determine when the passed code should execute.
 */
public class Timer implements Comparable<Timer>{

  private final Consumer<MutableGameState> executable;
  private final double delay;
  private double instantiationTimeStamp;

  /**
   * Creates an instance of the Timer object
   *
   * @param delay      number of seconds before this code executes
   * @param executable code to execute when the Timer expires
   */
  public Timer(double delay, Consumer<MutableGameState> executable) {
    this.delay = delay;
    this.executable = executable;
  }

  /**
   * Sets the time that this Timer was created
   *
   * @param timeStamp start time in seconds
   */
  public void setInstantiationTimeStamp(double timeStamp) {
    instantiationTimeStamp = timeStamp;
  }

  /**
   * Executes the action
   *
   * @param gameState game state that has to be passed in
   */
  public void execute(MutableGameState gameState) {
    executable.accept(gameState);
  }

  /**
   * Optimization for timers
   * @param o
   * @return
   */
  @Override
  public int compareTo(@NotNull Timer o) {
    return Double.compare(this.delay, o.delay);
  }
}
