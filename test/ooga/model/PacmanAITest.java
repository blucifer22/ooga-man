package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.ai.PacmanAI;
import ooga.model.ai.PacmanBFSAI;
import ooga.model.grid.PacmanGrid;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.grid.Tile;
import ooga.model.grid.TileCoordinates;
import ooga.model.sprites.Blinky;
import ooga.model.sprites.PacMan;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PacmanAITest {

  private PacmanAI pacmanIn;
  private PacMan pacman;

  @BeforeEach
  public void setup() {
    // Grid set-up:
    // _ _ _ _ _ G
    // _ P _ _ _ G

    pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 0.5)), new Vec2(0, -1), 5);
    PacmanGrid grid = new PacmanGrid(6, 2);
    for (int y = 0; y < 2; y++) {
      for (int x = 0; x < 6; x++) {
        grid.setTile(x, y, new Tile(new TileCoordinates(x, y), "", true, true));
      }
    }
    Blinky ghost1 = new Blinky(new SpriteCoordinates(new Vec2(5.5, 0.5)), new Vec2(0, -1), 2);
    Blinky ghost2 = new Blinky(new SpriteCoordinates(new Vec2(5.5, 1.5)), new Vec2(0, -1), 2);
    pacmanIn = new PacmanAI(grid, pacman);
    pacmanIn.addTarget(ghost1);
    pacmanIn.addTarget(ghost2);
  }

  @Test
  public void testMaximizeDistanceMoveLeft() {
    // Pac-Man attempts to move left to maximize the distance between it and the ghosts
    Vec2 direction = pacmanIn.getRequestedDirection(0.0);
    assertEquals(new Vec2(-1, 0), direction);
  }

  @Test
  public void testBFSAlgorithms() {
    /* T: Target, S: Start, X: End
    X X X X X X
    X T X X X X
    X _ X _ T X
    X S _ _ _ X
    X X X X X X
     */
    int[][] protoGrid = {
      {1, 1, 1, 1, 1, 1},
      {1, 0, 1, 1, 1, 1},
      {1, 0, 1, 0, 0, 1},
      {1, 0, 0, 0, 0, 1},
      {1, 1, 1, 1, 1, 1},
    };
    PacmanGrid grid = new PacmanGrid(protoGrid[0].length, protoGrid.length);
    for (int j = 0; j < protoGrid.length; j++) {
      for (int k = 0; k < protoGrid[0].length; k++) {
        Tile tile =
            protoGrid[j][k] == 0
                ? new Tile(new TileCoordinates(k, j), null, true, false)
                : new Tile(new TileCoordinates(k, j), null, false, false);
        grid.setTile(k, j, tile);
      }
    }

    PacmanBFSAI ai = new PacmanBFSAI(grid, pacman);
    Vec2 start = new Vec2(1, 3);
    Vec2 target1 = new Vec2(1, 1);
    Vec2 target2 = new Vec2(4, 2);
    int result = ai.getDistanceBFS(new TileCoordinates(start), new TileCoordinates(target1));
    assertEquals(2, result);
    result = ai.getDistanceBFS(new TileCoordinates(start), new TileCoordinates(target2));
    assertEquals(4, result);
  }

  @Test
  public void testBFSAI() {
    int[][] protoGrid = {
      {1, 1, 1, 1, 1, 1},
      {1, 0, 1, 1, 1, 1},
      {1, 0, 1, 0, 0, 1},
      {1, 0, 0, 0, 0, 1},
      {1, 1, 1, 1, 1, 1},
    };
    PacmanGrid grid = new PacmanGrid(protoGrid[0].length, protoGrid.length);
    for (int j = 0; j < protoGrid.length; j++) {
      for (int k = 0; k < protoGrid[0].length; k++) {
        Tile tile =
            protoGrid[j][k] == 0
                ? new Tile(new TileCoordinates(k, j), null, true, false)
                : new Tile(new TileCoordinates(k, j), null, false, false);
        grid.setTile(k, j, tile);
      }
    }
    pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(-1, 0), 0);
    PacmanBFSAI ai = new PacmanBFSAI(grid, pacman);
    pacman.setInputSource(ai);
    Blinky blinky = new Blinky(new SpriteCoordinates(new Vec2(4.5, 2.5)), new Vec2(-1, 0), 0);
    ai.addTarget(blinky);

    ai.getRequestedDirection(0.0);
  }

  @Test
  public void testBFSAIRun() {
    /*
    X X X X X X
    X _ X X X X
    X _ X _ _ X
    X _ P _ G X
    X X X X X X
     */
    int[][] protoGrid = {
      {1, 1, 1, 1, 1, 1},
      {1, 0, 1, 1, 1, 1},
      {1, 0, 1, 0, 0, 1},
      {1, 0, 0, 0, 0, 1},
      {1, 1, 1, 1, 1, 1},
    };
    PacmanGrid grid = new PacmanGrid(protoGrid[0].length, protoGrid.length);
    for (int j = 0; j < protoGrid.length; j++) {
      for (int k = 0; k < protoGrid[0].length; k++) {
        Tile tile =
            protoGrid[j][k] == 0
                ? new Tile(new TileCoordinates(k, j), null, true, false)
                : new Tile(new TileCoordinates(k, j), null, false, false);
        grid.setTile(k, j, tile);
      }
    }
    pacman = new PacMan(new SpriteCoordinates(new Vec2(2.5, 3.5)), new Vec2(-1, 0), 0);
    PacmanBFSAI ai = new PacmanBFSAI(grid, pacman);
    pacman.setInputSource(ai);
    Blinky blinky = new Blinky(new SpriteCoordinates(new Vec2(4.5, 2.5)), new Vec2(-1, 0), 0);
    ai.addTarget(blinky);
    Vec2 out = ai.getRequestedDirection(0.0);
    assertEquals(new Vec2(-1, 0), out);
  }

  @Test
  public void scatterBFSPacman() {
    int[][] protoGrid = {
      {1, 1, 1, 1, 1, 1},
      {1, 0, 1, 1, 1, 1},
      {1, 0, 1, 0, 0, 1},
      {1, 0, 0, 0, 0, 1},
      {1, 1, 1, 1, 1, 1},
    };
    PacmanGrid grid = new PacmanGrid(protoGrid[0].length, protoGrid.length);
    for (int j = 0; j < protoGrid.length; j++) {
      for (int k = 0; k < protoGrid[0].length; k++) {
        Tile tile =
            protoGrid[j][k] == 0
                ? new Tile(new TileCoordinates(k, j), null, true, false)
                : new Tile(new TileCoordinates(k, j), null, false, false);
        grid.setTile(k, j, tile);
      }
    }
    pacman = new PacMan(new SpriteCoordinates(new Vec2(2.5, 3.5)), new Vec2(-1, 0), 0);
    PacmanBFSAI ai = new PacmanBFSAI(grid, pacman);
    pacman.setInputSource(ai);
    Blinky blinky = new Blinky(new SpriteCoordinates(new Vec2(16.5, 16.5)), new Vec2(-1, 0), 0);
    ai.addTarget(blinky);
    Vec2 out = ai.getRequestedDirection(0.0);
  }
}
