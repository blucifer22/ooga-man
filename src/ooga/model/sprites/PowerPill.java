package ooga.model.sprites;

import ooga.model.MutableGameState;
import ooga.model.PacmanGameState;
import ooga.model.SpriteCoordinates;
import ooga.util.Vec2;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 *
 * @author Matthew Belissary
 */
public class PowerPill extends Sprite {

  public PowerPill(SpriteCoordinates position, Vec2 direction) {
    super(position, direction);
  }

  @Override
  public String getType() {
    return "powerpill";
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    state.prepareRemove(this);
    System.out.println("Powerup Consumed");
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
}
