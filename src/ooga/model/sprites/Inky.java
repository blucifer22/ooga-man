package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Inky extends Ghost {

  public static final String TYPE = "inky";

  public Inky(SpriteCoordinates position, Vec2 direction, double speed) {
    super("inky", position, direction, speed);
  }

  public Inky(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0), DEFAULT_SPEED);
  }

  @Override
  protected double getInitialWaitTime() {
    return 6.0;
  }
}
