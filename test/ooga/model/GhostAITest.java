package ooga.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import ooga.model.ai.ClydeAI;
import ooga.model.ai.GhostAI;
import ooga.model.ai.InkyAI;
import ooga.model.ai.PinkyAI;
import ooga.model.sprites.*;
import ooga.model.sprites.Ghost.GhostBehavior;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GhostAITest {

  private PacMan pacMan;
  private Ghost blinky;
  private PacmanGameState state;
  private PacmanGrid grid;

  @BeforeEach
  public void setupPacMan() {
    /*
    _ _ _ _ _ _ _ _ _ _ _ _
    _ _ _ _ _ _ _ _ _ _ _ _
    _ _ _ _ P _ _ _ _ _ _ _
    _ _ _ _ _ _ _ _ _ _ _ _
    _ _ _ _ _ _ _ _ _ _ _ _
     */
    pacMan = new PacMan(new SpriteCoordinates(new Vec2(4.5, 2.5)), new Vec2(0, -1), 5);
    PacmanGrid grid = new PacmanGrid(12, 5);
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 12; x++) {
        grid.setTile(x, y, new Tile(new TileCoordinates(x, y), "", true, true));
      }
    }
    this.grid = grid;
    state = new PacmanGameState();
    state.loadGrid(grid);
    state.setPlayers(new Player(1, new HumanInputManager(KeybindingType.PLAYER_1)), null);
  }

  @Test
  public void testInkyInitialWait() {
    Inky inky = new Inky(new SpriteCoordinates(new Vec2(1.5, 2.5)), new Vec2(0, -1), 4);
    PinkyAI in = new PinkyAI(grid, inky);
    in.setTarget(pacMan);
    inky.setInputSource(in);
    inky.step(1 / 60., state);
    assertEquals(GhostBehavior.WAIT, inky.getGhostBehavior());
  }

  @Test
  public void testInkyNarrowDistance() {
    /*
    _ _ _ _ _ _ _ _ _ _ _ _
    _ _ _ _ _ _ _ _ _ _ _ _
    _ P _ _ _ _ I _ _ _ _ _
    _ _ _ _ _ _ _ _ _ _ _ _
    _ _ _ _ _ _ _ _ _ _ _ _
     */
    pacMan = new PacMan(new SpriteCoordinates(new Vec2(1.5, 2.5)), new Vec2(0, 0), 5);
    Inky clyde = new Inky(new SpriteCoordinates(new Vec2(6.5, 2.5)), new Vec2(1, 0), 2);
    InkyAI in = new InkyAI(grid, clyde);
    in.setTarget(pacMan);
    clyde.setInputSource(in);
    pacMan.unfreeze();
    clyde.onGameEvent(GameEvent.SPRITES_UNFROZEN);
    for (int k = 0; k < 461; k++) {
      clyde.step(1 / 60., state);
    }
    Vec2 req = in.getRequestedDirection(1/60.0);
    assertEquals(new Vec2(-1, 0), req);
  }

  @Test
  public void testInitialWait() {
    blinky = new Blinky(new SpriteCoordinates(new Vec2(1.5, 1.9)), new Vec2(0, -1), 4);
    GhostAI AI = new GhostAI(grid, blinky);
    AI.setTarget(pacMan);
    blinky.setInputSource(AI);
    for (int k = 0; k < 30; k++) {
      blinky.step(1 / 60., state);
    }
    blinky.unfreeze();
    blinky.onGameEvent(GameEvent.SPRITES_UNFROZEN);
    assertEquals(GhostBehavior.WAIT, blinky.getGhostBehavior());
    for (int k = 0; k < 240; k++) {
      blinky.step(1 / 60., state);
    }
    assertEquals(GhostBehavior.CHASE, blinky.getGhostBehavior());
  }

  @Test
  public void testResetInitialWait() {
    blinky = new Blinky(new SpriteCoordinates(new Vec2(1.5, 1.9)), new Vec2(0, -1), 4);
    GhostAI AI = new GhostAI(grid, blinky);
    AI.setTarget(pacMan);
    blinky.setInputSource(AI);
    blinky.onGameEvent(GameEvent.SPRITES_UNFROZEN);
    for (int k = 0; k < 30; k++) {
      blinky.step(1 / 60., state);
    }
    assertEquals(GhostBehavior.WAIT, blinky.getGhostBehavior());
    for (int k = 0; k < 240; k++) {
      blinky.step(1 / 60., state);
    }
    assertEquals(GhostBehavior.CHASE, blinky.getGhostBehavior());

    for (int k = 0; k < 30; k++) {
      blinky.step(1 / 60., state);
    }
    blinky.reset();
    assertEquals(GhostBehavior.WAIT, blinky.getGhostBehavior());
  }


  @Test
  public void testParallelSort() {
    double[] directionsArray = {5, 7, 8};
    List<Double> directions = new ArrayList<>();
    for (double val : directionsArray) {
      directions.add(val);
    }
    double[] distancesArray = {45, 7, 5};
    List<Double> distances = new ArrayList<>();
    for (double val : distancesArray) {
      distances.add(val);
    }

    List<Double> expectedOut = new ArrayList<>();
    expectedOut.add(8.0);
    expectedOut.add(7.0);
    expectedOut.add(5.0);
    // https://stackoverflow.com/questions/18129807/in-java-how-do-you-sort-one-list-based-on-another
    Collections.sort(directions, Comparator.comparing(item -> distances.indexOf(item)));
    assertEquals(expectedOut, directions);
  }


}
