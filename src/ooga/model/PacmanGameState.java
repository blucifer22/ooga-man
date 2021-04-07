package ooga.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import ooga.model.api.GridRebuildObservable;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.SpriteExistenceObservable;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.leveldescription.GridDescription;

import java.util.Collection;
import ooga.model.sprites.Sprite;

/**
 * This class contains all the state of a in-progress pacman game and serves as the top-level class
 * in the model.
 */
public class PacmanGameState implements SpriteExistenceObservable, GridRebuildObservable {

  private final Set<SpriteExistenceObserver> spriteExistenceObservers;
  private final Set<GridRebuildObserver> gridRebuildObservers;
  private PacmanGrid grid;
  private final Collection<Sprite> sprites;

  private int pacManScore;

  public PacmanGameState() {
    spriteExistenceObservers = new HashSet<>();
    gridRebuildObservers = new HashSet<>();
    sprites = new LinkedList<>();
  }

  public void setDefaultInputSource() {

  }

  public void addSpriteExistenceObserver(SpriteExistenceObserver spriteExistenceObserver) {
    spriteExistenceObservers.add(spriteExistenceObserver);
  }

  public void loadGrid(GridDescription gridDescription) {
    grid = new PacmanGrid(gridDescription);

    notifyGridRebuildObservers();
  }

  // advance game state by `dt' seconds
  public void step(double dt) {
    for (Sprite sprite : getSprites()) {
      sprite.step(dt, grid, this);
    }

    // Next level, all consumables eaten
    if (getRemainingConsumablesCount() == 0) {
      //notifyGridRebuildObservers();
      // TODO: add some consumables and implement round progression logic
    }

  }

  private int getRemainingConsumablesCount() {
    int count = 0;
    for (Sprite sprite : getSprites()) {
      if (sprite.mustBeConsumed()) {
        count++;
      }
    }
    return count;
  }

  /**
   * Gets the list of other Sprites that resides in the same list as this sprite
   *
   * @param sprite
   * @return
   */
  public List<Sprite> getCollidingWith(Sprite sprite) {
    TileCoordinates tc = sprite.getCoordinates().getTileCoordinates();
    List<Sprite> collidingSprites = new ArrayList<>();
    for (Sprite otherSprite : sprites) {
      if (sprite != otherSprite && sprite.getCoordinates().getTileCoordinates().equals(tc)) {
        collidingSprites.add(sprite);
      }
    }
    return collidingSprites;
  }

  private void handleCollision(Sprite movingSprite, Sprite otherSprite) {
  }

  public void addSprite(Sprite sprite) {
    sprites.add(sprite);

    for (SpriteExistenceObserver obs : spriteExistenceObservers) {
      obs.onSpriteCreation(sprite);
    }
  }

  public Collection<Sprite> getSprites() {
    return sprites;
  }

  public PacmanGrid getGrid() {
    return grid;
  }

  public void advanceLevel() {
  }


  private void notifyGridRebuildObservers() {
    for (GridRebuildObserver observers : gridRebuildObservers) {
      observers.onGridRebuild(grid);
    }
  }


  @Override
  public void addGridRebuildObserver(GridRebuildObserver observer) {
    gridRebuildObservers.add(observer);
  }
}
