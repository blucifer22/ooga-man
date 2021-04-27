package ooga.model.api;

/**
 * Interface to be implemented by an observer of a tile/
 */
public interface TileObserver {
  /**
   * Called upon a tile event
   * @param e The event that occurred.
   */
  void onTileEvent(TileEvent e);
}
