package ooga.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.model.SpriteEvent.EventType;
import ooga.util.Vec2;

/**
 * Sprites are things that exist on top of the grid, but are not pure UI elements such as score
 * labels.
 *
 * @author George Hong
 */
public abstract class Sprite implements SpriteObservable {

  private final SpriteCoordinates position;
  private Vec2 direction;
  private Map<SpriteEvent.EventType, List<SpriteObserver>> observers;

  public Sprite(SpriteCoordinates position, Vec2 direction) {
    this.position = position;
    this.direction = direction;
  }

  public Sprite() {
    // TODO: Verify that this is appropriate behavior for the no-arg constructor
    this.position = new SpriteCoordinates();
    this.direction = Vec2.ZERO;
    observers = new HashMap<>();
    for (SpriteEvent.EventType eventType : SpriteEvent.EventType.values()) {
      observers.put(eventType, new ArrayList<>());
    }
  }

  /**
   * Returns whether this Sprite moves over the course of the game
   *
   * @return
   */
  public abstract boolean isStationary();

  /**
   * Returns the type of this Sprite
   *
   * @return
   */
  public abstract String getType();

  // coordinates of the tile above which this sprite's center lies

  /**
   * Coordinates of this Sprite.  Also provides the tile coordinates.
   *
   * @return
   */
  public SpriteCoordinates getCoordinates() {
    return position;
  }

  /**
   * Direction that the Sprite is facing
   *
   * @return
   */
  public Vec2 getDirection() {
    return direction;
  }

  public void setDirection(Vec2 direction) {
    this.direction = direction;
  }

  public boolean isVisible() {
    return false;
  }

  // Observation

  /**
   * Adds an observer that will be notified whenever any of the subset of observedEvents occurs
   *
   * @param so             observer object to add
   * @param observedEvents events that this observer listens for
   */
  public void addObserver(SpriteObserver so, SpriteEvent.EventType... observedEvents) {
    for (SpriteEvent.EventType observedEvent : observedEvents) {
      observers.get(observedEvent).add(so);
    }
  }

  /**
   * Removes an observer from listening to any event of this Sprite
   *
   * @param so observer object to remove
   */
  public void removeObserver(SpriteObserver so) {
    for (SpriteEvent.EventType observedEvent : observers.keySet()) {
      while (observers.get(observedEvent).remove(so)) {
      }
    }
  }

  /**
   * Notify each observer that the following sprite events have occurred.  If multiple events occur
   * simultaneously, an observer set to receive multiple types of events receives each separately.
   *
   * @param observedEvents collection of events to notify observers of.
   */
  protected void notifyObservers(SpriteEvent.EventType... observedEvents) {
    for (SpriteEvent.EventType observedEvent : observedEvents) {
      for (SpriteObserver observer : observers.get(observedEvent)) {
        observer.onSpriteUpdate(new SpriteEvent(this, observedEvent));
      }
    }
  }

  // advance state by dt seconds
  public abstract void step(double dt);

  public abstract boolean mustBeConsumed();
}
