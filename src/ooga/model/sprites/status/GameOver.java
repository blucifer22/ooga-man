package ooga.model.sprites.status;

import ooga.model.MutableGameState;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
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

  public GameOver(SpriteCoordinates position, Vec2 direction) {
    super("", SpriteAnimationType.GAME_OVER_FLASH, position, direction);
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    // Do nothing
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    super.step(dt, pacmanGameState);
  }
}
