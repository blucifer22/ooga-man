package ooga.model;

public interface InputSource {
  Vec2 getMovementDirection();
  boolean isActionPressed(ActionInput actionInput);
  enum ActionInput {
    FIRE,
    SPEED_UP,
    SLOW_DOWN,
    RANDOM_TELEPORT
  }
}
