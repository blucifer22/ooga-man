package ooga.model.ai;

import ooga.model.PacmanGrid;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

public class ClydeAI extends GhostAI {

  public ClydeAI(PacmanGrid grid, Sprite ghost) {
    super(grid, ghost);
  }

  /**
   * Clyde AI's chase behavior is based on its proximity to Pac-Man.  If Pac-Man is within 8 tiles
   * of Clyde, ClydeAI adopts BlinkyAI's tracking.  Otherwise, ClydeAI defaults to its scatter
   * behavior.
   *
   * @return direction to move the ghost
   */
  public Vec2 chaseBehavior() {
    Vec2 targetTilePos = getTarget().getCoordinates().getTileCoordinates().toVec2();
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();
    if (currentTilePos.distance(targetTilePos) <= 8) {
      return reduceDistance(targetTilePos, currentTilePos);
    } else {
      return scatterBehavior();
    }
  }
}
