package ooga.model;

import ooga.util.Vec2;

/**
 * Mobile sprites can be in motion (although this does not need to always be true -- mobile sprites
 * can occasionally remain stationary)
 */
public abstract class MovingSprite extends Sprite {

  private InputSource inputSource;
  private double speed;

  /**
   * @param position
   * @param direction
   * @param speed     property of a moving Sprite
   */
  public MovingSprite(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction);
    this.speed = speed;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }


  protected InputSource getInputSource() {
    return inputSource;
  }

  public void setInputSource(InputSource s) {
    inputSource = s;

  }
}