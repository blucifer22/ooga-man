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

  public Vec2 getExactCoordinates() {
    return position;
  }

  public TileCoordinates getTileCoordinates() {
    double x = position.getX();
    double y = position.getY();
    return new TileCoordinates((int) x, (int) y);
  }

  /**
   * Returns the coordinates of the center of the tile currently resided by the Sprite associated
   * with these coordinates.
   *
   * @return Vec2 containing the center of the Tile
   */
  public Vec2 getTileCenter() {
    TileCoordinates tileCoordinates = getTileCoordinates();
    return new Vec2(tileCoordinates.getX() + 0.5, tileCoordinates.getY() + 0.5);
  }

//  public Vec2 getTileOffset() {
//    return null;
//  }
}