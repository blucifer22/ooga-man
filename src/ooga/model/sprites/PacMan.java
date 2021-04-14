package ooga.model.sprites;

import ooga.model.*;
import ooga.model.api.PowerupEventObserver;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.FreeRunningPeriodicAnimation;
import ooga.model.sprites.animation.PeriodicAnimation;
import ooga.util.Vec2;

import java.util.List;

/**
 * @author George Hong
 */
public class PacMan extends MoveableSprite {

  public static final String TYPE = "pacman_halfopen";
  private int ghostsEaten;

  public PacMan(SpriteCoordinates position, Vec2 direction, double speed) {
    super(new FreeRunningPeriodicAnimation(List.of("pacman_closed", "pacman_halfopen", "pacman_open"),
            PeriodicAnimation.FrameOrder.TRIANGLE, 1/15.0),
            position, direction, speed);
    swapClass = SwapClass.PACMAN;
  }

  public PacMan(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(),
            new Vec2(1,0), 5.0);
  }

  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToPacman();
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.isDeadlyToPacMan()) {
      delete(state);
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
    super.step(dt, pacmanGameState);
    move(dt, pacmanGameState.getGrid());
    handleCollisions(pacmanGameState);

    getCurrentAnimation().setPaused(getCurrentSpeed() == 0);
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
      case SPEED_UP_ACTIVATED -> {
        setMovementSpeed(getMovementSpeed() * 2);
        getCurrentAnimation().setRelativeSpeed(getCurrentAnimation().getRelativeSpeed() * 2);
      }
      case SPEED_UP_DEACTIVATED -> {
        setMovementSpeed(getMovementSpeed() * 0.5);
        getCurrentAnimation().setRelativeSpeed(getCurrentAnimation().getRelativeSpeed() * 0.5);
      }
    }
  }
}
