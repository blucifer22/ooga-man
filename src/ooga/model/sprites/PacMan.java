package ooga.model.sprites;

import ooga.model.PacmanGrid;
import ooga.model.SpriteCoordinates;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author George Hong */
public class PacMan extends MoveableSprite {

  public static final String TYPE = "Pac-Man";

  public PacMan(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
  }

  public PacMan(SpriteDescription spriteDescription) {
    super(spriteDescription);
  }

  @Override
  protected boolean canMoveTo(Tile tile) {
    return tile.isOpenToPacman();
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
  public SpriteCoordinates getCenter() {
    return getCoordinates();
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }
}
