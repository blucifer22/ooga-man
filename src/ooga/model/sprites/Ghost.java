package ooga.model.sprites;

import ooga.model.*;
import ooga.model.api.PowerupEventObserver;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.ObservableAnimation;
import ooga.util.Vec2;

/**
 * @author Matthew Belissary
 */
public abstract class Ghost extends MoveableSprite {

  public static final String TYPE = "ghost";
  private boolean isDeadly = true;
  private boolean isEaten;
  private int baseGhostScore = 200;
  private GhostBehavior ghostBehavior;

  protected Ghost(ObservableAnimation animation,
                  SpriteCoordinates position,
                  Vec2 direction,
                  double speed) {
    super(animation, position, direction, speed);
    swapClass = SwapClass.GHOST;
    ghostBehavior = GhostBehavior.CHASE;
  }

  public Ghost(ObservableAnimation animation,
               SpriteDescription spriteDescription) {
    this(animation,
         spriteDescription.getCoordinates(),
         new Vec2(1,0), 1);
  }

  /**
   * Gets the current state of the ghost
   * @return
   */
  public GhostBehavior getGhostBehavior() {
    return ghostBehavior;
  }

  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToGhosts();
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (!isDeadly && !isEaten && other.eatsGhosts()){
      this.setMovementSpeed(this.getMovementSpeed() * 2);
      changeBehavior(GhostBehavior.EATEN);
      isEaten = true;
    }
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    super.step(dt, pacmanGameState);
    move(dt, pacmanGameState.getGrid());
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }

  @Override
  public boolean isDeadlyToPacMan() {
    return isDeadly;
  }

  @Override
  public boolean eatsGhosts() {
    return false;
  }

  @Override
  public boolean isConsumable() {
    return !isDeadly && !isEaten;
  }

  @Override
  public boolean hasMultiplicativeScoring() {
    return true;
  }

  @Override
  public boolean isRespawnTarget() {
    return false;
  }

  @Override
  public int getScore() {
    return baseGhostScore;
  }

  private void changeBehavior(GhostBehavior behavior) {
    ghostBehavior = behavior;
  }

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {
    switch (event) {
      case GHOST_SLOWDOWN_ACTIVATED -> setMovementSpeed(getMovementSpeed() * 0.5);
      case GHOST_SLOWDOWN_DEACTIVATED -> setMovementSpeed(getMovementSpeed() * 2);
      case FRIGHTEN_ACTIVATED -> {
        changeBehavior(GhostBehavior.FRIGHTENED);
        isDeadly = false;
        setDirection(getDirection().scalarMult(-1));
      }
      case FRIGHTEN_DEACTIVATED -> {
        changeBehavior(GhostBehavior.CHASE);
        isDeadly = true;
        setDirection(getDirection().scalarMult(-1));
      }
      case POINT_BONUS_ACTIVATED -> baseGhostScore *= 2;
      case POINT_BONUS_DEACTIVATED -> baseGhostScore *= 0.5;
    }
  }

  /* TODO: perhaps refactor? */
  public enum GhostBehavior {
    FRIGHTENED,
    SCATTER,
    CHASE,
    EATEN,
    WAIT
  }
}
