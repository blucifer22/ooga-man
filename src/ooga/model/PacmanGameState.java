package ooga.model;

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
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.Sprite;

/**
 * This class contains all the state of a in-progress pacman game and serves as the top-level class
 * in the model.
 */
public class PacmanGameState implements SpriteExistenceObservable, GridRebuildObservable {

  private final Set<SpriteExistenceObserver> spriteExistenceObservers;
  private final Set<GridRebuildObserver> gridRebuildObservers;
  private PacmanGrid grid;
  private Collection<Sprite> sprites;

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

  public void loadSprites(List<SpriteDescription> spriteDescriptions) {
    spriteDescriptions.forEach(spriteDescription -> {
      sprites.add(spriteDescription.toSprite());
    });
  }

  // advance game state by `dt' seconds
  public void step(double dt) {
    for (Sprite sprite : getSprites()) {
      sprite.step(dt, grid);
    }
    // TODO: Refactor into separate method
    for (Sprite sprite : sprites) {
      for (Sprite otherSprite : getSprites()) {
        if (sprite != otherSprite) {
          TileCoordinates sprite1Position = sprite.getCoordinates().getTileCoordinates();
          TileCoordinates sprite2Position = otherSprite.getCoordinates().getTileCoordinates();
          if (sprite1Position.equals(sprite2Position)) {
            handleCollision(sprite, otherSprite);
          }
        }
      }
    }

    int count = 0;
    for (Sprite sprite : getSprites()) {
      if (sprite.mustBeConsumed()) {
        count++;
      }
    }
    // Next level, all consumables eaten
    if (count == 0) {
      //notifyGridRebuildObservers();
      // TODO: add some consumables and implement round progression logic
    }

  }

  private void handleCollision(Sprite movingSprite, Sprite otherSprite) {
  }

  public void addSprite(Sprite sprite){
    sprites.add(sprite);

    for(SpriteExistenceObserver obs : spriteExistenceObservers)
      obs.onSpriteCreation(sprite);
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
