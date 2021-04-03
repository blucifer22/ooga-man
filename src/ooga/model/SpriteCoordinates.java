package ooga.model;

import ooga.util.Vec2;

public class SpriteCoordinates {

  private final Vec2 position;

  public SpriteCoordinates(Vec2 position) {
    this.position = position;
  }

  public TileCoordinates getTileCoordinates() {
    double x = position.getX();
    double y = position.getY();
    return new TileCoordinates((int) x, (int) y);
  }

//  public Vec2 getTileOffset() {
//    return null;
//  }
}