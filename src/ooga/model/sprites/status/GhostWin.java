package ooga.model.sprites.status;

import ooga.model.MutableGameState;
import ooga.model.SpriteCoordinates;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType;
import ooga.util.Vec2;

/**
 * A static sprite that flashes "Ghosts Win" when the win conditions for Ghosts have been met
 *
 * @author Marc Chmielewski
 */
public class GhostWin extends Sprite {

  public GhostWin(SpriteCoordinates position, Vec2 direction) {
    super("", SpriteAnimationType.GHOST_WIN_FLASH, position, direction);
  }

  /**
   * Sprites override this method to define game state changes or changes to the sprite upon coming
   * into contact with another Sprite.
   *
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   */
  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    // Does nothinng
  }
}
