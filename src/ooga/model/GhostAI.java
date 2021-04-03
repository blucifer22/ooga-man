package ooga.model;

import ooga.util.Vec2;

/**
 * First-order implementation of ghost AI.
 *
 * @author Franklin Wei
 */
public class GhostAI implements InputSource {
  /* TODO: perhaps refactor? */
  public enum GhostBehavior {
    SCATTER,
    CHASE,
    FRIGHTENED
  }

  public GhostAI(PacmanGrid grid, Sprite ghost, Sprite target) {
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
