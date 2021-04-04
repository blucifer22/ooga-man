package ooga.model;

import java.util.Iterator;

/**
 * Object that represents the structure of the Grid and its contents, along with dimensional
 * properties.
 */
class PacmanGrid implements Iterable<Tile>, ObservableGrid {

  private final int width;
  private final int height;
  private final Tile[][] contents;

  public PacmanGrid(int width, int height) {
    this.width = width;
    this.height = height;
    contents = new Tile[height][width];
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Tile getTile(TileCoordinates tileCoordinates) {
    return contents[tileCoordinates.getY()][tileCoordinates.getX()];
  }

  @Override
  public Iterator<Tile> iterator() {
    return null;
  }
}