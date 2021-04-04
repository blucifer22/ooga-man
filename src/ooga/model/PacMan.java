package ooga.model;

import ooga.util.Vec2;

/**
 * @author George Hong
 */
public class PacMan extends MovingSprite {

  public static final String TYPE = "Pac-Man";
  private Vec2 queuedDirection;

  public PacMan(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
    queuedDirection = new Vec2(1, 0);
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
    return getCoordinates();
  }

  @Override
  public void step(double dt) {
    Vec2 userDirection = getInputSource().getRequestedDirection();
    if (getDirection().parallelTo(userDirection)) {
      setDirection(userDirection);
    } else if (!userDirection.equals(Vec2.ZERO)) {
      queuedDirection = userDirection;
    }

    Vec2 centerCoordinates = getCoordinates().getTileCenter();

  }

  private boolean inPositionForSnapping(Vec2 center, Vec2 currentPosition, Vec2 direction,
      double dt, double speed) {
    if (currentPosition.equals(center)) {
      return true;
    }
    Vec2 nextPosition = currentPosition.add(direction.scalarMult(speed).scalarMult(dt));


    return false;
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }
}
