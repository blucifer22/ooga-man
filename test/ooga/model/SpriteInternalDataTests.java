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
    pacMan = new PacMan(spriteCoordinates, direction, 11);
  }

  @Test
  public void checkTileCenterTest() {
    Vec2[] inputVec = {
        new Vec2(0, 0),
        new Vec2(3, 4),
        new Vec2(4, 2),
    };
    Vec2[] expectedVec = {
        new Vec2(0.5, 0.5),
        new Vec2(3.5, 4.5),
        new Vec2(4.5, 2.5),
    };
    for (int k = 0; k < inputVec.length; k++) {
      SpriteCoordinates spriteCoordinates = new SpriteCoordinates(inputVec[k]);
      assertEquals(expectedVec[k], spriteCoordinates.getTileCenter());
    }
  }

  @Test
  public void parallelVectorTestTrue() {
    Vec2[] inputList1 = {
        new Vec2(-1, 0),
        new Vec2(0, -1),
        new Vec2(1, 0),
        new Vec2(1, 1),
    };
    Vec2[] inputList2 = {
        new Vec2(1, 0),
        new Vec2(0, 1),
        new Vec2(-1, 0),
        new Vec2(-1, -1)
    };
    for (int k = 0; k < inputList1.length; k++) {
      assertTrue(inputList1[k].parallelTo(inputList2[k]));
    }
  }

  @Test
  public void parallelVectorTestFalse() {
    Vec2[] inputList1 = {
        new Vec2(0, 1),
        new Vec2(1, 0),
        new Vec2(-1, 0),
        new Vec2(1, 1),
    };
    Vec2[] inputList2 = {
        new Vec2(1, 0),
        new Vec2(0, 1),
        new Vec2(0, 1),
        new Vec2(-1, 0)
    };
    for (int k = 0; k < inputList1.length; k++) {
      assertFalse(inputList1[k].parallelTo(inputList2[k]));
    }
  }

  @Test
  public void distanceTest() {
    Vec2[] inputList1 = {
        new Vec2(0, 1),
        new Vec2(-1, -1),
    };
    Vec2[] inputList2 = {
        new Vec2(0, 2),
        new Vec2(1, 1),
    };
    double[] expected = {
        1,
        2 * Math.sqrt(2),
    };
    for (int k = 0; k < inputList1.length; k++) {
      Vec2 vecA = inputList1[k];
      Vec2 vecB = inputList2[k];
      double expectedValue = expected[k];
      assertEquals(expectedValue, vecA.distance(vecB), Vec2.EPSILON);
    }
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
