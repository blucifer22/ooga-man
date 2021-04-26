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
 * @author George Hong
 */
public class PacMan extends MoveableSprite {

  public static final String TYPE = "pacman_halfopen";
  private static final double INITIAL_FREEZE_DURATION = 4.2; // length of the starting sound
  private int ghostsEaten;
  private int dotsEaten;

  private PacmanState currentState;

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
      System.out.println("SCORE: " + state.getScore());
    }
  }

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

  @Override
  public boolean eatsGhosts() {
    return true;
  }

  @Override
  public void onAnimationComplete() {
    if (currentState == PacmanState.DYING) {
      changeState(PacmanState.DEAD);
    }
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

  @Override
  public void uponNewLevel(int roundNumber, MutableGameState state) {
    super.uponNewLevel(roundNumber, state);
    state.getAudioManager().playSound("start-classic");
    state.getClock().addTimer(new Timer(INITIAL_FREEZE_DURATION,
        gameState -> gameState.broadcastEvent(GameEvent.SPRITES_UNFROZEN)));
    reset();
  }

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
