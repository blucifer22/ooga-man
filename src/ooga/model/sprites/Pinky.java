package ooga.model.sprites;

import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.StillAnimation;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Pinky extends Ghost {

  public static final String TYPE = "pinky";

  public Pinky(SpriteCoordinates position, Vec2 direction, double speed) {
    super(new StillAnimation("pinky"),
            position, direction, speed);
  }

  public Pinky(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0), 4.0);
  }

  @Override
  protected double getInitialWaitTime() {
    return 4.0;
  }
}
