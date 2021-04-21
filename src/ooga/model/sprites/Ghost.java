package ooga.model.sprites;

import ooga.model.*;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Clock;
import ooga.util.Timer;
import ooga.util.Vec2;

import static ooga.model.sprites.Ghost.GhostAnimationType.*;
import static ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType.GHOST_FRIGHTENED;
import static ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType.GHOST_FRIGHTENED_END;

/**
 * @author Matthew Belissary
 */
public abstract class Ghost extends MoveableSprite {

  private final Clock ghostClock;
  private final SpriteCoordinates spawn;
  private int baseGhostScore = 200;
  private int frightenedBank;
  private GhostBehavior ghostBehavior;
  private GhostAnimationState animationState;
  private boolean forceAnimationUpdate;

  protected Ghost(
      String spriteAnimationPrefix,
      SpriteCoordinates position,
      Vec2 direction,
      double speed) {
    super(spriteAnimationPrefix, directionToAnimationType(direction, NORMAL), position, direction,
        speed);
    spawn = position;
    swapClass = SwapClass.GHOST;
    animationState = GhostAnimationState.WAIT;
    mapAnimationStateToBehavior(animationState);
    ghostClock = new Clock();

    ghostClock.addTimer(new Timer(getInitialWaitTime(), state -> {
      if (ghostBehavior == GhostBehavior.WAIT) {
        changeAnimationState(GhostAnimationState.CHASE);
      }
    }));

    forceAnimationUpdate = false;
  }

  public Ghost(
      String spriteAnimationPrefix,
      SpriteDescription spriteDescription) {
    this(spriteAnimationPrefix,
        spriteDescription.getCoordinates(),
        new Vec2(1, 0), 1);
  }

  protected static SpriteAnimationFactory.SpriteAnimationType directionToAnimationType(
      Vec2 direction, GhostAnimationType type) {
    return switch (type) {
      case FRIGHTENED -> GHOST_FRIGHTENED;
      case FRIGHTENED_END -> GHOST_FRIGHTENED_END;
      default -> {
        String directionName = "RIGHT";
        if (direction.getMagnitude() > Vec2.EPSILON) {
          Vec2 unitDirection = direction.normalize();
          if (Vec2.RIGHT.dot(unitDirection) > 0.5) {
            directionName = "RIGHT";
          } else if (Vec2.LEFT.dot(unitDirection) > 0.5) {
            directionName = "LEFT";
          } else if (Vec2.UP.dot(unitDirection) > 0.5) {
            directionName = "UP";
          } else if (Vec2.DOWN.dot(unitDirection) > 0.5) {
            directionName = "DOWN";
          }
        }
        yield SpriteAnimationFactory.SpriteAnimationType
            .valueOf("GHOST_" + directionName + (type == EYES ? "_EYES" : ""));
      }
    };
  }

  /**
   * Resets the ghosts to their starting positions
   */
  @Override
  public void reset() {
    super.reset();
    ghostClock.clear();
    ghostClock.reset();
    changeAnimationState(GhostAnimationState.WAIT);
    ghostClock.addTimer(new Timer(getInitialWaitTime(), state -> {
      if (ghostBehavior == GhostBehavior.WAIT) {
        changeAnimationState(GhostAnimationState.CHASE);
      }
    }));
    forceAnimationUpdate = false;
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

  public SpriteCoordinates getSpawn() {
    return spawn;
  }

  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToGhosts();
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (!isDeadlyToPacMan() && isConsumable() && other.eatsGhosts()) {
      this.setMovementSpeed(this.getMovementSpeed() * 1.5);
      changeAnimationState(GhostAnimationState.EATEN);
    }
  }

  // TODO: Add other animations for: FRIGHTENED_WAIT, FRIGHTENED_WAIT_BLINKING, and FRIGHTENED_BLINKING
  private GhostAnimationType stateToAnimationType(GhostAnimationState animationState) {
    return switch (animationState) {
      case WAIT -> NORMAL;
      case FRIGHTENED_WAIT -> NORMAL;
      case FRIGHTENED_WAIT_BLINKING -> NORMAL;
      case FRIGHTENED -> FRIGHTENED;
      case FRIGHTENED_BLINKING -> FRIGHTENED;
      case EATEN -> EYES;
      case CHASE -> NORMAL;
    };
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    Vec2 oldDirection = getDirection();

    ghostClock.step(dt, pacmanGameState);
    super.step(dt, pacmanGameState);
    move(dt, pacmanGameState.getGrid());

    if (getCoordinates().getTileCoordinates().equals(getSpawn().getTileCoordinates())
        && ghostBehavior.equals(GhostBehavior.EATEN)) {
      this.setCoordinates(new SpriteCoordinates(getSpawn().getTileCenter()));
      this.setMovementSpeed(this.getMovementSpeed() * (2.0 / 3.0));
      changeAnimationState(GhostAnimationState.WAIT);
      pacmanGameState.getClock().addTimer(new Timer(0.25, mutableGameState -> {
        this.setCurrentSpeed(getMovementSpeed());
        this.changeAnimationState(GhostAnimationState.CHASE);
        setDirection(getDirection().scalarMult(-1));
      }));
    }

    handleCollisions(pacmanGameState);

    if (forceAnimationUpdate || !getDirection().equals(oldDirection)) {
      forceAnimationUpdate = false;
      setCurrentAnimationType(directionToAnimationType(getDirection(),
          stateToAnimationType(animationState)
      ));
    }
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }

  @Override
  public boolean isDeadlyToPacMan() {
    return ghostBehavior.equals(GhostBehavior.CHASE);
  }

  @Override
  public boolean eatsGhosts() {
    return false;
  }

  @Override
  public boolean isConsumable() {
    return ghostBehavior.equals(GhostBehavior.FRIGHTENED);
  }

  @Override
  public boolean hasMultiplicativeScoring() {
    return ghostBehavior.equals(GhostBehavior.FRIGHTENED);
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
    GhostAnimationType oldAnimType = stateToAnimationType(animationState);
    ghostBehavior = behavior;
    if (stateToAnimationType(animationState) != oldAnimType) {
      forceAnimationUpdate = true;
    }
  }

  private void changeAnimationState(GhostAnimationState state) {
    GhostAnimationType oldAnimType = stateToAnimationType(animationState);
    animationState = state;
    mapAnimationStateToBehavior(animationState);
    if (stateToAnimationType(animationState) != oldAnimType) {
      forceAnimationUpdate = true;
    }
  }

  private void mapAnimationStateToBehavior(GhostAnimationState state){
    switch (state) {
      case CHASE -> changeBehavior(GhostBehavior.CHASE);
      case WAIT, FRIGHTENED_WAIT_BLINKING, FRIGHTENED_WAIT -> changeBehavior(GhostBehavior.WAIT);
      case FRIGHTENED, FRIGHTENED_BLINKING -> changeBehavior(GhostBehavior.FRIGHTENED);
      case EATEN -> changeBehavior(GhostBehavior.EATEN);
    }
  }

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {
    switch (event) {
      case GHOST_SLOWDOWN_ACTIVATED -> setMovementSpeed(getMovementSpeed() * 0.5);
      case GHOST_SLOWDOWN_DEACTIVATED -> setMovementSpeed(getMovementSpeed() * 2);
      case FRIGHTEN_ACTIVATED -> {
        frightenedBank++;
        if (getGhostBehavior().equals(GhostBehavior.CHASE)) {
          changeAnimationState(GhostAnimationState.FRIGHTENED);
          //TODO: Needs timer to go for 6 sections before doing
          // changeAnimationState(GhostAnimationState.FRIGHTENED_BLINKING);
          setDirection(getDirection().scalarMult(-1));
        }
        if (getGhostBehavior().equals(GhostBehavior.CHASE)){
          changeAnimationState(GhostAnimationState.FRIGHTENED_WAIT);
          //TODO: Needs timer to go for 6 sections before doing
          // changeAnimationState(GhostAnimationState.FRIGHTENED_WAIT_BLINKING);
        }
      }
      case FRIGHTEN_DEACTIVATED -> {
        frightenedBank--;
        System.out.println(isConsumable());
        System.out.println(frightenedBank);
        if(isConsumable() && frightenedBank == 0) {
          if (!getGhostBehavior().equals(GhostBehavior.WAIT)) {
            changeAnimationState(GhostAnimationState.CHASE);
            setDirection(getDirection().scalarMult(-1));
          }
        }
      }
      case POINT_BONUS_ACTIVATED -> baseGhostScore *= 2;
      case POINT_BONUS_DEACTIVATED -> baseGhostScore *= 0.5;
    }
  }

  protected enum GhostAnimationType {
    NORMAL,
    EYES,
    FRIGHTENED,
    FRIGHTENED_END
  }

  public enum GhostBehavior {
    FRIGHTENED,
    SCATTER,
    CHASE,
    EATEN,
    WAIT
  }

  public enum GhostAnimationState {
    WAIT,
    FRIGHTENED_WAIT,
    FRIGHTENED_WAIT_BLINKING,
    FRIGHTENED,
    FRIGHTENED_BLINKING,
    CHASE,
    EATEN
  }
}
