package ooga.model.sprites.animation;

/**
 * A non-moving animation.
 *
 * @author Franklin wei
 */
public class StillAnimation extends SpriteAnimation {
  /**
   * Construct a still animation.
   * @param costume Costume
   */
  public StillAnimation(String costume) {
    super(costume);
  }

  @Override
  public void step(double dt) {
    // do nothing
  }
}
