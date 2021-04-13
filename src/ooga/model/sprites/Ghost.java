package ooga.model.sprites;

import ooga.model.*;
import ooga.model.api.PowerupEventObserver;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/**
 * @author Matthew Belissary
 */
public class Ghost extends MoveableSprite {

  public static final String TYPE = "ghost";
  private boolean isDeadly = true;
  private boolean isAfraid;

  public Ghost(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
    swapClass = SwapClass.GHOST;
  }

  public Ghost(SpriteDescription spriteDescription) {
    super(spriteDescription);
  }

  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToGhosts();
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {

  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    move(dt, pacmanGameState.getGrid());
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }

  @Override
  public boolean isDeadlyToPacMan() {
    return isDeadly;
  }

  public boolean isFrightened(){ return isAfraid; }

  private void changeFearStatus(boolean fearStatus){ isAfraid = fearStatus; }

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {
    switch (event){
//      case GHOST_SLOWDOWN_ACTIVATED -> setMovementSpeed(getMovementSpeed() * 0.5);
//      case GHOST_SLOWDOWN_DEACTIVATED -> setMovementSpeed(getMovementSpeed() * 2);
      case FRIGHTEN_ACTIVATED -> changeFearStatus(true);
      case FRIGHTEN_DEACTIVATED -> changeFearStatus(false);
    }
  }
}
