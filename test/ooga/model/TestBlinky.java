package ooga.model;

import ooga.model.sprites.Blinky;
import ooga.util.Vec2;

/**
 * Default Blinky, that immediately responds to input and has no Pen delay
 */
public class TestBlinky extends Blinky {

  public TestBlinky(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
  }

  @Override
  protected double getInitialWaitTime() {
    return 0;
  }
}
