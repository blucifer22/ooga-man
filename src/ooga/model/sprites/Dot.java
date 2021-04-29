package ooga.model.sprites;

import java.util.Map;
import ooga.model.GameEvent;
import ooga.model.MutableGameState;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 */
public class Dot extends Sprite {

  private int dotScoreIncrement = 10;

  /**
   * Constructs a dot object at the given coordinates
   *
   * @param position Initial position
   * @param direction Initial orientation.
   */
  public Dot(SpriteCoordinates position, Vec2 direction) {
    super("dot", SpriteAnimationFactory.SpriteAnimationType.DOT_STILL, position, direction);
    setSwapClass(SwapClass.NONE);
    addPowerUpOptions(
        Map.of(
            GameEvent.POINT_BONUS_ACTIVATED, () -> dotScoreIncrement *= 2,
            GameEvent.POINT_BONUS_DEACTIVATED, () -> dotScoreIncrement *= 0.5));
  }

  /**
   * Constructs a dot from a Sprite Description
   *
   * @param spriteDescription Sprite description.
   */
  public Dot(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0));
  }

  /**
   * Deletes a dot if PacMan sprite collides with dot
   *
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   */
  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.eatsGhosts()) {
      delete(state);
    }
  }

  /**
   * Steps through animation and sound of a dot per frame (1/60 of a second)
   *
   * @param dt Time step.
   * @param pacmanGameState Game state.
   */
  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    super.step(dt, pacmanGameState);
  }

  /**
   * Whether this dot is advancement-blocking.
   *
   * @return true since a ghost must be consumed
   */
  @Override
  public boolean mustBeConsumed() {
    return true;
  }

  /**
   * Get point value.
   *
   * @return the base score of a dot
   */
  @Override
  public int getScore() {
    return dotScoreIncrement;
  }
}
