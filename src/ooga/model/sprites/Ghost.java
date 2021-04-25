package ooga.model.sprites;

import java.util.Map;
import java.util.Set;

import ooga.model.*;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Clock;
import ooga.util.Timer;
import ooga.util.Vec2;

import static ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType.GHOST_FRIGHTENED;
import static ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType.GHOST_FRIGHTENED_END;

/**
 * @author Matthew Belissary
 */
public abstract class Ghost extends MoveableSprite {

  private final Clock ghostClock;

  private final double defaultMoveSpeed;
  private final SpriteCoordinates spawn;
  private int baseGhostScore = 200;
  private int frightenedBank;
  private GhostState currentState;
  private boolean forceAnimationUpdate;

  private static final GhostState INITIAL_STATE = GhostState.WAIT;

  // TODO: Delete "protected" to make Ghost classes package private
  protected Ghost(
      String spriteAnimationPrefix,
      SpriteCoordinates position,
      Vec2 direction,
      double speed) {
    super(spriteAnimationPrefix,
          directionToAnimationType(direction, stateToAnimationType(INITIAL_STATE)), position, direction,
        speed);
    spawn = position;
    swapClass = SwapClass.GHOST;
    currentState = INITIAL_STATE;
    defaultMoveSpeed = speed;
    ghostClock = new Clock();

    powerupOptions.putAll(Map
        .of(GameEvent.FRIGHTEN_ACTIVATED, this::activateFrightened,
            GameEvent.FRIGHTEN_DEACTIVATED, this::deactivateFrightened,
            GameEvent.GHOST_SLOWDOWN_ACTIVATED, () -> setMovementSpeed(getMovementSpeed() * 0.5),
            GameEvent.GHOST_SLOWDOWN_DEACTIVATED, () -> setMovementSpeed(getMovementSpeed() * 2),
            GameEvent.POINT_BONUS_ACTIVATED, () -> baseGhostScore *= 2,
            GameEvent.POINT_BONUS_DEACTIVATED, () -> baseGhostScore *= 0.5,
            GameEvent.FRIGHTEN_WARNING, () -> {
              changeState(switch(currentState) {
                  case FRIGHTENED -> GhostState.FRIGHTENED_BLINKING;
                  case FRIGHTENED_WAIT -> GhostState.FRIGHTENED_WAIT_BLINKING;
                  default -> currentState;
                });
            },
            GameEvent.PACMAN_DEATH, () -> freeze(),
            GameEvent.SPRITES_UNFROZEN, () -> {
                  ghostClock.addTimer(new Timer(getInitialWaitTime(), this::waitTimerExpired));
                  unfreeze();
                }
            ));

    forceAnimationUpdate = false;
  }

  private void waitTimerExpired(MutableGameState gameState) {
    GhostState nextState = switch (currentState) {
      case FRIGHTENED_WAIT -> GhostState.FRIGHTENED;
      case FRIGHTENED_WAIT_BLINKING -> GhostState.FRIGHTENED_BLINKING;
      default -> GhostState.CHASE; // includes normal WAIT -> CHASE transition
    };
    changeState(nextState);
    this.setMovementSpeed(defaultMoveSpeed);
  }

  public Ghost(
      String spriteAnimationPrefix,
      SpriteDescription spriteDescription) {
    this(spriteAnimationPrefix,
        spriteDescription.getCoordinates(),
        new Vec2(1, 0), 1);
  }

  //TODO: Ask if i can move this code
  protected static SpriteAnimationFactory.SpriteAnimationType directionToAnimationType(
      Vec2 direction, GhostAnimationType type) {
    return switch (type) {
      case ANIM_FRIGHTENED -> GHOST_FRIGHTENED;
      case ANIM_FRIGHTENED_BLINKING -> GHOST_FRIGHTENED_END;
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
            .valueOf(
                "GHOST_" + directionName + (type == GhostAnimationType.ANIM_EYES ? "_EYES" : ""));
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
    changeState(GhostState.WAIT);
    unfreeze();
    ghostClock.addTimer(new Timer(getInitialWaitTime(), this::waitTimerExpired));
  }

  // TODO: Delete "protected" to make Ghost classes package private

  /**
   * Defines how long this ghost takes before leaving the pen at the start of the game
   *
   * @return
   */
  protected double getInitialWaitTime() {
    return 0;
  }

  public double getDefaultMoveSpeed() {
    return defaultMoveSpeed;
  }

  /**
   * Gets the current state of the ghost
   *
   * @return
   */
  public GhostBehavior getGhostBehavior() {
    return switch (currentState) {
      case CHASE -> GhostBehavior.CHASE;
      case WAIT, FRIGHTENED_WAIT_BLINKING, FRIGHTENED_WAIT -> GhostBehavior.WAIT;
      case FRIGHTENED, FRIGHTENED_BLINKING -> GhostBehavior.RUNAWAY;
      case EATEN -> GhostBehavior.EATEN;
    };
  }

  public SpriteCoordinates getSpawn() {
    return spawn;
  }

  // TODO: Delete "protected" to make Ghost classes package private
  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToGhosts();
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (!isDeadlyToPacMan() && isConsumable() && other.eatsGhosts()) {
      state.getAudioManager().playSound("ghost-eaten");
      state.getAudioManager().pushNewAmbience("ghost-eyes");
      this.setMovementSpeed(this.getMovementSpeed() * 1.5);
      changeState(GhostState.EATEN);
    }
  }

  // TODO: Add other animations for: FRIGHTENED_WAIT, FRIGHTENED_WAIT_BLINKING, and FRIGHTENED_BLINKING
  private static GhostAnimationType stateToAnimationType(GhostState currentState) {
    return switch (currentState) {
      case WAIT, CHASE -> GhostAnimationType.ANIM_NORMAL;
      case FRIGHTENED_WAIT, FRIGHTENED -> GhostAnimationType.ANIM_FRIGHTENED;
      case FRIGHTENED_WAIT_BLINKING, FRIGHTENED_BLINKING -> GhostAnimationType.ANIM_FRIGHTENED_BLINKING;
      case EATEN -> GhostAnimationType.ANIM_EYES;
    };
  }

  private void updateAnimationState() {
    setCurrentAnimationType(
        directionToAnimationType(getDirection(),
                                 stateToAnimationType(currentState)));

  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    Vec2 oldDirection = getDirection();

    ghostClock.step(dt, pacmanGameState);
    super.step(dt, pacmanGameState);
    move(dt, pacmanGameState.getGrid());

    if (getCoordinates().getTileCoordinates().equals(getSpawn().getTileCoordinates())
        && getGhostBehavior() == GhostBehavior.EATEN) {
      this.setCoordinates(new SpriteCoordinates(getSpawn().getTileCenter()));
      this.setMovementSpeed(this.getMovementSpeed() * (2.0 / 3.0));
      changeState(GhostState.WAIT);
      pacmanGameState.getAudioManager().popAmbience();
      waitTimerExpired(pacmanGameState);
    }

    handleCollisions(pacmanGameState);

    if (forceAnimationUpdate || !getDirection().equals(oldDirection)) {
      forceAnimationUpdate = false;
      updateAnimationState();
    }
  }

  @Override
  public boolean isDeadlyToPacMan() {
    return getGhostBehavior() == GhostBehavior.CHASE;
  }

  @Override
  public boolean isConsumable() {
    return Set.of(GhostState.FRIGHTENED,
        GhostState.FRIGHTENED_BLINKING,
        GhostState.FRIGHTENED_WAIT,
        GhostState.FRIGHTENED_WAIT_BLINKING).contains(currentState);
  }

  @Override
  public boolean hasMultiplicativeScoring() {
    return true;
  }

  @Override
  public int getScore() {
    return baseGhostScore;
  }

  private void changeState(GhostState state) {
    System.out.printf("Ghost %s state transition: %s -> %s\n",
        this.getCurrentAnimation().getCurrentCostume(),
        currentState.toString(), state.toString());

    GhostAnimationType oldAnimType = stateToAnimationType(currentState);
    currentState = state;
    if (stateToAnimationType(currentState) != oldAnimType) {
      forceAnimationUpdate = true;
    }
  }

  private void deactivateFrightened() {
    frightenedBank--;
    if (isConsumable() && frightenedBank == 0) {
      if (!getGhostBehavior().equals(GhostBehavior.WAIT)) {
        changeState(GhostState.CHASE);
        setDirection(getDirection().scalarMult(-1));
      }
    }
  }

  private void activateFrightened() {
    frightenedBank++;
    if (getGhostBehavior().equals(GhostBehavior.CHASE)) {
      changeState(GhostState.FRIGHTENED);
      setDirection(getDirection().scalarMult(-1));
    }
    if (getGhostBehavior().equals(GhostBehavior.WAIT)) {
      changeState(GhostState.FRIGHTENED_WAIT);
    }
  }

  // TODO: Delete "protected" to make Ghost classes package private
  // TODO: Make each ENUM its own class
  private enum GhostAnimationType {
    ANIM_NORMAL,
    ANIM_EYES,
    ANIM_FRIGHTENED,
    ANIM_FRIGHTENED_BLINKING
  }

  public enum GhostBehavior {
    RUNAWAY,
    SCATTER,
    CHASE,
    EATEN,
    WAIT
  }

  private enum GhostState {
    WAIT,
    FRIGHTENED_WAIT,
    FRIGHTENED_WAIT_BLINKING,
    FRIGHTENED,
    FRIGHTENED_BLINKING,
    CHASE,
    EATEN
  }
}
