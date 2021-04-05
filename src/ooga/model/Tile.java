package ooga.model;

import ooga.util.Vec2;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents basic properties of a Tile such as whether they can be occupied
 *
 * @author George Hong
 */
public class Tile implements TileObservable {

  private final TileCoordinates tileCoordinates;
  private final String tileType;
  private final boolean isOpenToPacman;
  private final boolean isOpenToGhosts;

  @JsonCreator
  public Tile(
      @JsonProperty("coordinates") TileCoordinates tileCoordinates,
      @JsonProperty("type") String initialType,
      @JsonProperty("openToPacman") boolean isOpenToPacman,
      @JsonProperty("openToGhosts") boolean isOpenToGhosts) {
    this.tileCoordinates = tileCoordinates;
    this.tileType = initialType;
    this.isOpenToPacman = isOpenToPacman;
    this.isOpenToGhosts = isOpenToGhosts;
  }

  @JsonGetter
  public TileCoordinates getCoordinates() {
    return tileCoordinates;
  }

  @JsonGetter
  public boolean isOpenToPacman() {
    // true if pacman can move into this tile
    return isOpenToPacman;
  }

  @JsonGetter
  public boolean isOpenToGhosts() {
    return isOpenToGhosts;
  }

  @JsonGetter
  public String getType() {
    return tileType;
  }
}
