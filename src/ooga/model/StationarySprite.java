package ooga.model;

import ooga.util.Vec2;

/**
 * Stationary sprites can never move.
 */
public abstract class StationarySprite extends Sprite {

  public StationarySprite(SpriteCoordinates position, Vec2 direction) {
    super(position, direction);
  }
}