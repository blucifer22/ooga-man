package ooga.model;

import java.util.Iterator;

/**
 * Object that represents the structure of the Grid and its contents, along with dimensional
 * properties.
 */
abstract class PacmanGrid implements Iterable<Tile> {

  public int getWidth() {
    return 0;
  }

  public int getHeight() {
    return 0;
  }

  public Tile getTile(TileCoordinates tileCoordinates) {
    return null;
  }

  @Override
  public Iterator<Tile> iterator() {
    return null;
  }
}