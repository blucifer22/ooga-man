package ooga.model;

public abstract interface InputSource {
  Vec2 getMovementDirection();
  enum ActionInput {
    FIRE,
    SPEED_UP,
    SLOW_DOWN,
    RANDOM_TELEPORT
  }
}
