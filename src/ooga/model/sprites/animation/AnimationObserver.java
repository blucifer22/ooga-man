package ooga.model.sprites.animation;

/**
 * Interface to be implemented by an observer of an animation.
 */
public interface AnimationObserver {
  /**
   * Called on a costume change.
   * @param newCostume New costume name.
   */
  void onCostumeChange(String newCostume);

  /**
   * Called upon animation termination.
   *
   * Only for non-periodic animations.
   */
  default void onAnimationComplete() {}
}
