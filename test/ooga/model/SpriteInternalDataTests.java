package ooga.model;

import static org.junit.jupiter.api.Assertions.*;

import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpriteInternalDataTests {

  private PacMan pacMan;

  @BeforeEach
  public void setupPacMan() {
    Vec2 position = new Vec2(2.5, 1.5);
    Vec2 direction = new Vec2(1, 0);
    SpriteCoordinates spriteCoordinates = new SpriteCoordinates(position);
    pacMan = new PacMan(spriteCoordinates, direction, 10);
  }

  @Test
  public void truncationTest() {
    TileCoordinates actualTileCoords = pacMan.getCoordinates().getTileCoordinates();
    TileCoordinates expectedTileCoords = new TileCoordinates(2, 1);
    assertEquals(expectedTileCoords, actualTileCoords);
  }

  @Test
  public void initialStatesSavedTest() {
    Vec2 actualDirection = pacMan.getDirection();
    Vec2 actualPosition = pacMan.getCoordinates().getExactCoordinates();
    assertEquals(new Vec2(1, 0), actualDirection);
    assertEquals(new Vec2(2.5, 1.5), actualPosition);
    assertEquals(10, pacMan.getSpeed());
  }

}
