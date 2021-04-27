package ooga.model.ai;

import ooga.model.grid.PacmanGrid;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * The InkyAI is an AI intended for use with the Inky ghost.  Inky's behavior is unpredictable, and
 * can be characterized by random movement when Pac-Man is far away, before springing into action
 * when Pac-Man becomes close to it.
 *
 * @author George Hong
 */
public class InkyAI extends GhostAI {

  /**
   * Constructs an instance of InkyAI
   *
   * @param grid  grid occupied by the linked Ghost associated with this AI
   * @param ghost ghost controlled by this AI instance.
   */
  public InkyAI(PacmanGrid grid, Sprite ghost) {
    super(grid, ghost);
  }

  /**
   * Inky AI's chase behavior is based on its proximity to Pac-Man. If Pac-Man is within 8 tiles of
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
