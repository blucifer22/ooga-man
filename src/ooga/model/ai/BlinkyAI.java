package ooga.model.ai;

import ooga.model.grid.PacmanGrid;
import ooga.model.sprites.Sprite;

/**
 * AI intended for the ghost Blinky that relies on the default, aggressive chase AI
 *
 * @author George Hong
 */
public class BlinkyAI extends GhostAI {

  /**
   * Constructs an instance of GhostAI similar to the classical Blinky ghost behavior.  This AI for
   * ghosts pursues Pac-Man relentlessly.
   *
   * @param grid  grid occupied by the Ghost linked to this Ghost AI
   * @param ghost reference to the ghost that this AI provides instructions to
   */
  public BlinkyAI(PacmanGrid grid, Sprite ghost) {
    super(grid, ghost);
  }
}
