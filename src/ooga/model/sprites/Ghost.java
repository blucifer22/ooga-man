package ooga.model.sprites;

import static ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType.GHOST_FRIGHTENED;
import static ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType.GHOST_FRIGHTENED_END;

import java.util.Map;
import java.util.Set;
import ooga.model.GameEvent;
import ooga.model.MutableGameState;
import ooga.model.api.InputSource;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.grid.Tile;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Clock;
import ooga.util.Timer;
import ooga.util.Vec2;

/**
 * @author Matthew Belissary
 */
public abstract class Ghost extends MoveableSprite {

  protected static final double DEFAULT_SPEED = 5.0;
  private static final GhostState INITIAL_STATE = GhostState.WAIT;
  private static final double EYES_SPEEDUP = 2.0;
  private final Clock ghostClock;
  private final double defaultMoveSpeed;
  private final SpriteCoordinates spawn;
  private int baseGhostScore = 200;
  private int frightenedBank;
  private GhostState currentState;
  private boolean forceAnimationUpdate;

  /**
   * Constructs Ghost object via parameters in JSON as well as speed
   *
   * @param spriteAnimationPrefix
   * @param position
   * @param direction
   * @param speed
   */
  protected Ghost(
      String spriteAnimationPrefix,
      SpriteCoordinates position,
      Vec2 direction,
      double speed) {
    super(spriteAnimationPrefix,
          directionToAnimationType(direction, GhostAnimationType.ANIM_NORMAL), position, direction,
        speed);
    spawn = position;
    setSwapClass(SwapClass.GHOST);
    currentState = INITIAL_STATE;
    defaultMoveSpeed = speed;
    ghostClock = new Clock();

    addPowerUpOptions(Map
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

  /**
   * Constructs Ghost from a sprite description
   *
   * @param spriteAnimationPrefix
   * @param spriteDescription
   */
  public Ghost(
      String spriteAnimationPrefix,
      SpriteDescription spriteDescription) {
    this(spriteAnimationPrefix,
        spriteDescription.getCoordinates(),
        new Vec2(1, 0), 1);
  }

  /**
   * Changes animation sprite depending on which direction the ghost is travelling
   *
   * @param direction
   * @param type
   * @return
   */
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
                    "GHOST_" + directionName + switch(type) {
                      case ANIM_EYES -> "_EYES";
                      case ANIM_PLAYER_BLINKING -> "_PLAYER";
                      default -> "";
                    });
      }
    };
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


  /**
   * Defines how long this ghost takes before leaving the pen at the start of the game
   *
   * @return
   */
  protected double getInitialWaitTime() {
    return 0;
  }

  /**
   * @return the default movement speed of the ghost
   */
  public double getDefaultMoveSpeed() {
    return defaultMoveSpeed;
  }

  /**
   * @return the current state of the ghost
   */
  public GhostBehavior getGhostBehavior() {
    return switch (currentState) {
      case CHASE -> GhostBehavior.CHASE;
      case WAIT, FRIGHTENED_WAIT_BLINKING, FRIGHTENED_WAIT -> GhostBehavior.WAIT;
      case FRIGHTENED, FRIGHTENED_BLINKING -> GhostBehavior.RUNAWAY;
      case EATEN -> GhostBehavior.EATEN;
    };
  }

  /**
   * @return spawn coordinates of the ghost
   */
  public SpriteCoordinates getSpawn() {
    return spawn;
  }

  /**
   * @param tile
   * @return true if ghost can move into the given tile
   */
  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToGhosts();
  }

  /**
   * Determines what a ghost should do when interacting with other sprites
   *
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   */
  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (!isDeadlyToPacMan() && isConsumable() && other.eatsGhosts()) {
      state.getAudioManager().playSound("ghost-eaten");
      state.getAudioManager().pushNewAmbience("ghost-eyes");
      this.setMovementSpeed(this.getMovementSpeed() * EYES_SPEEDUP);
      changeState(GhostState.EATEN);
    }
  }

  private boolean isPlayerControlled() {
    return getInputSource().isHumanControlled();
  }

  private GhostAnimationType stateToAnimationType(GhostState currentState) {
    if(isPlayerControlled() && (currentState != GhostState.EATEN))
      return GhostAnimationType.ANIM_PLAYER_BLINKING;

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

  /**
   * Moves the ghost through a frame (1/60 of a second) in the gamestate
   *
   * @param dt
   * @param pacmanGameState
   */
  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    Vec2 oldDirection = getDirection();

    ghostClock.step(dt, pacmanGameState);
    super.step(dt, pacmanGameState);
    move(dt, pacmanGameState.getGrid());

    if (getCoordinates().getTileCoordinates().equals(getSpawn().getTileCoordinates())
        && getGhostBehavior() == GhostBehavior.EATEN) {
      this.setCoordinates(new SpriteCoordinates(getSpawn().getTileCenter()));
      this.setMovementSpeed(this.getMovementSpeed() / EYES_SPEEDUP);
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

  /**
   * @return true if ghost will kill PacMan on contact
   */
  @Override
  public boolean isDeadlyToPacMan() {
    return getGhostBehavior() == GhostBehavior.CHASE;
  }

  /**
   * @return true if ghost is in a state in which it can be consumed by PacMan
   */
  @Override
  public boolean isConsumable() {
    return Set.of(GhostState.FRIGHTENED,
        GhostState.FRIGHTENED_BLINKING,
        GhostState.FRIGHTENED_WAIT,
        GhostState.FRIGHTENED_WAIT_BLINKING).contains(currentState);
  }

  /**
   * Sets input source of the ghost and updates in animation state (when swapping ghosts)
   *
   * @param s
   */
  @Override
  public void setInputSource(InputSource s) {
    super.setInputSource(s);
    forceAnimationUpdate = true;
  }

  /**
   * @return true since ghost score is multiplicative based on how many have been consumed
   */
  @Override
  public boolean hasMultiplicativeScoring() {
    return true;
  }

  /**
   * @return the base score for consuming a ghost
   */
  @Override
  public int getScore() {
    return baseGhostScore;
  }

  private void changeState(GhostState state) {
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

  private enum GhostAnimationType {
    ANIM_NORMAL,
    ANIM_PLAYER_BLINKING,
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
