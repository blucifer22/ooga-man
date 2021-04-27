package ooga.model.grid;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ooga.util.Vec2;

/**
 * Represents a coordinate grid location composed of solely integer values
 *
 * <p>The grid coordinates follow the conventional raster coordinate system. (0, 0) refers to the
 * top left. The x-coordinate moves horizontally, left to right. Increasing y-coordinate moves from
 * top to bottom.
 *
 * @author George Hong
 */
public class TileCoordinates {

  private final int x;
  private final int y;

  /**
   * Constructs an instance of this class
   *
   * @param x x-coordinate representing the position of the Tile
   * @param y y-coordinate representing the position of the Tile
   */
  public TileCoordinates(@JsonProperty("x") int x, @JsonProperty("y") int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs an instance of this class from a vector, truncating extra precision, if necessary
   *
   * @param coordinates vector representing coordinates to construct a TileCoordinate object from
   */
  public TileCoordinates(Vec2 coordinates) {
    this((int) coordinates.getX(), (int) coordinates.getY());
  }

  /**
   * Default constructor for TileCoordinates.  Assumes Coordinates at the top left corner,
   * corresponding to (0, 0).
   */
  public TileCoordinates() {
    this.x = 0;
    this.y = 0;
  }

  /**
   * Converts TileCoordinates to a vector of the position
   *
   * @return Vector corresponding to the position of the tile coordinates
   */
  public Vec2 toVec2() {
    return new Vec2(x, y);
  }

  /**
   * Returns the x-coordinate of this TileCoordinate object
   *
   * @return x-coordinate
   */
  @JsonGetter
  public int getX() {
    return x;
  }

  /**
   * Returns the y-coordinate of this TileCoordinate object
   *
   * @return x-coordinate
   */
  @JsonGetter
  public int getY() {
    return y;
  }

  /**
   * Checks to see whether two TileCoordinate objects refer to the same position on the grid
   *
   * @param o other TileCoordinate objects.
   * @return whether the other TileCoordinate object refers to the same position.
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof TileCoordinates) {
      TileCoordinates tileCoords = (TileCoordinates) o;
      return tileCoords.x == this.x && tileCoords.y == this.y;
    }
    return false;
  }

  /**
   * Gets the hash code for this TileCoordinate object.
   *
   * @return hash code for this TileCoordinate object.
   */
  @Override
  public int hashCode() {
    return this.x + this.y;
  }
}
