package ooga.model.sprites;

import ooga.model.MutableGameState;
import ooga.model.PacmanPowerupEvent;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.ObservableAnimation;
import ooga.model.sprites.animation.SpriteAnimationFactory.SpriteAnimationType;
import ooga.model.sprites.animation.StillAnimation;
import ooga.util.Vec2;

public class Home extends Sprite {

  public Home(SpriteCoordinates position, Vec2 direction) {
    super("", SpriteAnimationType.BLANK, position, direction);
  }

  public Home(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates(), new Vec2(1,0));
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) { }

  @Override
  public boolean mustBeConsumed() { return false; }

  @Override
  public boolean isDeadlyToPacMan() { return false; }

  @Override
  public boolean eatsGhosts() { return false; }

  @Override
  public boolean isConsumable() { return false; }

  @Override
  public boolean isRespawnTarget() { return true; }

  @Override
  public boolean hasMultiplicativeScoring() { return false; }

  @Override
  public int getScore() { return 0; }

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {}
}
