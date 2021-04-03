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

  /**
   * The general constructor for GridDescription. Takes in a width, a height, and a 2D array of
   * Tiles with which to initially populate itself.
   *
   * @param width The width of the Grid that this GridDescription represents.
   * @param height The height of the Grid that this GridDescription represents.
   * @param tileList The List of Tiles that compose the Grid that this GridDescription represents.
   * @throws IllegalArgumentException If the wrong number of Tiles are provided
   */
  public GridDescription(int width, int height, List<Tile> tileList)
      throws IllegalArgumentException {

    this.width = width;
    this.height = height;

    if (tileList.size() != width * height) {
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

  /**
   * Get the width of this GridDescription.
   *
   * @return The width of this GridDescription.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get the height of this GridDescription.
   *
   * @return The height of this GridDescription.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Get the 2D array of Tiles that make up this GridDescription
   *
   * @return A 2D array of Tiles that make up this GridDescription
   */
  public Tile[][] getGrid() {
    return grid;
  }
}
