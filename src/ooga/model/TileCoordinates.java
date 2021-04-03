package ooga.model;

import ooga.util.Vec2;

/**
 * Represents a coordinate grid location composed of solely integer values
 * <p>
 * The grid coordinates follow the conventional raster coordinate system.  (0, 0) refers to the top
 * left.  The x-coordinate moves horizontally, left to right.  Increasing y-coordinate moves from
 * top to bottom.
 *
 * @author George Hong
 */
public class TileCoordinates {

  private final int x;
  private final int y;

  public TileCoordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof TileCoordinates) {
      TileCoordinates tileCoords = (TileCoordinates) o;
      return tileCoords.x == this.x && tileCoords.y == this.y;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.x + this.y;
  }
}