package ooga.model.sprites;

import java.util.Map;
import ooga.model.*;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

/**
 * @author George Hong
 */
public class PacMan extends MoveableSprite {

  public static final String TYPE = "pacman_halfopen";
  private int ghostsEaten;
  private int dotsEaten;

  public PacMan(SpriteCoordinates position, Vec2 direction, double speed) {
    super("pacman",
        SpriteAnimationFactory.SpriteAnimationType.PACMAN_CHOMP,
        position, direction, speed);
    setSwapClass(SwapClass.PACMAN);
    setPowerupOptions(Map
        .of(PacmanPowerupEvent.SPEED_UP_ACTIVATED, this::activateSpeedUp,
            PacmanPowerupEvent.SPEED_UP_DEACTIVATED, this::deactivateSpeedUp));
    dotsEaten = 0;
  }

  public PacMan(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(),
        new Vec2(1, 0), 6.4);
  }

  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToPacman();
  }

  private void applyScore(MutableGameState state, Sprite other) {
    assert(other.isConsumable());

    int pointsToAdd = 0;

    if(other.hasMultiplicativeScoring()) {
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
    if(!consumable.mustBeConsumed())
      return;

    state.getAudioManager().playSound("pacman-chomp" + ((dotsEaten++ % 2 == 0) ? "1" : "2"));
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.isDeadlyToPacMan()) {
      state.isPacmanDead(true);
    } else if (other.isConsumable()) {
      applyScore(state, other);

      playEatingSound(state, other);

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

  @Override
  public void uponNewLevel(int roundNumber, MutableGameState state) {
    super.uponNewLevel(roundNumber, state);

    state.getAudioManager().playSound("start-classic");
  }
}
