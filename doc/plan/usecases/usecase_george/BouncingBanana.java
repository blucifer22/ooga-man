package ooga.usecase_george;

import ooga.model.MovingSprite;
import ooga.model.SpriteCoordinates;

public class BouncingBanana extends MovingSprite {

  SpriteCoordinates pos;
  private double xVel;
  private double yVel;

  @Override
  public SpriteCoordinates getCoordinates() {
    return new SpriteCoordinates();
  }

  public BouncingBanana(SpriteCoordinates pos) {

  }

  public void applyBounce() {

  }

  @Override
  boolean isStationary() {
    return false;
  }

  @Override
  public String getType() {
    return "BouncingBanana";
  }

  @Override
  public SpriteCoordinates getCenter() {
    return new SpriteCoordinates();
  }

  @Override
  void step(double dt) {

  }
}
