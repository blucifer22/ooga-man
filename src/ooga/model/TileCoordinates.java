package ooga.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
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

  public TileCoordinates(@JsonProperty("x") int x, @JsonProperty("y") int y) {
    this.x = x;
    this.y = y;
  }

  public TileCoordinates() {
    // TODO: Verify that this is appropriate behavior for the no-arg constructor
    this.x = 0;
    this.y = 0;
  }
  public Vec2 toVec2(){
    return new Vec2(x, y);
  }

  @JsonGetter
  public int getX() {
    return x;
  }

  @JsonGetter
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