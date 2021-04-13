package ooga.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ooga.model.api.GridRebuildObservable;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.PowerupEventObserver;
import ooga.model.api.SpriteExistenceObservable;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.leveldescription.GridDescription;

import java.util.Collection;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.Sprite;

/**
 * This class contains all the state of a in-progress pacman game and serves as the top-level class
 * in the model.
 */
public class PacmanGameState implements SpriteExistenceObservable, GridRebuildObservable,
    MutableGameState {

  private final Set<SpriteExistenceObserver> spriteExistenceObservers;
  private final Set<GridRebuildObserver> gridRebuildObservers;
  private final Set<PowerupEventObserver> pacmanPowerupObservers;

  private final Collection<Sprite> sprites;
  private final Set<Sprite> toDelete;
  private PacmanGrid grid;
  private int pacManScore;

  public PacmanGameState() {
    spriteExistenceObservers = new HashSet<>();
    gridRebuildObservers = new HashSet<>();
    toDelete = new HashSet<>();
    sprites = new LinkedList<>();
    pacmanPowerupObservers = new HashSet<>();
  }

  public void setDefaultInputSource() {

  }

  /**
   * @param score
   */
  @Override
  public void incrementScore(int score) {
    pacManScore += score;
  }

  @Override
  public int getScore() {
    return pacManScore;
  }

  @Override
  public void prepareRemove(Sprite sprite) {
    toDelete.add(sprite);
    notifySpriteDestruction(sprite);
  }

  public void addSpriteExistenceObserver(SpriteExistenceObserver spriteExistenceObserver) {
    spriteExistenceObservers.add(spriteExistenceObserver);
  }

  public void loadGrid(GridDescription gridDescription) {
    grid = new PacmanGrid(gridDescription);
    notifyGridRebuildObservers();
  }

  public void loadGrid(PacmanGrid grid) {
    this.grid = grid;
    notifyGridRebuildObservers();
  }

  public void loadSprites(List<SpriteDescription> spriteDescriptions) {
    spriteDescriptions.forEach(spriteDescription -> addSprite(spriteDescription.toSprite()));
  }

  // advance game state by `dt' seconds
  public void step(double dt) {
    toDelete.clear();
    for (Sprite sprite : getSprites()) {
      if (toDelete.contains(sprite)) {
        continue;
      }
      sprite.step(dt, this);
    }

    for (Sprite sprite : toDelete) {
      sprites.remove(sprite);
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
  @Override
  public List<Sprite> getCollidingWith(Sprite sprite) {
    TileCoordinates tc = sprite.getCoordinates().getTileCoordinates();
    List<Sprite> collidingSprites = new ArrayList<>();
    for (Sprite otherSprite : sprites) {
      if (sprite != otherSprite && otherSprite.getCoordinates().getTileCoordinates().equals(tc)) {
        collidingSprites.add(otherSprite);
      }
    }
    return collidingSprites;
  }

  @Override
  public void addSprite(Sprite sprite) {
    sprites.add(sprite);
    notifySpriteCreation(sprite);
  }

  public Collection<Sprite> getSprites() {
    return sprites;
  }

  @Override
  public PacmanGrid getGrid() {
    return grid;
  }

  public void advanceLevel() {
  }


  private void notifySpriteDestruction(Sprite sprite) {
    for (SpriteExistenceObserver observer : spriteExistenceObservers) {
      observer.onSpriteDestruction(sprite);
    }
  }

  private void notifySpriteCreation(Sprite sprite) {
    for (SpriteExistenceObserver observer : spriteExistenceObservers) {
      observer.onSpriteCreation(sprite);
    }
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

  /**
   * Register Sprite to be notified when powerup effects begin and end, allowing the Sprites to
   * change their behavior as required.
   *
   * @param listener
   */
  @Override
  public void registerEventListener(Sprite listener) {
    pacmanPowerupObservers.add(listener);
  }

  /**
   * Can be used by Powerup Pills to introduce an effect
   *
   * @param type
   */
  @Override
  public void notifyPowerupListeners(PacmanPowerupEvent type) {
    for (PowerupEventObserver observer : pacmanPowerupObservers) {
      observer.respondToPowerEvent(type);
    }
  }
}
