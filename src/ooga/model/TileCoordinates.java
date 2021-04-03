package ooga.model;

/**
 * Represents a coordinate grid location composed of solely integer values
 * <p>
 * The grid coordinates follow the conventional raster coordinate system.  (0, 0) refers to the top
 * left.  The x-coordinate moves horizontally, left to right.  Increasing y-coordinate moves from
 * top to bottom.
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
}