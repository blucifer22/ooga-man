package ooga.model;

import ooga.util.Vec2;

/**
 * @author George Hong
 */
public class PacMan extends MovingSprite {

  public static final String TYPE = "Pac-Man";

  public PacMan(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
  }

  @Override
  public boolean isStationary() {
    return false;
  }

  @Override
  public String getType() {
    return null;
  }

  @Override
  public SpriteCoordinates getCenter() {
    // TODO:
    return null;
  }

  @Override
  public void step(double dt) {

  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }
}
