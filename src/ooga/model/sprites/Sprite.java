package ooga.model.sprites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ooga.model.*;
import ooga.model.api.ObservableSprite;
import ooga.model.api.SpriteEvent;
import ooga.model.api.SpriteObserver;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

import static ooga.model.api.SpriteEvent.EventType.*;

/**
 * Sprites are things that exist on top of the grid, but are not pure UI elements such as score
 * labels.
 *
 * @author George Hong
 */
public abstract class Sprite implements ObservableSprite {

  private SpriteCoordinates position;
  private Vec2 direction;
  private Map<SpriteEvent.EventType, Set<SpriteObserver>> observers;
  private String type;

  @JsonCreator
  public Sprite(
      @JsonProperty("position") SpriteCoordinates position,
      @JsonProperty("direction") Vec2 direction) {
    this.position = position;
    this.direction = direction;
    initializeObserverMap();
  }

  public Sprite(SpriteDescription description) {
    this.position = description.getCoordinates();
    this.direction = Vec2.ZERO;
  }

  @JsonCreator
  public Sprite() {
    // TODO: Verify that this is appropriate behavior for the no-arg constructor
    this.position = new SpriteCoordinates();
    this.direction = Vec2.ZERO;
    initializeObserverMap();
  }

  private void initializeObserverMap() {
    observers = new HashMap<>();
    for (SpriteEvent.EventType eventType : SpriteEvent.EventType.values()) {
      observers.put(eventType, new HashSet<>());
    }
  }

  /**
   * Removes the Sprite from the game
   */
  public void delete(PacmanGameState state) {
    state.prepareRemove(this);
  }

  /**
   * Returns the type of this Sprite
   *
   * @return
   */
  public String getType() {
    return type;
  }

  protected String setType(String newType) {
    type = newType;
    notifyObservers(TYPE_CHANGE);
  }

  // coordinates of the tile above which this sprite's center lies

  /**
   * Coordinates of this Sprite. Also provides the tile coordinates.
   *
   * @return
   */
  public SpriteCoordinates getCoordinates() {
    return position;
  }

  /**
   * Translate this sprite to a new set of coordinates.
   * <p>
   * Notifies observers.
   *
   * @param c New position.
   */
  protected void setCoordinates(SpriteCoordinates c) {
    setPosition(c.getPosition());
  }

  /**
   * Translate this sprite to a new position (passed as a vector). Notifies obserrs with a TRANSLATE
   * event.
   *
   * @param v New position.
   */
  protected void setPosition(Vec2 v) {
    position = new SpriteCoordinates(v);

    notifyObservers(TRANSLATE);
  }

  /**
   * Direction that the Sprite is facing
   *
   * @return
   */
  public Vec2 getDirection() {
    return direction;
  }

  /**
   * Change the orientation of this Sprite. Notifies observers
   *
   * @param
   */
  protected void setDirection(Vec2 direction) {
    this.direction = direction;

    notifyObservers(ROTATE);
  }

  public boolean isVisible() {
    return true;
  }

  /**
   * Allows a Sprite to detect all objects that reside in the same tile as it does.  Each of these
   * other Sprites is given the opportunity to respond to coming into contact with this Sprite.
   *
   * @param state
   */
  public void handleCollisions(PacmanGameState state) {
    List<Sprite> sprites = state.getCollidingWith(this);
    for (Sprite other : sprites) {
      this.uponHitBy(other, state);
      other.uponHitBy(this, state);
    }
  }

  /**
   * Sprites override this method to define game state changes or changes to the sprite upon coming
   * into contact with another Sprite.
   *
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   *              themselves from the game or adjust the score
   */
  public abstract void uponHitBy(Sprite other, PacmanGameState state);

  /**
   * Adds an observer that will be notified whenever any of the subset of observedEvents occurs
   *
   * @param so             observer object to add
   * @param observedEvents events that this observer listens for
   */
  public void addObserver(SpriteObserver so, SpriteEvent.EventType... observedEvents) {
    SpriteEvent.EventType[] eventsToRegister =
        observedEvents.length == 0 ? SpriteEvent.EventType.values() : observedEvents;
    for (SpriteEvent.EventType observedEvent : eventsToRegister) {
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
      observers.get(observedEvent).remove(so);
    }
  }

  /**
   * Notify each observer that the following sprite events have occurred. If multiple events occur
   * simultaneously, an observer set to receive multiple types of events receives each separately.
   *
   * @param observedEvents collection of events to notify observers of.
   */
  protected void notifyObservers(SpriteEvent.EventType... observedEvents) {
    SpriteEvent.EventType[] eventsToRegister =
        observedEvents.length == 0 ? SpriteEvent.EventType.values() : observedEvents;
    for (SpriteEvent.EventType observedEvent : eventsToRegister) {
      for (SpriteObserver observer : observers.get(observedEvent)) {
        observer.onSpriteUpdate(new SpriteEvent(this, observedEvent));
      }
    }
  }

  // advance state by dt seconds
  public abstract void step(double dt, PacmanGameState pacmanGameState);

  public abstract boolean mustBeConsumed();

  public abstract boolean isDeadlyToPacMan();
}
