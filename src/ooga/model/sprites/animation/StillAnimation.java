package ooga.model.sprites.animation;

public class StillAnimation extends SpriteAnimation {
  public StillAnimation(String costume) {
    super(costume);
  }

  @Override
  public void step(double dt) {
    // do nothing
  }
}
