package ooga.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents basic properties of a Tile such as whether they can be occupied
 *
 * @author George Hong
 */
public class Tile {

  private final TileCoordinates tileCoordinates;
  private final String tileType;
  private final boolean isOpenToPacman;
  private final boolean isOpenToGhosts;

  @JsonCreator
  public Tile(
      @JsonProperty("coordinates") TileCoordinates tileCoordinates,
      @JsonProperty("type") String initialType,
      @JsonProperty("isOpenToPacman") boolean isOpenToPacman,
      @JsonProperty("isOpenToGhosts") boolean isOpenToGhosts) {
    this.tileCoordinates = tileCoordinates;
    this.tileType = initialType;
    this.isOpenToPacman = isOpenToPacman;
    this.isOpenToGhosts = isOpenToGhosts;
  }

  public TileCoordinates getCoordinates() {
    return tileCoordinates;
  }

  public boolean isOpenToPacman() {
    // true if pacman can move into this tile
    return isOpenToPacman;
  }

  public boolean isOpenToGhosts() {
    return isOpenToGhosts;
  }

  public String getType() {
    return tileType;
  }
}
