package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Inky extends Ghost{

  public static final String TYPE = "inky";

  public Inky(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
  }

  public Inky(SpriteDescription spriteDescription) {
    super(spriteDescription);
  }

  @Override
  public String getCostume() {
    return TYPE;
  }

}
