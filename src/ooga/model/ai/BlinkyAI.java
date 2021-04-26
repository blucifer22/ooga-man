package ooga.model.ai;

import ooga.model.grid.PacmanGrid;
import ooga.model.sprites.Sprite;

/**
 * Encodes that Blinky relies on the default, aggressive chase AI
 *
 * @author George Hong
 */
public class BlinkyAI extends GhostAI {

  /**
   * Constructs an instance of GhostAI similar to the classical AI
   *
   * @param grid
   * @param ghost
   */
  public BlinkyAI(PacmanGrid grid, Sprite ghost) {
    super(grid, ghost);
  }
}
