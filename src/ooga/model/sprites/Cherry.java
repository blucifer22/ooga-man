package ooga.model.sprites;

import ooga.model.MutableGameState;
import ooga.model.PacmanPowerupEvent;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.FreeRunningPeriodicAnimation;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType;
import ooga.model.sprites.animation.StillAnimation;
import ooga.util.Timer;
import ooga.util.Vec2;

import java.lang.management.MemoryUsage;

/**
 * Basic Dot, consumable by Pac-Man to increase the score.
 *
 * @author Matthew Belissary
 */
public class Cherry extends Sprite {

  private int cherryScoreIncrement = 50;
  private boolean isEdible = true;

  public Cherry(SpriteCoordinates position, Vec2 direction) {
    super("cherry",
            SpriteAnimationFactory.SpriteAnimationType.CHERRY_STILL,
            position, direction);
  }

  public Cherry(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1,0));
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    if (other.eatsGhosts()){
      isEdible = false;
      this.setCurrentAnimationType(SpriteAnimationType.BLANK);
      state.getClock().addTimer(new Timer(45, mutableGameState -> {
        isEdible = true;
        this.setCurrentAnimationType(SpriteAnimationType.CHERRY_STILL);
      }));
    }
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
    return isEdible;
  }

  @Override
  public boolean isRespawnTarget() { return false; }

  @Override
  public boolean hasMultiplicativeScoring() { return false; }

  @Override
  public int getScore() {
    return cherryScoreIncrement;
  }

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {
    switch (event){
      case POINT_BONUS_ACTIVATED -> cherryScoreIncrement *= 2;
      case POINT_BONUS_DEACTIVATED -> cherryScoreIncrement *= 0.5;
    }
  }
}
