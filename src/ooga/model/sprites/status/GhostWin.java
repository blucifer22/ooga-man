package ooga.model.sprites.status;

import ooga.model.MutableGameState;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType;
import ooga.util.Vec2;

/**
 * A static sprite that flashes "Ghosts Win" when the win conditions for Ghosts have been met
 *
 * @author Marc Chmielewski
 */
public class GhostWin extends Sprite {

  /**
   * The basic constructor for a GhostWin Sprite. This constructor instantiates a GhostWin Sprite at
   * the specified position and with the specified orientation. The animation is the default
   * GHOST_WIN_FLASH.
   *
   * @param position A SpriteCoordinates containing the location at which to spawn the GhostWin.
   * @param direction A Vec2 specifying the orientation of the GhostWin.
   */
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
    // Does nothing
  }
}
