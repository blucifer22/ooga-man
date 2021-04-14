package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Clyde extends Ghost{

  public static final String TYPE = "clyde";

  public Clyde(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
  }

  public Clyde(SpriteDescription spriteDescription) {
    super(spriteDescription);
  }

  @Override
  public String getCostume() {
    return TYPE;
  }

}
