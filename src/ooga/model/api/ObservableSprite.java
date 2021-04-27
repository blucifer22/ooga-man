package ooga.model.api;

import ooga.model.grid.SpriteCoordinates;
import ooga.util.Vec2;

/**
 * Interface implemented by a sprite that can be observed.
 */
public interface ObservableSprite {
  /**
   * Get current costume.
   * @return Current costume.
   */
  String getCostume();

  /**
   * Get coordinates.
   * @return Coordinates.
   */
  SpriteCoordinates getCoordinates();

  /**
   * Get direction vector.
   * @return Direction vector.
   */
  Vec2 getDirection();

  /**
   * Query visibility.
   *
   * @return Current visibility.
   */
  boolean isVisible();

  /**
   * Add an observer.
   * @param so Observer to add.
   * @param observedEvents Which events to observe.
   */
  void addObserver(SpriteObserver so, SpriteEvent.EventType... observedEvents);

  /**
   * Remove an observer.
   * @param so Observer to remove.
   */
  void removeObserver(SpriteObserver so);
}
