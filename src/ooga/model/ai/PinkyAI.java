package ooga.model.ai;

import ooga.model.PacmanGrid;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

public class PinkyAI extends GhostAI {

  public PinkyAI(PacmanGrid grid, Sprite ghost) {
    super(grid, ghost);
  }

  /**
   * PinkyAI attempts to reach the tile coordinate 4 tiles in the direction of its target's
   * orientation
   *
   * @return direction to move the ghost
   */

  @Override
  public Vec2 chaseBehavior(double dt) {
    Vec2 targetOrientation = getTarget().getDirection();
    Vec2 targetTilePos = getTarget().getCoordinates().getTileCoordinates().toVec2()
        .add(targetOrientation.scalarMult(4));
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();

    return reduceDistance(targetTilePos, currentTilePos);
  }
}
