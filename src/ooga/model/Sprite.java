package ooga.model;

import ooga.controller.SpriteObserver;

/**
 * Sprites are things that exist on top of the grid, but are not pure UI elements such as score
 * labels.
 */
public abstract class Sprite implements SpriteObservable {

  abstract boolean isStationary();

  public abstract String getType();

  // coordinates of the tile above which this spirte's center lies
  public SpriteCoordinates getCoordinates() {
    return null;
  }

  public double getOrientation() {
    return 0;
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
  abstract void step(double dt);
}