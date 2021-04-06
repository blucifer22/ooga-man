package ooga.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ooga.model.api.ObservableGrid;

/**
 * Object that represents the structure of the Grid and its contents, along with dimensional
 * properties.
 */
class PacmanGrid implements Iterable<Tile>, ObservableGrid {

  private final int width;
  private final int height;
  private final List<List<Tile>> contents;

  public PacmanGrid(int width, int height) {
    this.width = width;
    this.height = height;
    contents = initialize2DTileList(width, height);
  }

  private List<List<Tile>> initialize2DTileList(int width, int height) {
    List<List<Tile>> ret = new ArrayList<>();
    List<Tile> emptyRow = new ArrayList<>();
    for (int col = 0; col < width; col++){
      emptyRow.add(null);
    }
    for (int row = 0; row < height; row++) {
      ret.add(new ArrayList<>(emptyRow));
    }
    return ret;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Tile getTile(TileCoordinates tileCoordinates) {
    return contents.get(tileCoordinates.getY()).get(tileCoordinates.getX());
  }

  public void setTile(int row, int col, Tile tile) {
    contents.get(row).set(col, tile);
  }

  @Override
  public Iterator<Tile> iterator() {
    return null;
  }
}