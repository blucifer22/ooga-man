package ooga.model.sprites;

import ooga.model.MutableGameState;
import ooga.model.PacmanPowerupEvent;
import ooga.model.SpriteCoordinates;
import ooga.util.Vec2;

import java.lang.management.MemoryUsage;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 *
 * @author Matthew Belissary
 */
public class Cherry extends Sprite {

  private int cherryScoreIncrement = 50;

  public Cherry(SpriteCoordinates position, Vec2 direction) {
    super(position, direction);
  }

  @Override
  public String getType() {
    return "cherry";
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    state.incrementScore(cherryScoreIncrement);
    state.prepareRemove(this);
    System.out.println("SCORE: " + state.getScore());
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
  public void respondToPowerEvent(PacmanPowerupEvent event) {
    switch (event){
//      case POINT_BONUS_ACTIVATED -> cherryScoreIncrement *= 2;
//      case POINT_BONUS_DEACTIVATED -> cherryScoreIncrement *= 0.5;
    }
  }
}
