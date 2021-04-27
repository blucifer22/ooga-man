package ooga.model.sprites;

import ooga.model.grid.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Pinky extends Ghost {

  /**
   * Construct a "Pinky" sprite.
   * @param position Position.
   * @param direction Orientation.
   * @param speed Initial speed.
   */
  public Pinky(SpriteCoordinates position, Vec2 direction, double speed) {
    super("pinky", position, direction, speed);
  }

  /**
   * Construct Pinky from a Sprite Description.
   * @param spriteDescription Description to use.
   */
  public Pinky(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0), DEFAULT_SPEED);
  }

  /**
   * Get initial wait time.
   * @return Wait time (seconds).
   */
  @Override
  protected double getInitialWaitTime() {
    return 4.0;
  }
}
