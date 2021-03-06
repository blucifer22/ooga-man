package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import ooga.model.grid.PacmanGrid;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.grid.Tile;
import ooga.model.grid.TileCoordinates;
import ooga.model.sprites.PacMan;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpriteInternalDataTests {

  private PacMan pacMan;
  private PacmanGameState state;
  private PacmanGrid grid;

  @BeforeEach
  public void setupPacMan() {
    int[][] protoGrid = {
      {1, 1, 1, 1, 1, 1},
      {1, 0, 1, 1, 1, 1},
      {1, 0, 0, 0, 0, 1},
      {1, 1, 1, 1, 1, 1},
    };
    grid = new PacmanGrid(6, 4);
    for (int j = 0; j < protoGrid.length; j++) {
      for (int k = 0; k < protoGrid[0].length; k++) {
        Tile tile =
            protoGrid[j][k] == 0
                ? new Tile(new TileCoordinates(k, j), null, true, false)
                : new Tile(new TileCoordinates(k, j), null, false, false);
        grid.setTile(k, j, tile);
      }
    }
    state = new PacmanGameState();
    state.loadGrid(grid);
    Vec2 position = new Vec2(4.5, 2.5);
    Vec2 direction = new Vec2(-1, 0);
    SpriteCoordinates spriteCoordinates = new SpriteCoordinates(position);
    pacMan = new PacMan(spriteCoordinates, direction, 11);
    pacMan.unfreeze();
    state.setPlayers(new Player(1, new HumanInputManager(KeybindingType.PLAYER_1)), null);
  }

  @Test
  public void moveLeftTest() {
    List<Vec2> prepopulatedActions = new ArrayList<>();
    for (int j = 0; j < 1000; j++) {
      prepopulatedActions.add(new Vec2(-1, 0));
    }
    SeededTestInputSource input = new SeededTestInputSource();
    input.addActions(prepopulatedActions);

    pacMan.setInputSource(input);

    // Check that the defaultInputSource is set correctly
    assertNotNull(pacMan.getDefaultInputSource());

    for (int k = 0; k < 500; k++) {
      pacMan.step(1.0 / 60, state);
    }

    assertEquals(new Vec2(1.5, 2.5), pacMan.getCoordinates().getPosition());
  }

  @Test
  public void cannotMoveUpRightDownTest() {
    List<Vec2> prepopulatedActions = new ArrayList<>();
    prepopulatedActions.add(new Vec2(1, 0)); // RIGHT
    prepopulatedActions.add(new Vec2(0, -1)); // UP
    prepopulatedActions.add(new Vec2(1, 0)); // RIGHT
    prepopulatedActions.add(new Vec2(0, 1)); // DOWN
    prepopulatedActions.add(new Vec2(-1, 0)); // LEFT
    for (int k = 0; k < 50; k++) {
      prepopulatedActions.add(Vec2.ZERO);
    }
    SeededTestInputSource input = new SeededTestInputSource();
    input.addActions(prepopulatedActions);
    pacMan.setInputSource(input);

    for (int k = 0; k < 4; k++) {
      pacMan.step(1.0 / 60, state);
    }

    assertEquals(new Vec2(4.5, 2.5), pacMan.getCoordinates().getPosition());

    for (int k = 0; k < 40; k++) {
      pacMan.step(1.0 / 60, state);
    }

    assertEquals(new Vec2(1.5, 2.5), pacMan.getCoordinates().getPosition());
  }

  @Test
  public void moveUpStickyTest() {
    List<Vec2> prepopulatedActions = new ArrayList<>();
    for (int j = 0; j < 4; j++) {
      prepopulatedActions.add(new Vec2(-1, 0));
    }
    prepopulatedActions.add(new Vec2(0, -1));
    for (int j = 0; j < 100; j++) {
      prepopulatedActions.add(new Vec2(-1, 0));
    }
    SeededTestInputSource input = new SeededTestInputSource();
    input.addActions(prepopulatedActions);
    pacMan.setInputSource(input);

    for (int k = 0; k < 100; k++) {
      pacMan.step(1.0 / 60, state);
    }

    assertEquals(new Vec2(1.5, 1.5), pacMan.getCoordinates().getPosition());
  }

  @Test
  public void immediateLeftRightTest() {
    List<Vec2> prepopulatedActions = new ArrayList<>();
    for (int j = 0; j < 2; j++) {
      prepopulatedActions.add(new Vec2(-1, 0));
    }
    for (int j = 0; j < 2; j++) {
      prepopulatedActions.add(new Vec2(1, 0));
    }
    SeededTestInputSource input = new SeededTestInputSource();
    input.addActions(prepopulatedActions);

    pacMan.setInputSource(input);

    for (int k = 0; k < 4; k++) {
      pacMan.step(1.0 / 60, state);
    }

    assertEquals(new Vec2(4.5, 2.5), pacMan.getCoordinates().getPosition());
  }

  @Test
  public void checkTileCenterTest() {
    Vec2[] inputVec = {
      new Vec2(0, 0), new Vec2(3, 4), new Vec2(4, 2),
    };
    Vec2[] expectedVec = {
      new Vec2(0.5, 0.5), new Vec2(3.5, 4.5), new Vec2(4.5, 2.5),
    };
    for (int k = 0; k < inputVec.length; k++) {
      SpriteCoordinates spriteCoordinates = new SpriteCoordinates(inputVec[k]);
      assertEquals(expectedVec[k], spriteCoordinates.getTileCenter());
    }
  }

  @Test
  public void parallelVectorTestTrue() {
    Vec2[] inputList1 = {
      new Vec2(-1, 0), new Vec2(0, -1), new Vec2(1, 0), new Vec2(1, 1),
    };
    Vec2[] inputList2 = {new Vec2(1, 0), new Vec2(0, 1), new Vec2(-1, 0), new Vec2(-1, -1)};
    for (int k = 0; k < inputList1.length; k++) {
      assertTrue(inputList1[k].parallelTo(inputList2[k]));
    }
  }

  @Test
  public void parallelVectorTestFalse() {
    Vec2[] inputList1 = {
      new Vec2(0, 1), new Vec2(1, 0), new Vec2(-1, 0), new Vec2(1, 1),
    };
    Vec2[] inputList2 = {new Vec2(1, 0), new Vec2(0, 1), new Vec2(0, 1), new Vec2(-1, 0)};
    for (int k = 0; k < inputList1.length; k++) {
      assertFalse(inputList1[k].parallelTo(inputList2[k]));
    }
  }

  @Test
  public void distanceTest() {
    Vec2[] inputList1 = {
      new Vec2(0, 1), new Vec2(-1, -1),
    };
    Vec2[] inputList2 = {
      new Vec2(0, 2), new Vec2(1, 1),
    };
    double[] expected = {
      1, 2 * Math.sqrt(2),
    };
    for (int k = 0; k < inputList1.length; k++) {
      Vec2 vecA = inputList1[k];
      Vec2 vecB = inputList2[k];
      double expectedValue = expected[k];
      assertEquals(expectedValue, vecA.distance(vecB), Vec2.EPSILON);
    }
  }

  @Test
  public void inBetweenTest() {
    Vec2 center = new Vec2(0.5, 0.5);
    Vec2[] inputList1 = {
      new Vec2(0.5, 0),
      new Vec2(0.5, 0.75),
      new Vec2(0, 0.5),
      new Vec2(0.45, 0.5),
      new Vec2(0.45, 0.5),
    };
    Vec2[] inputList2 = {
      new Vec2(0.5, 0.75),
      new Vec2(0.5, 0),
      new Vec2(1, 0.5),
      new Vec2(0.51, 0.5),
      new Vec2(0.51, 0.51),
    };
    boolean[] expected = {
      true, true, true, true, false,
    };
    for (int k = 0; k < inputList1.length; k++) {
      Vec2 vecA = inputList1[k];
      Vec2 vecB = inputList2[k];
      boolean expectedValue = expected[k];
      assertEquals(expectedValue, center.isBetween(vecA, vecB));
    }
  }

  @Test
  public void truncationTest() {
    TileCoordinates actualTileCoords = pacMan.getCoordinates().getTileCoordinates();
    TileCoordinates expectedTileCoords = new TileCoordinates(4, 2);
    assertEquals(expectedTileCoords, actualTileCoords);
  }

  @Test
  public void initialStatesSavedTest() {
    Vec2 actualDirection = pacMan.getDirection();
    Vec2 actualPosition = pacMan.getCoordinates().getPosition();
    assertEquals(new Vec2(-1, 0), actualDirection);
    assertEquals(new Vec2(4.5, 2.5), actualPosition);
    assertEquals(11, pacMan.getMovementSpeed());
  }
}
