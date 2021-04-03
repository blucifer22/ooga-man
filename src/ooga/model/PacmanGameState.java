package ooga.model;

import java.util.Collection;

/**
 * This class contains all the state of a in-progress pacman game and serves as the top-level class
 * in the model.
 */
public class PacmanGameState {

  private PacmanGrid grid;
  private Collection<Sprite> sprites;
  private int pacManScore;

  public PacmanGameState() {
    //this.grid = new PacmanGrid();
  }

  public void addExistenceObserver(SpriteExistenceObserver spriteExistenceObserver) {

  }

  // advance game state by `dt' seconds
  public void step(double dt) {

  }

  public Collection<Sprite> getSprites() {
    return null;
  }

  public Collection<Sprite> getMovingSprites() {
    return null;
  }

  public Collection<Sprite> getStationarySprites() {
    return null;
  }

  public PacmanGrid getGrid() {
    return null;
  }

  public void advanceLevel() {
  }
}
