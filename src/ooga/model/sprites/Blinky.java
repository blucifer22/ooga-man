package ooga.model.sprites;

import ooga.model.grid.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Blinky extends Ghost {

  /**
   * Create a Blinky sprite.
   * @param position Initial position.
   * @param direction Initial orientation.
   * @param speed Initial speed.
   */
  public Blinky(SpriteCoordinates position, Vec2 direction, double speed) {
    super("blinky", position, direction, speed);
  }

  /**
   * Create a Blinky sprite from a sprite description.
   * @param spriteDescription Sprite description.
   */
  public Blinky(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0), DEFAULT_SPEED);
  }

  /**
   * Get the amount of time this ghost waits in the pen.
   * @return Wait time.
   */
  @Override
  protected double getInitialWaitTime() {
    return 1.0;
  }
}
