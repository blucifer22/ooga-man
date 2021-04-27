package ooga.model.sprites;

import ooga.model.grid.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/**
 * Inky!
 * @author Matthew Belissary
 */
public class Inky extends Ghost {
  /**
   * Construct an "Inky" sprite.
   * @param position Initial position.
   * @param direction Initial direction.
   * @param speed Movement speed..
   */
  public Inky(SpriteCoordinates position, Vec2 direction, double speed) {
    super("inky", position, direction, speed);
  }

  /**
   * Construct Inky from a sprite description.
   * @param spriteDescription Sprite description.
   */
  public Inky(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0), DEFAULT_SPEED);
  }

  /**
   * Initial wait time of this sprite.
   * @return Initial wait time (seconds).
   */
  @Override
  protected double getInitialWaitTime() {
    return 6.0;
  }
}
