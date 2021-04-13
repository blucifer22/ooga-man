package ooga.model.sprites;

import ooga.model.*;
import ooga.model.api.PowerupEventObserver;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/**
 * @author George Hong
 */
public class PacMan extends MoveableSprite {

  public static final String TYPE = "pacman_halfopen";
  private int ghostsEaten;

  public PacMan(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
    swapClass = SwapClass.PACMAN;
  }

  public PacMan(SpriteDescription spriteDescription) {
    super(spriteDescription);
  }

  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToPacman();
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.isDeadlyToPacMan()) {
      state.prepareRemove(this);
      System.out.println("GAMEOVER");
    } else if (other.hasMultiplicativeScoring()) {
      ghostsEaten++;
      state.incrementScore(other.getScore() * ghostsEaten);
      System.out.println("SCORE: " + state.getScore());
    } else if (other.isConsumable()){
      state.incrementScore(other.getScore());
      System.out.println("SCORE: " + state.getScore());
    }
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    move(dt, pacmanGameState.getGrid());
    handleCollisions(pacmanGameState);
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }

  @Override
  public boolean isDeadlyToPacMan() {
    return false;
  }

  @Override
  public boolean eatsGhosts() {
    return true;
  }

  @Override
  public boolean isConsumable() {
    return false;
  }

  @Override
  public boolean hasMultiplicativeScoring() {
    return false;
  }

  @Override
  public int getScore() {
    return 0;
  }

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {
    switch (event){
      case SPEED_UP_ACTIVATED -> setMovementSpeed(getMovementSpeed() * 2);
      case SPEED_UP_DEACTIVATED -> setMovementSpeed(getMovementSpeed() * 0.5);
    }
  }
}
