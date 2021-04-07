package ooga.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ooga.util.Vec2;

/**
 * @author George Hong
 */
public class SpriteCoordinates implements ImmutableSpriteCoordinates {

  private Vec2 position;

  @JsonCreator
  public SpriteCoordinates(@JsonProperty("position") Vec2 position) {
    this.position = position;
  }

  @JsonCreator
  public SpriteCoordinates() {
    // TODO: Verify that this is appropriate behavior for the no-arg constructor
    this.position = Vec2.ZERO;
  }

  @JsonCreator
  public SpriteCoordinates(ImmutableSpriteCoordinates c) {
    this(c.getPosition());
  }

  public void setPosition(Vec2 position) {
    this.position = position;
  }

  @Override
  @JsonGetter
  public Vec2 getPosition() {
    return position;
  }

  @Override
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
  @Override
  @JsonIgnore
  public Vec2 getTileCenter() {
    TileCoordinates tileCoordinates = getTileCoordinates();
    return new Vec2(tileCoordinates.getX() + 0.5, tileCoordinates.getY() + 0.5);
  }

  @Override
  public String toString() {
    return position.toString();
  }
}