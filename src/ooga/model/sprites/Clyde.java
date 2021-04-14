package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.StillAnimation;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Clyde extends Ghost{

  public static final String TYPE = "clyde";

  public Clyde(SpriteCoordinates position, Vec2 direction, double speed) {
    super(new StillAnimation("clyde"),
            position, direction, speed);
  }

  public Clyde(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1,0), 3.9);
  }
}
