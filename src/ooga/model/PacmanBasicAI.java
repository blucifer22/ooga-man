package ooga.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;
import org.jetbrains.annotations.NotNull;

/**
 * Basic AI for Pac-Man that focuses on maximizing distance between all included target Sprites
 *
 * @author George Hong
 */
public class PacmanBasicAI implements InputSource {

  private final List<Sprite> targets;
  private final PacmanGrid pacmanGrid;
  private final Sprite pacMan;

  public PacmanBasicAI(PacmanGrid grid, Sprite pacMan) {
    this.pacMan = pacMan;
    targets = new ArrayList<>();
    pacmanGrid = grid;
  }

  private int getDistanceBFS(TileCoordinates start, TileCoordinates target) {
    Vec2[] directions = {
        new Vec2(-1, 0),
        new Vec2(1, 0),
        new Vec2(0, 1),
        new Vec2(0, -1)
    };
    Set<TileCoordinates> visited = new HashSet<>();
    Queue<TileCoordinates> qu = new LinkedList<>();
    qu.add(start);
    int distance = 0;
    while (!qu.isEmpty()) {
      Set<TileCoordinates> nextLevel = new HashSet<>();
      for (int k = 0; k < qu.size(); k++) {
        TileCoordinates current = qu.remove();
        visited.add(current);

        if (current.equals(target)) {
          return distance;
        }
        for (Vec2 direction : directions) {
          Vec2 adjPosition = current.toVec2().add(direction);
          TileCoordinates coord = new TileCoordinates(adjPosition);
          if (pacmanGrid.inBoundaries(coord) && isOpenToPacman(
              adjPosition)) {
            nextLevel.add(coord);
          }
        }
      }
      distance++;
      for (TileCoordinates next : nextLevel) {
        if (!visited.contains(next)) {
          qu.add(next);
        }
      }
    }
    return -1;
  }

  /**
   * Add Sprite to be tracked by the movement algorithm.  The algorithm will try to maximize the
   * distance between Pac-Man and these sprites.
   *
   * @param sprite Sprites to move away from.
   */
  public void addTarget(Sprite sprite) {
    targets.add(sprite);
  }

  /**
   * The Pac-Man AI is focused on survival.  It considers the position of all ghosts in the game.
   * For each direction that it can choose, it considers the closest Sprite in that direction to
   * make decisions.  The final direction chosen is an attempt to maximize the closest distance.
   * This AI can freely reverse, if necessary.
   *
   * @param currentTilePos current position of this AI
   * @return Direction to send to the Sprite
   */
  protected Vec2 maximizeDistance(Vec2 currentTilePos) {
    Vec2[] directions = {
        new Vec2(0, 0),
        new Vec2(-1, 0),
        new Vec2(1, 0),
        new Vec2(0, 1),
        new Vec2(0, -1)
    };

    List<DirectionDistanceWrapper> distances = new ArrayList<>();
    for (Vec2 direction : directions) {
      Vec2 nextPos = currentTilePos.add(direction);
      double closestDistance = Double.MAX_VALUE;
      for (Sprite target : targets) {
        Vec2 targetTilePos = target.getCoordinates().getTileCoordinates().toVec2();
//        double candDistance = getDistanceBFS(new TileCoordinates(nextPos),
//            new TileCoordinates(targetTilePos));
        double candDistance = nextPos.distance(targetTilePos);
        closestDistance = Math.min(candDistance, closestDistance);
      }
      distances.add(new DirectionDistanceWrapper(direction, closestDistance));
    }

    Collections.sort(distances, Collections.reverseOrder());
    //Vec2 currentReverseDirection = getGhost().getDirection().scalarMult(-1);
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

  private boolean isOpenToPacman(Vec2 target) {
    return pacmanGrid.getTile(new TileCoordinates((int) target.getX(), (int) target.getY()))
        .isOpenToGhosts();
  }

  @Override
  public Vec2 getRequestedDirection() {
    Vec2 currentTilePos = pacMan.getCoordinates().getTileCoordinates().toVec2();
    return maximizeDistance(currentTilePos);
  }

  @Override
  public boolean isActionPressed() {
    return false;
  }

  class DirectionDistanceWrapper implements Comparable<DirectionDistanceWrapper> {

    private final Vec2 vec;
    private final double dis;

    public DirectionDistanceWrapper(Vec2 vec, double dis) {
      this.vec = vec;
      this.dis = dis;
    }

    public Vec2 getVec() {
      return vec;
    }

    @Override
    public int compareTo(@NotNull DirectionDistanceWrapper o) {
      return Double.compare(this.dis, o.dis);
    }
  }


}
