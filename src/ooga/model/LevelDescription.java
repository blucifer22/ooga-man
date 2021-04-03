package ooga.model;

import java.util.Collection;
import java.util.List;

/**
 * LevelDescription contains all of the information required to construct an ObservableGrid (AKA: A
 * Pac-Man level) including width, height, and Sprites.
 *
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class LevelDescription {
  private final int width, height;
  private final Tile grid[][];
  private Collection<Sprite> sprites;

  public LevelDescription(int width, int height, List<Tile> tileList, Collection<Sprite> sprites)
      throws IllegalArgumentException {

    this.width = width;
    this.height = height;

    this.grid = new Tile[width][height];
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            grid[i][j] = tileList.get((i * width) + j);
        }
    }

    this.sprites = sprites;
  }
}
