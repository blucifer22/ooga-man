package ooga.model;

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
  }

  @Override
  public boolean isActionPressed() {

  }

  public void changeBehavior(GhostBehavior newBehavior) {
  }
}
