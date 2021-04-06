package ooga.model.sprites;

import ooga.model.PacmanGrid;
import ooga.model.SpriteCoordinates;
import ooga.model.TileCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author Matthew Belissary */
public class Ghost extends Sprite {

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
  public String getType() {
    return TYPE;
  }

  @Override
  public SpriteCoordinates getCenter() {
    return getCoordinates();
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }
}
