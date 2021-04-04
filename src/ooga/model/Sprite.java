package ooga.model;

import ooga.util.Vec2;

/**
 * Sprites are things that exist on top of the grid, but are not pure UI elements such as score
 * labels.
 *
 * @author George Hong
 */
public abstract class Sprite implements SpriteObservable {

  private SpriteCoordinates position;
  private Vec2 direction;

  public Sprite(SpriteCoordinates position, Vec2 direction){
    this.position = position;
    this.direction = direction;
  }

  public Sprite () {
    // TODO: Verify that this is appropriate behavior for the no-arg constructor
    this.position = new SpriteCoordinates();
    this.direction = Vec2.ZERO;
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

  public boolean isVisible() {
    return false;
  }

  // Observation
  public void addObserver(SpriteObserver so, SpriteEvent.EventType... observedEvents) {

  }

  public void removeObserver(SpriteObserver so) {

  }

  // advance state by dt seconds
  public abstract void step(double dt);

  public abstract boolean mustBeConsumed();
}
