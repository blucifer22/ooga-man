package ooga.model.sprites;

import java.util.Map;
import ooga.model.GameEvent;
import ooga.model.MutableGameState;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType;
import ooga.util.Timer;
import ooga.util.Vec2;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 *
 * @author Matthew Belissary
 */
public class Cherry extends Sprite {

  private int cherryScoreIncrement = 100;
  private boolean isEdible = true;

  public Cherry(SpriteCoordinates position, Vec2 direction) {
    super("cherry", SpriteAnimationFactory.SpriteAnimationType.CHERRY_STILL, position, direction);
    setSwapClass(SwapClass.NONE);
    addPowerUpOptions(
        Map.of(
            GameEvent.POINT_BONUS_ACTIVATED, () -> cherryScoreIncrement *= 2,
            GameEvent.POINT_BONUS_DEACTIVATED, () -> cherryScoreIncrement *= 0.5));
  }

  public Cherry(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0));
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.eatsGhosts() && isConsumable()) {
      isEdible = false;

      state.getAudioManager().playSound("fruit-eaten");

      this.setCurrentAnimationType(SpriteAnimationType.BLANK);
      state
          .getClock()
          .addTimer(
              new Timer(
                  45,
                  mutableGameState -> {
                    isEdible = true;
                    this.setCurrentAnimationType(SpriteAnimationType.CHERRY_STILL);
                  }));
    }
  }

  // TODO: See if I dont need to override since it does nothing if not just add a DO NOTHING comment
  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    // Do Nothing since cherries do not move
  }

  @Override
  public boolean isConsumable() {
    return isEdible;
  }

  @Override
  public int getScore() {
    return cherryScoreIncrement;
  }
}
