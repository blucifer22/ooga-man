package ooga.model.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ooga.model.api.ObservableGrid;
import ooga.model.leveldescription.GridDescription;

/**
 * Object that represents the structure of the Grid and its contents, along with dimensional
 * properties.
 *
 * @author George Hong
 */
public class PacmanGrid implements Iterable<Tile>, ObservableGrid {

  private final int width;
  private final int height;
  private final List<List<Tile>> contents;

  /**
   * Constructs an instance of PacmanGrid
   *
   * @param width  width of the grid
   * @param height height of the grid
   */
  public PacmanGrid(int width, int height) {
    this.width = width;
    this.height = height;
    contents = initialize2DTileList(width, height);
  }

  /**
   * Constructs an instance of PacmanGrid from a GridDescription
   *
   * @param gridDescription supplied GridDescription
   */
  public PacmanGrid(GridDescription gridDescription) {
    this.width = gridDescription.getWidth();
    this.height = gridDescription.getHeight();
    this.contents = gridDescription.getGrid();
  }

  private List<List<Tile>> initialize2DTileList(int width, int height) {
    List<List<Tile>> ret = new ArrayList<>();
    List<Tile> emptyRow = new ArrayList<>();
    for (int col = 0; col < width; col++) {
      emptyRow.add(null);
    }
    for (int row = 0; row < height; row++) {
      ret.add(new ArrayList<>(emptyRow));
    }
    return ret;
  }

  /**
   * Returns the width of this grid
   *
   * @return width of this grid
   */
  public int getWidth() {
    return width;
  }

  /**
   * Returns the height of this grid
   *
   * @return height of this grid
   */
  public int getHeight() {
    return height;
  }

  /**
   * Returns the tile corresponding to the position supplied by the tileCoordinates
   *
   * @param tileCoordinates tileCoordinates object that encodes a position on the Grid
   * @return tile that occupies the position tileCoordinates.
   */
  public Tile getTile(TileCoordinates tileCoordinates) {
    return contents.get(tileCoordinates.getY()).get(tileCoordinates.getX());
  }

  /**
   * Returns whether the provided TileCoordinates is within the boundaries of the Grid
   *
   * @param tileCoordinates TileCoordinates object that encodes a position on the grid
   * @return boolean representing whether the given coordinates are within the bounds of the grid.
   */
  public boolean inBoundaries(TileCoordinates tileCoordinates) {
    int x = tileCoordinates.getX();
    int y = tileCoordinates.getY();
    return x < width && x >= 0 && y < height && y >= 0;
  }

  /**
   * Sets a tile object on onto the Grid, overwriting its previous contents.
   *
   * @param x    x-coordinate of the grid to write to
   * @param y    y-coordinate of the grid to write to
   * @param tile tile object to place in the provided coordinates
   */
  public void setTile(int x, int y, Tile tile) {
    contents.get(y).set(x, tile);
  }

  /**
   * @return null.
   * @deprecated gets an iterator for this grid.  Not used anymore.
   */
  @Override
  public Iterator<Tile> iterator() {
    return null;
  }

  /**
   * Returns the internal contents of the Grid
   *
   * @return 2D structure representing the structure of the Grid
   */
  public List<List<Tile>> getAllTiles() {
    return contents;
  }
}
