package ooga.model.api;

import ooga.model.grid.TileCoordinates;

/**
 * Interface to be implemented by an observable grid.
 */
public interface ObservableGrid {
  /**
   * Retrieve width.
   * @return Width.
   */
  int getWidth();

  /**
   * Retrieve height.
   * @return Height.
   */
  int getHeight();

  /**
   * Get a tile.
   * @param tileCoordinates location
   * @return Tile at that location.
   */
  ObservableTile getTile(TileCoordinates tileCoordinates);
}
