package ooga.model;

import ooga.model.sprites.Ghost;
import ooga.model.sprites.PacMan;
import ooga.util.Vec2;

public class BlinkyAI extends GhostAI {

  public BlinkyAI(PacmanGrid grid, Ghost ghost, PacMan target,
      double intelligence) {
    super(grid, ghost, target, intelligence);
  }
}
