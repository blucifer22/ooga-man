package ooga.model.sprites;

import java.util.Map;
import ooga.model.GameEvent;
import ooga.model.MutableGameState;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType;
import ooga.util.Timer;
import ooga.util.Vec2;

/**
 * Basic Cherry, consumable by Pac-Man to increase the score, and respawns after a set duration.
 *
 * @author Matthew Belissary
 */
public class Cherry extends Sprite {

  private int cherryScoreIncrement = 100;
  private boolean isEdible = true;

  /**
   * Constructs a cherry object at the given coordinates
   *
   * @param position
   * @param direction
   */
  public Cherry(SpriteCoordinates position, Vec2 direction) {
    super("cherry", SpriteAnimationFactory.SpriteAnimationType.CHERRY_STILL, position, direction);
    setSwapClass(SwapClass.NONE);
    addPowerUpOptions(
        Map.of(
            GameEvent.POINT_BONUS_ACTIVATED, () -> cherryScoreIncrement *= 2,
            GameEvent.POINT_BONUS_DEACTIVATED, () -> cherryScoreIncrement *= 0.5));
  }

  /**
   * Constructs a cherry from a Sprite Description
   *
   * @param spriteDescription
   */
  public Cherry(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1, 0));
  }

  /**
   * Determines what happens to the Cherry sprite depending on what sprite it collides with
   *
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   */
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

  /**
   * Does nothing since the Cherry does not move and is not animated
   *
   * @param dt
   * @param pacmanGameState
   */
  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    // Do Nothing since cherries do not move
  }

  /**
   * @return true if the cherry is visible on screen and can be eaten
   */
  @Override
  public boolean isConsumable() {
    return isEdible;
  }

  /**
   * @return the current score value of the Cherry
   */
  @Override
  public int getScore() {
    return cherryScoreIncrement;
  }
}
