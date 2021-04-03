package ooga.model;

/**
 * Represents basic properties of a Tile such as whether they can be occupied
 */
public class Tile {

  private final TileCoordinates tileCoordinates;
  private final String tileType;
  private final boolean isOpen;

  public Tile(TileCoordinates tileCoordinates, String initialType, boolean isOpen) {
    this.tileCoordinates = tileCoordinates;
    this.tileType = initialType;
    this.isOpen = isOpen;
  }

  public TileCoordinates getCoordinates() {
    return tileCoordinates;
  }

  public boolean isOpen() {
    // true if pacman/ghosts can move into this tile
    return isOpen;
  }

  public String getType() {
    return tileType;
  }
}
