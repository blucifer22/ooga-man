package ooga.model.ai;

import ooga.model.grid.PacmanGrid;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * AI intended for use with the ghost Clyde that has unpredictable Chase behavior, considerably less
 * focused on pursuing Pac-Man.
 *
 * @author George Hong
 */
public class ClydeAI extends GhostAI {

  /**
   * Constructs an instance of ClydeAI, similar to the classical Clyde ghost behavior.  This ghost
   * pursues Pac-Man only when Pac-Man becomes too far away.
   *
   * @param grid  grid occupied by the Ghost linked to this Ghost AI
   * @param ghost reference to the ghost that this AI provides instructions to
   */
  public ClydeAI(PacmanGrid grid, Sprite ghost) {
    super(grid, ghost);
  }

  /**
   * Clyde AI's chase behavior is based on its proximity to Pac-Man. If Pac-Man is within 8 tiles of
   * Clyde, ClydeAI defaults to its scatter behavior. If Pac-Man is further than 8 tiles away, it
   * tries to close the distance between it and Pac-Man.
   *
   * @return direction to move the ghost
   */
  @Override
  public Vec2 chaseBehavior(double dt) {
    Vec2 targetTilePos = getTarget().getCoordinates().getTileCoordinates().toVec2();
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();
    if (currentTilePos.distance(targetTilePos) > 8) {
      return reduceDistance(targetTilePos, currentTilePos);
    } else {
      return scatterBehavior(dt);
    }
  }
}
