package ooga.model.sprites;

import java.util.HashMap;
import java.util.Map;
import ooga.model.*;
import ooga.model.api.PowerupEventObserver;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.FreeRunningPeriodicAnimation;
import ooga.model.sprites.animation.PeriodicAnimation;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

import java.util.List;

/**
 * @author George Hong
 */
public class PacMan extends MoveableSprite {

  public static final String TYPE = "pacman_halfopen";
  private int ghostsEaten;

  public PacMan(SpriteCoordinates position, Vec2 direction, double speed) {
    super("pacman",
        SpriteAnimationFactory.SpriteAnimationType.PACMAN_CHOMP,
        position, direction, speed);
    swapClass = SwapClass.PACMAN;
    powerupOptions = Map
        .of(PacmanPowerupEvent.SPEED_UP_ACTIVATED, this::activateSpeedUp,
            PacmanPowerupEvent.SPEED_UP_DEACTIVATED, this::deactivateSpeedUp);
  }

  public PacMan(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(),
        new Vec2(1, 0), 5.0);
  }

  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToPacman();
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.isDeadlyToPacMan()) {
      state.isPacmanDead(true);
    } else if (other.hasMultiplicativeScoring()) {
      ghostsEaten++;
      state.incrementScore(other.getScore() * ghostsEaten);
      System.out.println("SCORE: " + state.getScore());
    } else if (other.isConsumable()) {
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
  public boolean eatsGhosts() {
    return true;
  }

  @Override
  public int getScore() {
    return 0;
  }

  private void deactivateSpeedUp() {
    setMovementSpeed(getMovementSpeed() * 0.5);
    getCurrentAnimation().setRelativeSpeed(getCurrentAnimation().getRelativeSpeed() * 0.5);
  }

  private void activateSpeedUp() {
    setMovementSpeed(getMovementSpeed() * 2);
    getCurrentAnimation().setRelativeSpeed(getCurrentAnimation().getRelativeSpeed() * 2);
  }
}
