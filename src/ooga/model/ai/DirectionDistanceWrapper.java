package ooga.model.ai;

import ooga.util.Vec2;
import org.jetbrains.annotations.NotNull;

/** @author George Hong */
public class DirectionDistanceWrapper implements Comparable<DirectionDistanceWrapper> {

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
