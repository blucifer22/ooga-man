package ooga.model.sprites;

import java.util.Map;
import ooga.model.GameEvent;
import ooga.model.MutableGameState;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

/**
 * The "Ready!" sprite.
 *
 * @author Franklin Wei
 */
public class ReadySprite extends Sprite {

  /**
   * Construct a Ready! sprite.
   * @param position Position
   * @param direction Orientation
   */
  public ReadySprite(SpriteCoordinates position, Vec2 direction) {
    super("ready", SpriteAnimationFactory.SpriteAnimationType.READY, position, direction);
    setSwapClass(SwapClass.NONE);
    addPowerUpOptions(
        Map.of(
            GameEvent.SPRITES_UNFROZEN,
            () -> setCurrentAnimationType(SpriteAnimationFactory.SpriteAnimationType.BLANK)));
  }

  /**
   * Construct a "Ready!" sprite.
   * @param spriteDescription Sprite description.
   */
  public ReadySprite(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0));
  }

  /**
   * Do nothing.
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   */
  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {}

  /**
   * Advance a level and reset the Ready! sprite.
   * @param roundNumber New round number.
   * @param state State to which this sprite belongs.
   */
  @Override
  public void uponNewLevel(int roundNumber, MutableGameState state) {
    super.uponNewLevel(roundNumber, state);

    setCurrentAnimationType(SpriteAnimationFactory.SpriteAnimationType.READY);
  }
}
