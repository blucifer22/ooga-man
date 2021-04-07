package ooga.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class implements a double-precision 2-vector over the reals.
 * <p>
 * This is a utility class that was imported verbatim from one of our previous projects.
 *
 * @author Mindy Wu
 * @author Franklin Wei
 * @author George Hong
 */
public class Vec2 {

  public static final Vec2 ZERO = new Vec2(0, 0);
  public static final double EPSILON = 1E-6;
  private final double x;
  private final double y;

  @JsonCreator
  public Vec2() {
    this.x = 0;
    this.y = 0;
  }

  @JsonCreator
  public Vec2(@JsonProperty("x") double x, @JsonProperty("y") double y) {
    this.x = x;
    this.y = y;
  }

  public static Vec2 unitVectorPointing(double angleDegrees) {
    return new Vec2(Math.cos(Math.toRadians(angleDegrees)),
        Math.sin(Math.toRadians(angleDegrees)));
  }

  public Vec2 add(Vec2 other) {
    return new Vec2(this.x + other.x,
                    this.y + other.y);
  }

  public Vec2 subtract(Vec2 other) {
    return new Vec2(this.x - other.x,
                    this.y - other.y);
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
    return new Vec2(this.x * scalar,
                    this.y * scalar);
  }

  @JsonIgnore
  public double getMagnitude() {
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }

  @JsonGetter
  public double getX() {
    return this.x;
  }

  @JsonGetter
  public double getY() {
    return this.y;
  }

  /**
   * Returns whether two vectors are parallel to each other
   *
   * @param other vector to consider
   * @return boolean representing whether two vectors are parallel
   */
  public boolean parallelTo(Vec2 other) {
    return Math.abs(this.angleTo(other)) < EPSILON ||
        Math.abs(this.angleTo(other) - 180) < EPSILON;
  }

  /**
   * Calculates the distance between two vectors.
   *
   * @param other
   * @return
   */
  public double distance(Vec2 other) {
    Vec2 difference = this.subtract(other);
    return difference.getMagnitude();
  }

  /**
   * Returns whether this vector resides on the line connecting two points
   *
   * @param vectorA one of the two endpoints
   * @param vectorB other endpoint
   * @return boolean representing whether this vector resides on the line connecting two points.
   */
  public boolean isBetween(Vec2 vectorA, Vec2 vectorB) {
    // https://stackoverflow.com/questions/328107/how-can-you-determine-a-point-is-between-two-other-points-on-a-line-segment
    double shortestDistance = vectorA.distance(vectorB);
    double distanceAll = this.distance(vectorA) + this.distance(vectorB);
    return Math.abs(distanceAll - shortestDistance) < EPSILON;
  }


  @Override
  public boolean equals(Object o) {
    if (o instanceof Vec2) {
      Vec2 vec = (Vec2) o;
      return vec.x == this.x && vec.y == this.y;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return (int) (this.x + this.y);
  }

  @Override
  public String toString(){
    return "( " + getX() + ", " + getY() + " )";
  }
}
