package ooga.model.sprites;

import ooga.model.*;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Ghost extends MoveableSprite {

  public static final String TYPE = "ghost";
  private Vec2 queuedDirection;

  public Ghost(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
    queuedDirection = new Vec2(-1, 0);
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
  public void step(double dt, PacmanGrid grid) {
    move(dt, grid);
  }

  @Override
  public ImmutableSpriteCoordinates getCenter() {
    return getCoordinates();
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }
}
