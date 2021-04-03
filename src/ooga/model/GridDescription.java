package ooga.model;

import java.util.Collection;
import java.util.List;

/**
 * GridDescription contains all of the information required to construct an ObservableGrid (AKA: A
 * Pac-Man level) including width, height, and Sprites.
 *
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class GridDescription {
  private final int width;
  private final int height;
  private final Tile[][] grid;

  public GridDescription(int width, int height, List<Tile> tileList)
      throws IllegalArgumentException {

    this.width = width;
    this.height = height;

    if (tileList.size() > width * height) {
      throw new IllegalArgumentException(
          "ILLEGAL ARGUMENT EXCEPTION:\nTOO MANY TILES FOR INDICATED DIMENSIONS!");
    }

    this.grid = new Tile[width][height];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        grid[i][j] = tileList.get((i * width) + j);
      }
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Tile[][] getGrid() {
    return grid;
  }
}
