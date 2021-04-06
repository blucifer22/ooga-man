package ooga.model.sprites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ooga.model.InputSource;
import ooga.model.PacmanGrid;
import ooga.model.SpriteCoordinates;
import ooga.model.TileCoordinates;
import ooga.model.api.ObservableSprite;
import ooga.model.api.SpriteEvent;
import ooga.model.api.SpriteObserver;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/**
 * Sprites are things that exist on top of the grid, but are not pure UI elements such as score
 * labels.
 *
 * @author George Hong
 */
public abstract class Sprite implements ObservableSprite {

  private final SpriteCoordinates position;
  private Vec2 direction;
  private Map<SpriteEvent.EventType, Set<SpriteObserver>> observers;
  private InputSource inputSource;
  private double speed;

  @JsonCreator
  public Sprite(
      @JsonProperty("position") SpriteCoordinates position,
      @JsonProperty("direction") Vec2 direction,
      @JsonProperty("speed") double speed) {
    this.position = position;
    this.direction = direction;
    this.speed = speed;
    initializeObserverMap();
  }

  public Sprite(SpriteDescription description) {
    this.position = description.getCoordinates();
    this.direction = Vec2.ZERO;
    this.speed = 0;
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
   * Returns the type of this Sprite
   *
   * @return
   */
  public abstract String getType();

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
   * @param so observer object to add
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
  public void step(double dt, PacmanGrid grid){
    Vec2 userDirection = getInputSource().getRequestedDirection();
    if (getDirection().parallelTo(userDirection)) {
      setDirection(userDirection);
      // queuedDirection = userDirection;
    } else if (!userDirection.equals(Vec2.ZERO)) {
      direction = userDirection;
    }

    Vec2 centerCoordinates = getCoordinates().getTileCenter();
    Vec2 currentPosition = getCoordinates().getPosition();
    Vec2 nextPosition = currentPosition.add(getDirection().scalarMult(getSpeed()).scalarMult(dt));

    // Grid-snapping
    if (centerCoordinates.isBetween(currentPosition, nextPosition)) {
      getCoordinates().setPosition(centerCoordinates);
      TileCoordinates currentTile = getCoordinates().getTileCoordinates();
      // Tile target assuming use of queued direction
      TileCoordinates newTargetTile =
          direction == null
              ? new TileCoordinates(0, 0)
              : new TileCoordinates(
                  currentTile.getX() + (int) direction.getX(),
                  currentTile.getY() + (int) direction.getY());
      // Tile target assuming continued use of current direction
      TileCoordinates currentTargetTile =
          new TileCoordinates(
              currentTile.getX() + (int) getDirection().getX(),
              currentTile.getY() + (int) getDirection().getY());

      if (direction != null && grid.getTile(newTargetTile).isOpenToPacman()) {
        setDirection(direction);
        direction = null;
      } else if (!grid.getTile(currentTargetTile).isOpenToPacman()) {
        setDirection(Vec2.ZERO);
      }
    }

    nextPosition =
        getCoordinates().getPosition().add(getDirection().scalarMult(getSpeed()).scalarMult(dt));
    getCoordinates().setPosition(nextPosition);
  }

  public abstract boolean mustBeConsumed();

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  protected InputSource getInputSource() {
    return inputSource;
  }

  public void setInputSource(InputSource s) {
    inputSource = s;
  }
}
