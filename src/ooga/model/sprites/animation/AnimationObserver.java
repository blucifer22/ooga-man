package ooga.model.sprites.animation;

public interface AnimationObserver {
  void onCostumeChange(String newCostume);
  default void onAnimationComplete() {}
}
