package ooga.model.sprites;

import ooga.model.MutableGameState;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.powerups.PowerUp;
import ooga.model.powerups.PowerUpFactory;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

/**
 * Basic Powerpill, consumable by Pac-Man to increase the score.
 *
 * @author Matthew Belissary
 */
public class PowerPill extends Sprite {

  private final PowerUpFactory powerUpFactory;

  private static final int POINT_VALUE = 50;

  public PowerPill(SpriteCoordinates position, Vec2 direction) {
    super("powerpill", SpriteAnimationFactory.SpriteAnimationType.POWER_PILL_BLINK, position,
        direction);
    powerUpFactory = new PowerUpFactory();
  }

  public PowerPill(SpriteDescription description) {
    this(description.getCoordinates(), new Vec2(1, 0));
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.eatsGhosts()) {
      delete(state);
//      // GameEvent has cases for activation of powerups (Even indices) and deactivates them for odd indices
//      // int evenIndex = new Random().nextInt(GameEvent.values().length / 2) * 2;
//      int evenIndex = 4;
//      state.broadcastEvent(GameEvent.values()[evenIndex]);
//      System.out.println(GameEvent.values()[evenIndex]);
//      state.getClock().addTimer(new Timer(9, mutableGameState -> {
//        state.broadcastEvent(GameEvent.values()[evenIndex + 1]);
//        System.out.println(GameEvent.values()[evenIndex + 1]);
//      }));
      PowerUp power = powerUpFactory.getRandomPowerUp();
      power.executePowerUp(state);

    }
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    super.step(dt, pacmanGameState);
  }

  @Override
  public int getScore() {
    return POINT_VALUE;
  }

  @Override
  public boolean mustBeConsumed() {
    return true;
  }


}
