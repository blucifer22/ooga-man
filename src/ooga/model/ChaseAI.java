package ooga.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.PacMan;
import ooga.util.Vec2;
import org.jetbrains.annotations.NotNull;

/**
 * AI implementation for a Ghost to track a target
 *
 * @author George Hong
 */
public class ChaseAI extends GhostAI {

  public ChaseAI(PacmanGrid grid, Ghost ghost, PacMan target,
      double intelligence) {
    super(grid, ghost, target, intelligence);
  }

  /**
   * The ChaseAI algorithm retrieves the currentTilePosition of its targets.  At any point in
   * time, it considers all 4 possible options.  It attempts to choose the options resulting in the
   * smallest distance between it and its target.  If walls obstruct an option, it will attempt to
   * choose the next best option.  The ChaseAI will not select a direction opposite of its current
   * direction, consistent with the rules of classic Pac-Man.
   *
   * @return direction to move the ghost
   */
  @Override
  public Vec2 getRequestedDirection() {
    Vec2 targetTilePos = getTarget().getCoordinates().getTileCoordinates().toVec2();
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();

    return reduceDistance(targetTilePos, currentTilePos);
  }

  @NotNull
  protected Vec2 reduceDistance(Vec2 targetTilePos, Vec2 currentTilePos) {
    Vec2[] directions = {
        new Vec2(-1, 0),
        new Vec2(1, 0),
        new Vec2(0, 1),
        new Vec2(0, -1)
    };

    List<DirectionDistanceWrapper> distances = new ArrayList<>();
    for (Vec2 direction : directions) {
      Vec2 nextPos = currentTilePos.add(direction);
      distances.add(new DirectionDistanceWrapper(direction, nextPos.distance(targetTilePos)));
    }
    Collections.sort(distances);
    Vec2 currentReverseDirection = getGhost().getDirection().scalarMult(-1);
    Queue<DirectionDistanceWrapper> queue = new LinkedList<>(distances);

    while (!queue.isEmpty()) {
      Vec2 cand = queue.remove().getVec();
      Vec2 target = currentTilePos.add(cand);
      if (!isOpenToGhosts(target) || cand.equals(currentReverseDirection)) {
        continue;
      }
      return cand;
    }
    return Vec2.ZERO;
  }

  private boolean isOpenToGhosts(Vec2 target) {
    return getPacmanGrid().getTile(new TileCoordinates((int) target.getX(), (int) target.getY()))
        .isOpenToGhosts();
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
    public int compareTo(@NotNull ChaseAI.DirectionDistanceWrapper o) {
      return Double.compare(this.dis, o.dis);
    }
  }
}
