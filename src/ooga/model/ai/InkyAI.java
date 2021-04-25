package ooga.model.ai;

import ooga.model.PacmanGrid;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * @author George Hong
 */
public class InkyAI extends GhostAI {

  public InkyAI(PacmanGrid grid, Sprite ghost) {
    super(grid, ghost);
  }

  /**
   * Inky AI's chase behavior is based on its proximity to Pac-Man.  If Pac-Man is within 8 tiles of
   * Inky, Inky aggressively chases after Pac-Man. Otherwise, Inky wanders in scatter mode.
   *
   * @return direction to move the ghost
   */

  @Override
  protected Vec2 chaseBehavior(double dt) {
    Vec2 targetTilePos = getTarget().getCoordinates().getTileCoordinates().toVec2();
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();
    if (currentTilePos.distance(targetTilePos) < 8) {
      return reduceDistance(targetTilePos, currentTilePos);
    } else {
      return scatterBehavior(dt);
    }
  }
}
