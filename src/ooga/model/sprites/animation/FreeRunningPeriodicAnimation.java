package ooga.model.sprites.animation;

import java.util.List;

/**
 * A free-running periodic animation. This contains a collection of costume names, a frame rate, and
 * can be paused.
 *
 * @author Franklin Wei
 */
public class FreeRunningPeriodicAnimation extends PeriodicAnimation {
  private final double framePeriod;
  private double animationTime;

  /**
   * Construct a free-running, periodic animation.
   * @param costumes Costumes, in order.
   * @param frameOrder Order in which to cycle costumes.
   * @param framePeriod Duration of each frame.
   */
  public FreeRunningPeriodicAnimation(
      List<String> costumes, FrameOrder frameOrder, double framePeriod) {
    super(costumes, frameOrder);
    this.framePeriod = framePeriod;
    this.animationTime = 0;
  }

  /**
   * Step animation.
   * @param dt Time step.
   */
  @Override
  public void step(double dt) {
    if (!isPaused()) {
      animationTime += dt * getRelativeSpeed();

      setPhase((int) (animationTime / framePeriod));
    }
  }
}
