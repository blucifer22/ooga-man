package ooga.model.sprites;

import ooga.model.MutableGameState;
import ooga.model.PacmanPowerupEvent;
import ooga.model.SpriteCoordinates;
import ooga.util.Vec2;

import java.lang.management.MemoryUsage;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 */
public class Dot extends Sprite {

  private int dotScoreIncrement = 1;

  public Dot(SpriteCoordinates position, Vec2 direction) {
    super(position, direction);
  }

  @Override
  public String getType() {
    return "dot";
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    state.incrementScore(dotScoreIncrement);
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

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {
    switch (event){
//      case POINT_BONUS_ACTIVATED -> dotScoreIncrement *= 2;
//      case POINT_BONUS_DEACTIVATED -> dotScoreIncrement *= 0.5;
    }
  }
}
