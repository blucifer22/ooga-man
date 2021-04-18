package ooga.model;

import ooga.model.sprites.Ghost;
import ooga.model.sprites.Home;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

public class BlinkyAI extends GhostAI {

  public BlinkyAI(PacmanGrid grid, Ghost ghost, Sprite target, Sprite home,
      double intelligence) {
    super(grid, ghost, target, home, intelligence);
  }
}
