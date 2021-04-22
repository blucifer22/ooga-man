package ooga.model.sprites;

import ooga.model.MutableGameState;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType;
import ooga.util.Vec2;

public class GameOver extends Sprite {

  public GameOver(SpriteCoordinates position, Vec2 direction) {
    super("", SpriteAnimationType.GAME_OVER_FLASH, position, direction);
  }

  public GameOver(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0));
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    // Do nothing
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    super.step(dt, pacmanGameState);
  }

  @Override
  public int getScore() {
    return 0;
  }
}
