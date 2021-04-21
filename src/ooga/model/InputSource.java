package ooga.model;

import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * Sprites use this interface to obtain input events from various
 * sources (i.e. AI, human input from a keyboard, input over a
 * network).
 */
public interface InputSource {
  /**
   * This method queries the source for the _current_ state of which
   * directional inputs are being given to the sprite.
   *
   * If no key is being held down, this should return Vec2.ZERO.
   *
   * The sign convention for this return value is the raster
   * convention -- +Y is directed downwards.
   *
   * @return currently requested input direction, or Vec2.ZERO if none
   */
  Vec2 getRequestedDirection();

  /**
   * Checks if the "action" button is being pressed or otherwise
   * requested.
   *
   * For example, if the user holds down spacebar in the human input
   * manager, this method should return true.
   *
   * @return whether the "action" input is being held or otherwise
   * requested (i.e. by AI input sources)
   */
  boolean isActionPressed();

  /**
   * Adds a Sprite target to the InputSource.
   *
   * @param target The Sprite to add to the InputSource.
   */
  void addTarget(Sprite target);
}
