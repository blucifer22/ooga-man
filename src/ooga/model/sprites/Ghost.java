package ooga.model.sprites;

import ooga.model.*;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.ObservableAnimation;
import ooga.util.Clock;
import ooga.util.Timer;
import ooga.util.Vec2;

/**
 * @author Matthew Belissary
 */
public abstract class Ghost extends MoveableSprite {

  public static final String TYPE = "ghost";
  private final Clock ghostClock;
  private boolean isDeadly = true;
  private int baseGhostScore = 200;
  private GhostBehavior ghostBehavior;

  protected Ghost(ObservableAnimation animation,
      SpriteCoordinates position,
      Vec2 direction,
      double speed) {
    super(animation, position, direction, speed);
    swapClass = SwapClass.GHOST;
    ghostBehavior = GhostBehavior.WAIT;
    ghostClock = new Clock();

    ghostClock.addTimer(new Timer(getInitialWaitTime(), state -> {
      ghostBehavior = GhostBehavior.CHASE;
    }));
  }

  public Ghost(ObservableAnimation animation,
      SpriteDescription spriteDescription) {
    this(animation,
        spriteDescription.getCoordinates(),
        new Vec2(1, 0), 1);
  }

  /**
   * Defines how long this ghost takes before leaving the pen at the start of the game
   *
   * @return
   */
  protected double getInitialWaitTime() {
    return 0;
  }

  /**
   * Gets the current state of the ghost
   *
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
    if (!isDeadly && other.eatsGhosts()) {
      state.prepareRemove(this);
      changeBehavior(GhostBehavior.EATEN);
    }
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    ghostClock.step(dt, pacmanGameState);
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
    return !isDeadly;
  }

  @Override
  public boolean hasMultiplicativeScoring() {
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
