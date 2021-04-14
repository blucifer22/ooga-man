package ooga.model.sprites;

import java.util.Random;
import ooga.model.MutableGameState;
import ooga.model.PacmanGameState;
import ooga.model.PacmanPowerupEvent;
import ooga.model.SpriteCoordinates;
import ooga.util.Timer;
import ooga.util.Vec2;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 *
 * @author Matthew Belissary
 */
public class PowerPill extends Sprite {

  public PowerPill(SpriteCoordinates position, Vec2 direction) {
    super(new FreeRunningAnimation(List.of("powerpill", "blank")), position, direction);
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    delete(state);
    // PacmanPowerupEvent has cases for activation of powerups (Even indices) and deactivates them for odd indices
    int evenIndex = new Random().nextInt(PacmanPowerupEvent.values().length / 2) * 2;
    state.notifyPowerupListeners(PacmanPowerupEvent.values()[evenIndex]);
    System.out.println(PacmanPowerupEvent.values()[evenIndex]);
    state.getClock().addTimer(new Timer(9, mutableGameState -> {
      state.notifyPowerupListeners(PacmanPowerupEvent.values()[evenIndex + 1]);
      System.out.println(PacmanPowerupEvent.values()[evenIndex + 1]);
    }));
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }

  @Override
  public boolean isDeadlyToPacMan() {
    return false;
  }

  @Override
  public boolean eatsGhosts() {
    return false;
  }

  @Override
  public boolean isConsumable() {
    return true;
  }

  @Override
  public boolean hasMultiplicativeScoring() {
    return false;
  }

  @Override
  public int getScore() {
    return 0;
  }

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {

  }
}
