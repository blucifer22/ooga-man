package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Pinky extends Ghost{

  public static final String TYPE = "pinky";

  public Pinky(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
  }

  public Pinky(SpriteDescription spriteDescription) {
    super(spriteDescription);
  }

  @Override
  public String getType() {
    return TYPE;
  }

}
