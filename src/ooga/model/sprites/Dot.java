package ooga.model.sprites;

import ooga.model.PacmanGameState;
import ooga.model.PacmanGrid;
import ooga.model.SpriteCoordinates;
import ooga.util.Vec2;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 */
public class Dot extends Sprite {

  public Dot(SpriteCoordinates position, Vec2 direction) {
    super(position, direction);
  }

  @Override
  public SpriteCoordinates getCenter() {
    return getCoordinates();
  }

  @Override
  public String getType() {
    return "dot";
  }

  @Override
  public void uponHitBy(Sprite other, PacmanGameState state) {
    state.incrementScore(1);
    state.prepareRemove(this);
  }

  @Override
  public void step(double dt, PacmanGrid grid, PacmanGameState pacmanGameState) {

  }

  @Override
  public boolean mustBeConsumed() {
    return true;
  }
}
