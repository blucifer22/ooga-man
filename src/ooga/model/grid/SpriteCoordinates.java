package ooga.model.grid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ooga.util.Vec2;

/**
 * SpriteCoordinates encode the position of Sprites for animation.
 *
 * @author George Hong
 */
public class SpriteCoordinates {

  private final Vec2 position;

  /**
   * Constructs an instance of this class from a provided vector
   *
   * @param position vector encoding the position of a Sprite
   */
  @JsonCreator
  public SpriteCoordinates(@JsonProperty("position") Vec2 position) {
    this.position = position;
  }

  /**
   * Default constructor, assuming a position of the origin (0, 0)
   */
  @JsonCreator
  public SpriteCoordinates() {
    this.position = Vec2.ZERO;
  }

  /**
   * Returns the position of this Sprite
   *
   * @return vector encoding the position of this Sprite.
   */
  @JsonGetter
  public Vec2 getPosition() {
    return position;
  }

  /**
   * Returns the tile coordinates of this Sprite.  TileCoordinates are strictly integer, so the
   * Sprite position is truncated.
   *
   * @return TileCoordinates representing the tile occupied by the SpriteCoordinates
   */
  @JsonIgnore
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
  @JsonIgnore
  public Vec2 getTileCenter() {
    TileCoordinates tileCoordinates = getTileCoordinates();
    return new Vec2(tileCoordinates.getX() + 0.5, tileCoordinates.getY() + 0.5);
  }

  /**
   * Converts the coordiantes into a easily printable form
   *
   * @return position represented as a String
   */
  @Override
  public String toString() {
    return position.toString();
  }
}
