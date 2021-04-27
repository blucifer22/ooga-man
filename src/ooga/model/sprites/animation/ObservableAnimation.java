package ooga.model.sprites.animation;

/**
 * An observable animation.
 *
 * @author Franklin Wei
 */
public interface ObservableAnimation {
  /**
   * Add an observer.
   * @param ao observer.
   */
  void addObserver(AnimationObserver ao);

  /**
   * Remove an observer.
   * @param ao Observer to remove.
   */
  void removeObserver(AnimationObserver ao);

  /**
   * Retrieve current costume name.
   * @return Current costume name.
   */
  String getCurrentCostume();

  /**
   * Advance animation time.
   * @param dt Time step.
   */
  void step(double dt);

  /**
   * (Un)pause animation.
   * @param paused whether to pause
   */
  void setPaused(boolean paused);

  /**
   * Get relative speedup of this animation.
   *
   * @return Relative time speedup.
   */
  double getRelativeSpeed();

  /**
   * Set speedup factor.
   *
   * @param relativeSpeed factor by which to speed up.
   */
  void setRelativeSpeed(double relativeSpeed);
}
