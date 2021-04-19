package ooga.model.sprites;

import ooga.model.MutableGameState;
import ooga.model.PacmanPowerupEvent;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.model.sprites.animation.StillAnimation;
import ooga.util.Vec2;

import java.lang.management.MemoryUsage;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 */
public class Dot extends Sprite {

  private int dotScoreIncrement = 1;

  public Dot(SpriteCoordinates position, Vec2 direction) {
    super("dot",
            SpriteAnimationFactory.SpriteAnimationType.DOT_STILL,
            position, direction);
  }

  public Dot(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1,0));
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.eatsGhosts()){
      delete(state);
    }
  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    super.step(dt, pacmanGameState);
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
  public boolean eatsGhosts() {
    return false;
  }

  @Override
  public boolean isConsumable() {
    return true;
  }

  @Override
  public boolean isRespawnTarget() { return false; }

  @Override
  public boolean hasMultiplicativeScoring() {
    return false;
  }

  @Override
  public int getScore() {
    return dotScoreIncrement;
  }

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {
    switch (event){
      case POINT_BONUS_ACTIVATED -> dotScoreIncrement *= 2;
      case POINT_BONUS_DEACTIVATED -> dotScoreIncrement *= 0.5;
    }
  }
}
