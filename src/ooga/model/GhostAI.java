package ooga.model;

import java.util.ArrayList;
import java.util.Random;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Ghost.GhostBehavior;
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
    switch (getGhost().getGhostBehavior()) {
      case CHASE:
        return chaseBehavior();
      case FRIGHTENED:
        return frightenedBehavior();
      case EATEN:
        return eatenBehavior();
      case WAIT:
        return Vec2.ZERO;
      case SCATTER:
        return scatterBehavior();
    }
    return Vec2.ZERO;
  }

  /**
   * This mode corresponds to the ghost seeking home
   * @return
   */
  protected Vec2 eatenBehavior() {
    // TODO: Implement
    return Vec2.ZERO;
  }

  /**
   * This mode coincides with the ghost reaction to upon the Power-pill's consumption.
   *
   * @return
   */
  protected Vec2 frightenedBehavior() {
    // TODO: Implement
    return Vec2.ZERO;
  }

  /**
   * This mode coincides with classic Pac-Man where ghosts periodically give up the chase and choose
   * to wander around for a few seconds.  This is emulated by default by using a random direction
   * generator.
   *
   * @return direction to queue for ghost to move to
   */
  protected Vec2 scatterBehavior() {
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

  /**
   * This behavior can be overridden to define the most aggressive modes for Pac-Man.  Targeting can
   * be used here to follow a tracked Sprite, such as Pac-Man.  The default GhostAI defaults to
   * random behavior to "track" Pac-Man.
   *
   * @return direction to queue for ghost to move to
   */
  protected Vec2 chaseBehavior() {
    return scatterBehavior();
  }

  @Override
  public boolean isActionPressed() {
    // TODO: Implement this!
    return false;
  }

}
