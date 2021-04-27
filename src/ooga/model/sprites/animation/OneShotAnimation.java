package ooga.model.sprites.animation;

import java.util.List;

/**
 * An animation that plays only once, and in order.
 *
 * @author Franklin Wei
 */
public class OneShotAnimation extends SpriteAnimation {
  private final List<String> costumes;
  private final double framePeriod;
  private double animationTime;

  /**
   * Construct a one-shot animation.
   * @param costumes List of costumes, in order.
   * @param framePeriod Duration of each frame, in seconds.
   */
  public OneShotAnimation(List<String> costumes, double framePeriod) {
    super(costumes.get(0));

    this.costumes = costumes;
    this.framePeriod = framePeriod;
    this.animationTime = 0;
  }

  /**
   * Advance animation
   * @param dt Time step.
   */
  @Override
  public void step(double dt) {
    if (!isPaused()) {
      animationTime += getRelativeSpeed() * dt;
      int costumeIndex = Math.min(costumes.size() - 1, (int) (animationTime / framePeriod));
      setCostume(costumes.get(costumeIndex));

      if (animationTime > framePeriod * costumes.size())
        notifyObservers(ao -> ao.onAnimationComplete());
    }
  }
}
