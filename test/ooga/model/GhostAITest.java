package ooga.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ooga.model.sprites.Blinky;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Home;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.animation.StillAnimation;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GhostAITest {

  private PacMan pacMan;
  private Ghost blinky;
  private Home home;
  private PacmanGameState state;
  private PacmanGrid grid;

  @BeforeEach
  public void setupPacMan() {
    pacMan = new PacMan(new SpriteCoordinates(new Vec2(4.5, 0.5)), new Vec2(0, -1), 5);
    PacmanGrid grid = new PacmanGrid(6, 2);
    for (int y = 0; y < 2; y++) {
      for (int x = 0; x < 6; x++) {
        grid.setTile(x, y, new Tile(new TileCoordinates(x, y), "", true, true));
      }
    }
    this.grid = grid;
    state = new PacmanGameState();
    state.loadGrid(grid);
  }

  @Test
  public void testBlinkyDecisionUpwards() {
    blinky = new Blinky(new SpriteCoordinates(new Vec2(1.5, 1.9)), new Vec2(0, -1), 4);
    home = new Home(new StillAnimation("home"), new SpriteCoordinates(new Vec2(1.5, 1.9)), new Vec2(0, 0));
    InputSource in = new GhostAI(grid, blinky, pacMan, home,-1);
    blinky.setInputSource(in);
    blinky.step(1/60., state);
    Vec2 req = in.getRequestedDirection();
    assertEquals(new Vec2(1, 0), req);
  }

  @Test
  public void testInitialWait() {
    blinky = new Blinky(new SpriteCoordinates(new Vec2(1.5, 1.9)), new Vec2(0, -1), 4);
    InputSource in = new ChaseAI(grid, blinky, pacMan, -1);
    blinky.setInputSource(in);
    for (int k = 0; k < 30; k++){
      blinky.step(1/60., state);
    }
    assertEquals(GhostBehavior.WAIT, blinky.getGhostBehavior());
    for (int k = 0; k < 240; k++){
      blinky.step(1/60., state);
    }
    assertEquals(GhostBehavior.CHASE, blinky.getGhostBehavior());
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
