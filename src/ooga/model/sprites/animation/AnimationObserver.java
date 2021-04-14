package ooga.model.sprites.animation;

@FunctionalInterface
public interface AnimationObserver {
  void onCostumeChange(String newCostume);
}
