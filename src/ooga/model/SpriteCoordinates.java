package ooga.model;

import ooga.util.Vec2;

/**
 * @author George Hong
 */
public class SpriteCoordinates {

  private final Vec2 position;

  public SpriteCoordinates(Vec2 position) {
    this.position = position;
  }

  public SpriteCoordinates() {
    // TODO: Verify that this is appropriate behavior for the no-arg constructor
    this.position = Vec2.ZERO;
  }

  public Vec2 getExactCoordinates() {
    return position;
  }

  public TileCoordinates getTileCoordinates() {
    double x = position.getX();
    double y = position.getY();
    return new TileCoordinates((int) x, (int) y);
  }

//  public Vec2 getTileOffset() {
//    return null;
//  }
}