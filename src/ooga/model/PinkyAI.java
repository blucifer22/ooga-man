package ooga.model;

import ooga.model.sprites.Ghost;
import ooga.model.sprites.Home;
import ooga.model.sprites.PacMan;
import ooga.util.Vec2;

public class PinkyAI extends GhostAI {

  public PinkyAI(PacmanGrid grid, Ghost ghost, PacMan target, Home home,
      double intelligence) {
    super(grid, ghost, target, home, intelligence);
  }

  /**
   * PinkyAI attempts to reach the tile coordinate 4 tiles in the direction of its target's
   * orientation
   *
   * @return direction to move the ghost
   */
  @Override
  public Vec2 chaseBehavior() {
    Vec2 targetOrientation = getTarget().getDirection();
    Vec2 targetTilePos = getTarget().getCoordinates().getTileCoordinates().toVec2()
        .add(targetOrientation.scalarMult(4));
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();

    return reduceDistance(targetTilePos, currentTilePos);
  }
}
