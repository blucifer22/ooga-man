package ooga.model.sprites;

import ooga.model.*;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Ghost extends MoveableSprite {

  public static final String TYPE = "ghost";

  public Ghost(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
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
  public void uponHitBy(Sprite other, PacmanGameState state) {

  }

  @Override
  public void step(double dt, PacmanGameState pacmanGameState) {
    move(dt, pacmanGameState.getGrid());
  }

  @Override
  public SpriteCoordinates getCenter() {
    return getCoordinates();
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }

  @Override
  public boolean isDeadlyToPacMan() { return true; }
}
