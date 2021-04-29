package ooga.model.sprites;

import java.util.Map;
import ooga.model.GameEvent;
import ooga.model.MutableGameState;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.grid.Tile;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Timer;
import ooga.util.Vec2;

/**
 * Pac-Man Sprite that the player controls and navigates the game with.  This class encodes many of
 * the properties for the player to play the game with.
 *
 * @author George Hong
 */
public class PacMan extends MoveableSprite {

  private static final double INITIAL_FREEZE_DURATION = 4.2; // length of the starting sound
  private int ghostsEaten;
  private int dotsEaten;

  private PacmanState currentState;

  /**
   * Construct a Pac-Man sprite.
   *
   * @param position  Initial position.
   * @param direction Initial direction.
   * @param speed     Movement speed.
   */
  public PacMan(SpriteCoordinates position, Vec2 direction, double speed) {
    super("pacman",
        SpriteAnimationFactory.SpriteAnimationType.PACMAN_CHOMP,
        position, direction, speed);
    setSwapClass(SwapClass.PACMAN);
    addPowerUpOptions(Map
        .of(GameEvent.SPEED_UP_ACTIVATED, this::activateSpeedUp,
            GameEvent.SPEED_UP_DEACTIVATED, this::deactivateSpeedUp));
    dotsEaten = 0;
    changeState(PacmanState.ALIVE);
  }

  /**
   * Construct a Pac-Man from a description, useful for loading Sprites from configuration files.
   *
   * @param spriteDescription Description to use.
   */
  public PacMan(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(),
        new Vec2(1, 0), 6.4);
  }

  private void changeState(PacmanState state) {
    currentState = state;
    switch (currentState) {
      case DYING -> setCurrentAnimationType(
          SpriteAnimationFactory.SpriteAnimationType.PACMAN_DEATH);
      case ALIVE -> setCurrentAnimationType(
          SpriteAnimationFactory.SpriteAnimationType.PACMAN_CHOMP);
    }
  }

  /**
   * Whether this sprite can move to a given tile.
   *
   * @param tile Destination tile.
   * @return True if the tile is open to pacman.
   */
  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToPacman();
  }

  private void applyScore(MutableGameState state, Sprite other) {
    assert (other.isConsumable());
    int pointsToAdd = 0;
    if (other.hasMultiplicativeScoring()) {
      ghostsEaten++;
      pointsToAdd = other.getScore() * ghostsEaten;
    } else {
      pointsToAdd = other.getScore();
    }
    state.incrementScore(pointsToAdd);
  }

  private void playEatingSound(MutableGameState state, Sprite consumable) {
    /*
     * Some explanation: sounds of non-dots are handled by those
     * objects, not pac-man. Dots need special handling since pacman
     * must alternate between the two chomping noises -- each
     * individual dot doesn't know which one to play.
     */
    if (!consumable.mustBeConsumed()) {
      return;
    }

    state.getAudioManager().playSound("pacman-chomp" + ((dotsEaten++ % 2 == 0) ? "1" : "2"));
  }

  /**
   * Respond to a collision with another Sprite.
   *
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   */
  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (currentState != PacmanState.ALIVE) {
      return; // must be alive to die or score
    }
    if (other.isDeadlyToPacMan()) {
      // begin death animation
      state.getAudioManager().playSound("pacman-death");
      setCurrentAnimationType(SpriteAnimationFactory.SpriteAnimationType.PACMAN_DEATH);
      state.broadcastEvent(GameEvent.PACMAN_DEATH);
      currentState = PacmanState.DYING;
    } else if (other.isConsumable()) {
      applyScore(state, other);
      playEatingSound(state, other);
    }
  }

  /**
   * Advance sprite state, moving this Sprite according to its internal stream of movement inputs,
   * and also checks its own interactions with other Sprites.
   *
   * @param dt              Time step.
   * @param pacmanGameState Game state.
   */
  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    super.step(dt, pacmanGameState);
    switch (currentState) {
      case ALIVE -> {
        move(dt, pacmanGameState.getGrid());
        handleCollisions(pacmanGameState);
        getCurrentAnimation().setPaused(getCurrentSpeed() == 0);
      }
      case DEAD -> {
        pacmanGameState.isPacmanDead(true);
      }
    }
  }

  /**
   * Whether this sprite is able to consume ghosts.
   *
   * @return True because Pac-Man can consume the Power-Pill, making the ghosts vulnerable.
   */
  @Override
  public boolean eatsGhosts() {
    return true;
  }

  /**
   * Called upon animation completion of the death animation.
   */
  @Override
  public void onAnimationComplete() {
    if (currentState == PacmanState.DYING) {
      changeState(PacmanState.DEAD);
    }
  }

  /**
   * Point value of Pac-Man.  This method is kept for potential new game modes.
   *
   * @return 0.  Classical Pac-Man does not reward any points upon consumption.
   */
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

  /**
   * Called upon new level. Fixes sounds.
   *
   * @param roundNumber current round of Pac-Man.
   * @param state       Game state.
   */
  @Override
  public void uponNewLevel(int roundNumber, MutableGameState state) {
    super.uponNewLevel(roundNumber, state);
    state.getAudioManager().playSound("start-classic");
    state.getClock().addTimer(new Timer(INITIAL_FREEZE_DURATION,
        gameState -> gameState.broadcastEvent(GameEvent.SPRITES_UNFROZEN)));
    reset();
  }

  /**
   * Called upon respawn, resetting properties as if beginning the level again.
   */
  @Override
  public void reset() {
    super.reset();
    changeState(PacmanState.ALIVE);
  }

  private enum PacmanState {
    ALIVE,
    DYING,
    DEAD
  }
}
