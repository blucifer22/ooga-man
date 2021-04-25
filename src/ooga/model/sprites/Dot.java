package ooga.model.sprites;

import java.util.Map;
import ooga.model.MutableGameState;
import ooga.model.GameEvent;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 */
public class Dot extends Sprite {

  private int dotScoreIncrement = 10;

  public Dot(SpriteCoordinates position, Vec2 direction) {
    super("dot",
            SpriteAnimationFactory.SpriteAnimationType.DOT_STILL,
            position, direction);
    setSwapClass(SwapClass.NONE);
    addPowerUpOptions(Map
        .of(GameEvent.POINT_BONUS_ACTIVATED, () -> dotScoreIncrement *= 2,
            GameEvent.POINT_BONUS_DEACTIVATED, () -> dotScoreIncrement *= 0.5));
  }

  public Dot(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1,0));
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.eatsGhosts()){
      delete(state);
    }
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    super.step(dt, pacmanGameState);
  }

  @Override
  public boolean mustBeConsumed() {
    return true;
  }

  @Override
  public int getScore() {
    return dotScoreIncrement;
  }

}
