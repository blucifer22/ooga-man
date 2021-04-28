package ooga.model.api;

import ooga.model.api.TileEvent.EventType;
import ooga.model.grid.TileCoordinates;

/**
 * Interface implemented by a tile that can be observed.
 *
 * @author David Coffman
 */
public interface ObservableTile {

  /**
   * Retrieve this tile's coordinates.
   * @return coordinates.
   */
  TileCoordinates getCoordinates();

  /**
   * Get the costume of this tile.
   * @return Costume, as a string.
   */
  String getType();

  /**
   * Add a tile observer to this tile.
   * @param observer Observer.
   * @param events Which events this observer accepts.
   */
  void addTileObserver(TileObserver observer, EventType... events);
}
