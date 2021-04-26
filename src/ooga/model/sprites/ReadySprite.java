package ooga.model.sprites;

import java.util.Map;
import ooga.model.MutableGameState;
import ooga.model.GameEvent;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

/**
 * "Ready!" sprite.
 */
public class ReadySprite extends Sprite {

  public ReadySprite(SpriteCoordinates position, Vec2 direction) {
    super("ready",
            SpriteAnimationFactory.SpriteAnimationType.READY,
            position, direction);
    setSwapClass(SwapClass.NONE);
    addPowerUpOptions(Map
                      .of(GameEvent.SPRITES_UNFROZEN, () -> setCurrentAnimationType(SpriteAnimationFactory.SpriteAnimationType.BLANK)));
  }

  public ReadySprite(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1,0));
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
  }

  @Override
  public void uponNewLevel(int roundNumber, MutableGameState state) {
    super.uponNewLevel(roundNumber, state);

    setCurrentAnimationType(SpriteAnimationFactory.SpriteAnimationType.READY);
  }
}
