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
 */
public class GhostAI implements InputSource {
  private Ghost ghost;
  private PacmanGrid pacmanGrid;
  private PacMan target;
  private double intelligence;

  /* TODO: perhaps refactor? */
  public enum GhostBehavior {
    SCATTER,
    CHASE,
    FRIGHTENED
  }

  public GhostAI(PacmanGrid grid, Ghost ghost, PacMan target, double intelligence) {
    this.pacmanGrid = grid;
    this.ghost = ghost;
    this.target = target;
    this.intelligence = intelligence;
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
    if (random.nextDouble() <= intelligence){
      Random randomVector = new Random();
      ret = randomVectorOptions.get(randomVector.nextInt(randomVectorOptions.size()));
    }
    return ret;
  }

  @Override
  public boolean isActionPressed() {
    // TODO: Implement this!
    return false;
  }

  public void changeBehavior(GhostBehavior newBehavior) {
  }
}
