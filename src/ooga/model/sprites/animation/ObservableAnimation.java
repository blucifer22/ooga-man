package ooga.model.sprites.animation;

public interface ObservableAnimation {
  void addObserver(AnimationObserver ao);

  void removeObserver(AnimationObserver ao);

  String getCurrentCostume();

  void step(double dt);

  void setPaused(boolean paused);

  double getRelativeSpeed();

  void setRelativeSpeed(double relativeSpeed);
}
