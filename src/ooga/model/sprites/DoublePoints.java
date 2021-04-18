package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.util.Vec2;

public class DoublePoints extends PowerPill implements PowerUp{

  public DoublePoints(SpriteCoordinates position, Vec2 direction) {
    super(position, direction);
  }

  @Override
  public void activatePowerUp() {
    uponHitBy();
  }

  @Override
  public void deactivatePowerUp() {

  }
}
