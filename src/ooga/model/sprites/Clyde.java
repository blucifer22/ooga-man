package ooga.model.sprites;

import ooga.model.grid.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Clyde extends Ghost {

  /**
   * Construct a "Clyde" sprite.
   * @param position Initial position.
   * @param direction Initial orientation.
   * @param speed Movement speed.
   */
  public Clyde(SpriteCoordinates position, Vec2 direction, double speed) {
    super("clyde", position, direction, speed);
  }

  /**
   * Construct Clyde from a sprite description.
   * @param spriteDescription Sprite description.
   */
  public Clyde(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0), DEFAULT_SPEED);
  }

  /**
   * Get wait time.
   * @return Initial wait time. Clyde is... special.
   */
  @Override
  protected double getInitialWaitTime() {
    return 8.0;
  }
}
