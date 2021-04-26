package ooga.model.sprites.status;

import ooga.model.MutableGameState;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType;
import ooga.util.Vec2;

/**
 * GameOver creates a static flashing GameOver sprite that indicates when the game is over in the
 * classic and chase modes.
 *
 * @author Marc Chmielewski
 */
public class GameOver extends Sprite {

  /**
   * The basic constructor for a GameOver Sprite. This constructor instantiates a GameOver Sprite at
   * the specified position and with the specified orientation.
   *
   * @param position A SpriteCoordinates containing the location at which to spawn the GameOver.
   * @param direction A Vec2 specifying the orientation of the GameOver.
   */
  public GameOver(SpriteCoordinates position, Vec2 direction) {
    super("", SpriteAnimationType.GAME_OVER_FLASH, position, direction);
  }

  /**
   * This method defines the collision behavior for GameOver. In this case, a collision has no
   * effect, so the implementation is empty.
   *
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   */
  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    // Do nothing
  }
}
