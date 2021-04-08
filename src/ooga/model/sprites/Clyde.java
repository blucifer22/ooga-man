package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

public class Clyde extends Ghost{

  public static final String TYPE = "clyde";

  public Clyde(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
  }

  public Clyde(SpriteDescription spriteDescription) {
    super(spriteDescription);
  }

  @Override
  public String getType() {
    return TYPE;
  }

}
