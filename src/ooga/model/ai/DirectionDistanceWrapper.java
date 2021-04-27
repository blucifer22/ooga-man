package ooga.model.ai;

import ooga.util.Vec2;
import org.jetbrains.annotations.NotNull;

/**
 * Encodes direction choices for Sprite AIs.  These direction options can be wrapped with a distance
 * and provides the means to sort options.  Many default AIs rely on choosing movement options based
 * on minimizing or maximizing distances, and this class is meant to provide that utility.
 *
 * @author George Hong
 */
public class DirectionDistanceWrapper implements Comparable<DirectionDistanceWrapper> {

  private final Vec2 vec;
  private final double dis;

  /**
   * Constructs an instance of this class.
   *
   * @param vec direction associated with a scalar value
   * @param dis scalar value to associate with a given direction.
   */
  public DirectionDistanceWrapper(Vec2 vec, double dis) {
    this.vec = vec;
    this.dis = dis;
  }

  /**
   * Returns the direction stored in this object
   *
   * @return vector direction.
   */
  public Vec2 getVec() {
    return vec;
  }

  /**
   * Compares one direction option with another.  The comparison is through the scalar associated
   * with each direction.
   *
   * @param o other direction-scalar pair
   * @return integer representing which number comes first.
   */
  @Override
  public int compareTo(@NotNull DirectionDistanceWrapper o) {
    return Double.compare(this.dis, o.dis);
  }
}
