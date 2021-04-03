package ooga.util;

/**
 * This class is a representation of the 2D vector needed for turtle positions. For example, when
 * a turtle position is updated, it may use any of these methods to update the Vec2 of the TurtleModel.
 *
 * @author Mindy Wu
 */
public class Vec2 {
  private double x;
  private double y;

  public Vec2() {
    this.x = 0;
    this.y = 0;
  }

  public Vec2(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vec2 add(Vec2 other) {
    Vec2 sum = new Vec2();
    sum.x = this.x + other.x;
    sum.y = this.y + other.y;
    return sum;
  }

  public Vec2 subtract(Vec2 other) {
    Vec2 diff = new Vec2();
    diff.x = this.x - other.x;
    diff.y = this.y - other.y;
    return diff;
  }

  public double dot(Vec2 other) {
    return this.x * other.x + this.y * other.y;
  }

  /**
   * Return the angle between this vector and another vector.
   *
   * @return angle, in degrees
   */
  public double angleTo(Vec2 other) {
    return Math.toDegrees(Math.acos(this.dot(other) / this.getMagnitude() / other.getMagnitude()));
  }

  public Vec2 scalarMult(double scalar) {
    Vec2 mult = new Vec2();
    mult.x = this.x * scalar;
    mult.y = this.y * scalar;
    return mult;
  }

  public double getMagnitude() {
    return Math.sqrt(this.x*this.x + this.y*this.y);
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public static Vec2 unitVectorPointing(double angleDegrees) {
    return new Vec2(Math.cos(Math.toRadians(angleDegrees)),
                    Math.sin(Math.toRadians(angleDegrees)));
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof Vec2) {
      Vec2 vec = (Vec2) o;
      return vec.x == this.x && vec.y == this.y;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return (int) (this.x + this.y);
  }
}
