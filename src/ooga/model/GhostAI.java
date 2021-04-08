package ooga.model;

import ooga.model.sprites.Ghost;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * First-order implementation of ghost AI.
 *
 * @author Franklin Wei
 */
public class GhostAI implements InputSource {
  private Ghost ghost;
  private PacmanGrid pacmanGrid;
  private Sprite target;
  private double intelligence;

  /* TODO: perhaps refactor? */
  public enum GhostBehavior {
    SCATTER,
    CHASE,
    FRIGHTENED
  }

  public GhostAI(PacmanGrid grid, Ghost ghost, Sprite target, double intelligence) {
    this.pacmanGrid = grid;
    this.ghost = ghost;
    this.target = target;
    this.intelligence = intelligence;
  }

  @Override
  public Vec2 getRequestedDirection() {
    // TODO: Implement this!
    return Vec2.ZERO;
  }

  @Override
  public boolean isActionPressed() {
    // TODO: Implement this!
    return false;
  }

  public void changeBehavior(GhostBehavior newBehavior) {
  }
}
