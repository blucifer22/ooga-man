package ooga.model.api;

/**
 * Interface for objects which observe sprites.
 */
public interface SpriteObserver {

  /**
   * Called on a sprite event.
   * @param e Event that occurred.
   */
  void onSpriteUpdate(SpriteEvent e);
}
