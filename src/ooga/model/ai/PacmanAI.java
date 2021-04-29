package ooga.model.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import ooga.model.api.InputSource;
import ooga.model.grid.PacmanGrid;
import ooga.model.grid.TileCoordinates;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * Basic AI for Pac-Man that focuses on maximizing distance between all tracked target Sprites.
 * Intended for use with chase mode.
 *
 * @author George Hong
 */
public class PacmanAI implements InputSource {

  private final List<Sprite> targets;
  private final PacmanGrid pacmanGrid;
  private final Sprite pacMan;

  /**
   * Constructs an instance of PacmanAI.
   *
   * @param grid   grid occupied by the Pac-Man sprite controlled by this AI
   * @param pacMan Pac-Man sprite controlled by this AI.
   */
  public PacmanAI(PacmanGrid grid, Sprite pacMan) {
    this.pacMan = pacMan;
    targets = new ArrayList<>();
    pacmanGrid = grid;
  }

  /**
   * Returns the stored PacmanGrid, helpful for making more informed decisions regarding appropriate
   * direction options.
   *
   * @return Pac-Man grid stored by this AI
   */
  protected PacmanGrid getPacmanGrid() {
    return pacmanGrid;
  }

  /**
   * Returns the stored target Sprites
   *
   * @return stored target Sprites
   */
  protected List<Sprite> getTargets() {
    return targets;
  }

  /**
   * Returns an instance of the Sprite associated with this AI
   *
   * @return sprite associated with this AI
   */
  protected Sprite getPacMan() {
    return pacMan;
  }

  /**
   * Add Sprite to be tracked by the movement algorithm. The algorithm will try to maximize the
   * distance between Pac-Man and these sprites.
   *
   * @param sprite Sprites to move away from.
   */
  public void addTarget(Sprite sprite) {
    targets.add(sprite);
  }

  /**
   * The Pac-Man AI is focused on survival. It considers the position of all ghosts in the game. For
   * each direction that it can choose, it considers the closest Sprite in that direction to make
   * decisions. The final direction chosen is an attempt to maximize the closest distance. This AI
   * can freely reverse its current direction, if necessary.  It is also capable of choosing to not
   * move.
   *
   * @param currentTilePos current position of this AI
   * @param targets        Sprites that this AI attempts to avoid and maximize distance from.
   * @return Direction instruction to send to the Sprite
   */
  protected Vec2 maximizeDistance(Vec2 currentTilePos, List<Sprite> targets) {
    Vec2[] directions = {
        new Vec2(0, 0), new Vec2(-1, 0), new Vec2(1, 0), new Vec2(0, 1), new Vec2(0, -1)
    };
    List<DirectionDistanceWrapper> distances = new ArrayList<>();
    for (Vec2 direction : directions) {
      Vec2 nextPos = currentTilePos.add(direction);
      if (!isOpenToPacman(nextPos)) {
        continue;
      }
      double closestDistance = Double.MAX_VALUE;
      for (Sprite target : targets) {
        Vec2 targetTilePos = target.getCoordinates().getTileCoordinates().toVec2();
        double candDistance = getCandDistance(nextPos, targetTilePos);
        closestDistance = Math.min(candDistance, closestDistance);
      }
      distances.add(new DirectionDistanceWrapper(direction, closestDistance));
    }
    Collections.sort(distances, Collections.reverseOrder());
    Queue<DirectionDistanceWrapper> queue = new LinkedList<>(distances);
    while (!queue.isEmpty()) {
      Vec2 cand = queue.remove().getVec();
      Vec2 target = currentTilePos.add(cand);
      if (!isOpenToPacman(target)) {
        continue;
      }
      return cand;
    }
    return Vec2.ZERO;
  }

  /**
   * Returns the distance from two points, allowing the AI to define the desired distance metric to
   * use based on the complexity of the algorithm
   *
   * @param start start point
   * @param end   end point
   * @return distance between provided points
   */
  protected double getCandDistance(Vec2 start, Vec2 end) {
    return end.distance(start);
  }

  /**
   * Checks whether target position is open to Pac-Man, the intended Sprite for this AI.  This
   * method also checks that the target is within bounds before proceeding.
   *
   * @param target target position for Pac-Man.
   * @return boolean representing whether Pac-Man can enter
   */
  protected boolean isOpenToPacman(Vec2 target) {
    if (!pacmanGrid.inBoundaries(new TileCoordinates(target))) {
      return false;
    }
    return pacmanGrid
        .getTile(new TileCoordinates((int) target.getX(), (int) target.getY()))
        .isOpenToPacman();
  }

  /**
   * Queried by a Sprite, intended for Pac-Man, so that a movement option is received and
   * potentially carried out.
   *
   * @param dt change in time, used for complex movement decisions.
   * @return direction option to move Sprite.
   */
  @Override
  public Vec2 getRequestedDirection(double dt) {
    Vec2 currentTilePos = pacMan.getCoordinates().getTileCoordinates().toVec2();
    return maximizeDistance(currentTilePos, targets);
  }

  /**
   * Queried by a Sprite, and checks that a special action is specified to be carried out by the
   * Sprite
   *
   * @return whether a special action is requested for the Sprite.  None are available for Pac-Man.
   */
  @Override
  public boolean isActionPressed() {
    return false;
  }
}
