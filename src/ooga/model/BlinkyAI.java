package ooga.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.PacMan;
import ooga.util.Vec2;
import org.jetbrains.annotations.NotNull;

/**
 * An aggressive AI analog to the classic Blinky Ghost
 *
 * @author George Hong
 */
public class BlinkyAI extends GhostAI {

  public BlinkyAI(PacmanGrid grid, Ghost ghost, PacMan target,
      double intelligence) {
    super(grid, ghost, target, intelligence);
  }

  @Override
  public Vec2 getRequestedDirection() {
    Vec2 targetTilePos = getTarget().getCoordinates().getTileCoordinates().toVec2();
    Vec2 currentTilePos = getSelf().getCoordinates().getTileCoordinates().toVec2();

    List<Vec2> directions = new ArrayList<>();
    directions.add(new Vec2(-1.0, 0));
    directions.add(new Vec2(1.0, 0));
    directions.add(new Vec2(0.0, 1.0));
    directions.add(new Vec2(0.0, -1.0));

    List<DirectionDistanceWrapper> distances = new ArrayList<>();
    for (Vec2 direction : directions) {
      Vec2 nextPos = currentTilePos.add(direction);
      distances.add(new DirectionDistanceWrapper(direction, nextPos.distance(targetTilePos)));
    }
    Collections.sort(distances);
    //Collections.sort(directions, Comparator.comparing(item -> distances.indexOf(item)));
    Vec2 currentReverseDirection = getSelf().getDirection().scalarMult(-1);
    while (directions.size() > 0) {
      Vec2 cand = distances.remove(0).getVec();
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
    public int compareTo(@NotNull BlinkyAI.DirectionDistanceWrapper o) {
      return Double.compare(this.dis, o.dis);
    }
  }
}
