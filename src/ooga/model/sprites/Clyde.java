package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Clyde extends Ghost {

  public Clyde(SpriteCoordinates position, Vec2 direction, double speed) {
    super("clyde", position, direction, speed);
  }

  public Clyde(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0), DEFAULT_SPEED);
  }

  @Override
  protected double getInitialWaitTime() {
    return 8.0;
  }
}
