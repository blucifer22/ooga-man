package ooga.model.sprites;

import ooga.model.MutableGameState;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.powerups.PowerUp;
import ooga.model.powerups.PowerUpFactory;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

/**
 * Basic Powerpill, consumable by Pac-Man to activate a powerup.
 *
 * @author Matthew Belissary
 */
public class PowerPill extends Sprite {

  private static final int POINT_VALUE = 50;
  private final PowerUpFactory powerUpFactory;

  /**
   * Constructs a powerpill object at the given coordinates
   *
   * @param position
   * @param direction
   */
  public PowerPill(SpriteCoordinates position, Vec2 direction) {
    super(
        "powerpill",
        SpriteAnimationFactory.SpriteAnimationType.POWER_PILL_BLINK,
        position,
        direction);
    setSwapClass(SwapClass.NONE);
    powerUpFactory = new PowerUpFactory();
  }

  /**
   * Constructs a powerpill from a Sprite Description
   *
   * @param description
   */
  public PowerPill(SpriteDescription description) {
    this(description.getCoordinates(), new Vec2(1, 0));
  }

  /**
   * Determines what happens to the gamestate sprite depending on when PacMann collides with the powerpill
   *
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   */
  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.eatsGhosts()) {
      delete(state);
      PowerUp power = powerUpFactory.getRandomPowerUp();
      power.executePowerUp(state);
    }
  }

  /**
   * Animates the powerpill each frame (1/60 of a second)
   *
   * @param dt
   * @param pacmanGameState
   */
  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    super.step(dt, pacmanGameState);
  }

  /**
   * @return the point value for consuming a powerpill
   */
  @Override
  public int getScore() {
    return POINT_VALUE;
  }

  /**
   * @return true since a powerpill must be consumed to progress a level
   */
  @Override
  public boolean mustBeConsumed() {
    return true;
  }
}
