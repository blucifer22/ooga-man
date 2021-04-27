package ooga.model.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import ooga.model.grid.PacmanGrid;
import ooga.model.grid.TileCoordinates;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * Add Pac-Man AI that uses a BFS algorithm. This AI has two primary modes. It engages in ghost-like
 * scatter movement when no ghosts are near. If ghosts are in the vicinity, it attempts to maximize
 * the minimum distance from a ghost.
 *
 * @author George Hong
 */
public class PacmanBFSAI extends PacmanAI {

  private static final int SENSITIVITY_RADIUS = 4;
  private static final double STABLE_MOVEMENT = 0.5;

  /**
   * Constructs an instance of the Pac-Man BFS-based AI.
   *
   * @param grid   grid occupied by the Pac-Man Sprite connected to this AI
   * @param pacMan Pac-Man Sprite controlled by this AI
   */
  public PacmanBFSAI(PacmanGrid grid, Sprite pacMan) {
    super(grid, pacMan);
  }

  /**
   * Queried by the connected Sprite.  This AI will check whether ghosts are within its
   * SENSITIVITY_RADIUS.  If this is the case, the AI will panic and attempt to escape by maximizing
   * BFS distance within the maze from each of the ghosts.  Otherwise, it will engage in scatter
   * mode.
   *
   * @param dt change in time, used for complex movement decisions.
   * @return Direction of movement.
   */
  @Override
  public Vec2 getRequestedDirection(double dt) {
    List<Sprite> threatsInVicinity = new ArrayList<>();
    Vec2 currentTilePos = getPacMan().getCoordinates().getTileCoordinates().toVec2();
    for (Sprite target : getTargets()) {
      Vec2 targetCoords = target.getCoordinates().getTileCoordinates().toVec2();
      if (currentTilePos.distance(targetCoords) < SENSITIVITY_RADIUS) {
        threatsInVicinity.add(target);
      }
    }
    if (threatsInVicinity.size() == 0) {
      return scatterBehavior();
    }
    return maximizeDistance(currentTilePos, threatsInVicinity);
  }

  /**
   * Cause pacman to scatter.
   * @return Direction of movement.
   */
  protected Vec2 scatterBehavior() {
    ArrayList<Vec2> randomVectorOptions = new ArrayList<>();
    if (ThreadLocalRandom.current().nextDouble(1) < STABLE_MOVEMENT) {
      return Vec2.ZERO;
    }
    Vec2 ret;
    randomVectorOptions.add(new Vec2(-1.0, 0));
    randomVectorOptions.add(new Vec2(1.0, 0));
    randomVectorOptions.add(new Vec2(0.0, 1.0));
    randomVectorOptions.add(new Vec2(0.0, -1.0));
    ret = randomVectorOptions.get(ThreadLocalRandom.current().nextInt(randomVectorOptions.size()));
    Vec2 currentReverseDirection = getPacMan().getDirection().scalarMult(-1);
    return ret.equals(currentReverseDirection) ? Vec2.ZERO : ret;
  }

  /**
   * Get the shortest distance (Manhattan metric) between two points.
   * @param start Start point
   * @param target Ent point.
   * @return Manhattan distance.
   */
  public int getDistanceBFS(TileCoordinates start, TileCoordinates target) {
    Vec2[] directions = {new Vec2(-1, 0), new Vec2(1, 0), new Vec2(0, 1), new Vec2(0, -1)};
    Set<TileCoordinates> visited = new HashSet<>();
    Queue<TileCoordinates> qu = new LinkedList<>();
    qu.add(start);
    int distance = 0;
    while (!qu.isEmpty()) {
      int levelSize = qu.size();
      for (int k = 0; k < levelSize; k++) {
        TileCoordinates current = qu.remove();
        if (visited.contains(current)) {
          continue;
        }
        visited.add(current);
        if (current.equals(target)) {
          return distance;
        }
        for (Vec2 direction : directions) {
          Vec2 adjPosition = current.toVec2().add(direction);
          TileCoordinates coord = new TileCoordinates(adjPosition);
          if (getPacmanGrid().inBoundaries(coord) && isOpenToPacman(adjPosition)) {
            qu.add(coord);
          }
        }
      }
      distance++;
    }
    return Integer.MAX_VALUE;
  }

  @Override
  protected double getCandDistance(Vec2 start, Vec2 end) {
    return getDistanceBFS(new TileCoordinates(start), new TileCoordinates(end));
  }
}
