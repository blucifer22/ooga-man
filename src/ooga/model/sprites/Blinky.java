package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

public class Blinky extends Ghost{

  public static final String TYPE = "blinky";

  public Blinky(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
  }

  public Blinky(SpriteDescription spriteDescription) {
    super(spriteDescription);
  }

  @Override
  public String getType() {
    return TYPE;
  }
}
