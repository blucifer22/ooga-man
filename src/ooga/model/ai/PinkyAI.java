package ooga.model.ai;

import ooga.model.grid.PacmanGrid;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * AI intended for use with the ghost Pinky that relies on a proactive AI that attempts to surround
 * Pac-Man.
 *
 * @author George Hong
 */
public class PinkyAI extends GhostAI {

  /**
   * Constructs an instance of GhostAI similar to the classic Pinky ghost behavior.  This AI for
   * ghosts attempts to step in front of Pac-Man, cutting off escape routes.
   *
   * @param grid  grid occupied by the Ghost linked to this Ghost AI.
   * @param ghost references to the ghost that this AI provides instructiosn to.
   */
  public PinkyAI(PacmanGrid grid, Sprite ghost) {
    super(grid, ghost);
  }

  /**
   * PinkyAI attempts to reach the tile coordinate 4 tiles in the direction of its target's
   * orientation.  In conjunction with Blinky, this creates the impression that Pinky often ends up
   * cutting Pac-Man's escape route off.
   *
   * @return direction to direct the ghost to carry out Pinky's canonical behavior.
   */
  @Override
  public Vec2 chaseBehavior(double dt) {
    Vec2 targetOrientation = getTarget().getDirection();
    Vec2 targetTilePos =
        getTarget()
            .getCoordinates()
            .getTileCoordinates()
            .toVec2()
            .add(targetOrientation.scalarMult(4));
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();

    return reduceDistance(targetTilePos, currentTilePos);
  }
}
