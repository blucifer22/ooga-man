package ooga.model.sprites;

import ooga.model.MutableGameState;
import ooga.model.SpriteCoordinates;
import ooga.util.Vec2;

import java.lang.management.MemoryUsage;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 */
public class Dot extends Sprite {

  public Dot(SpriteCoordinates position, Vec2 direction) {
    super(position, direction);
  }

  @Override
  public String getType() {
    return "dot";
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    state.incrementScore(1);
    state.prepareRemove(this);
    System.out.println("SCORE: " + state.getScore());
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {

  }

  @Override
  public boolean mustBeConsumed() {
    return true;
  }

  @Override
  public boolean isDeadlyToPacMan() {
    return false;
  }
}
