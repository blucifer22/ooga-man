package ooga.model;

public class PacMan extends MovingSprite{

  public static final String TYPE = "Pac-Man";

  public PacMan()

  @Override
  public boolean isStationary() {
    return false;
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public SpriteCoordinates getCenter() {
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
