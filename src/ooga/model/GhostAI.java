package ooga.model;

import java.util.ArrayList;
import java.util.Random;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * First-order implementation of ghost AI.
 *
 * @author Franklin Wei
 * @author Matthew Belissary
 */
public class GhostAI implements InputSource {

  private final Ghost ghost;
  private final PacmanGrid pacmanGrid;
  private final PacMan target;
  private final double intelligence;

  public GhostAI(PacmanGrid grid, Ghost ghost, PacMan target, double intelligence) {
    this.pacmanGrid = grid;
    this.ghost = ghost;
    this.target = target;
    this.intelligence = intelligence;
  }

  protected Sprite getTarget() {
    return target;
  }

  protected Ghost getGhost() {
    return ghost;
  }

  protected PacmanGrid getPacmanGrid() {
    return pacmanGrid;
  }

  @Override
  public Vec2 getRequestedDirection() {
    // TODO: Implement this!
    Vec2 ret = Vec2.ZERO;
    ArrayList<Vec2> randomVectorOptions = new ArrayList<>();
    randomVectorOptions.add(new Vec2(-1.0, 0));
    randomVectorOptions.add(new Vec2(1.0, 0));
    randomVectorOptions.add(new Vec2(0.0, 1.0));
    randomVectorOptions.add(new Vec2(0.0, -1.0));
    Random random = new Random();
    if (random.nextDouble() <= intelligence) {
      Random randomVector = new Random();
      ret = randomVectorOptions.get(randomVector.nextInt(randomVectorOptions.size()));
    }
    Vec2 currentReverseDirection = ghost.getDirection().scalarMult(-1);
    return ret.equals(currentReverseDirection) ? Vec2.ZERO : ret;
  }

  @Override
  public boolean isActionPressed() {
    // TODO: Implement this!
    return false;
  }

  public void changeBehavior(GhostBehavior newBehavior) {
  }

  /* TODO: perhaps refactor? */
  public enum GhostBehavior {
    SCATTER,
    CHASE,
    FRIGHTENED
  }

}
