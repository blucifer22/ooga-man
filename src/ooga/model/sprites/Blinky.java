package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Blinky extends Ghost {

  public static final String TYPE = "blinky";

  public Blinky(SpriteCoordinates position, Vec2 direction, double speed) {
    super("blinky", position, direction, speed);
  }

  public Blinky(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0), DEFAULT_SPEED);
  }

  @Override
  protected double getInitialWaitTime() {
    return 1.0;
  }
}
