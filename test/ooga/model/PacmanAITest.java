package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import ooga.model.sprites.Blinky;
import ooga.model.sprites.PacMan;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PacmanAITest {

  private PacmanBasicAI pacmanIn;
  private PacMan pacman;

  @BeforeEach
  public void setup() {
    // Grid set-up:
    // _ P _ _ _ G
    // _ _ _ _ _ G

    pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 0.5)), new Vec2(0, -1), 5);
    PacmanGrid grid = new PacmanGrid(6, 2);
    for (int y = 0; y < 2; y++) {
      for (int x = 0; x < 6; x++) {
        grid.setTile(x, y, new Tile(new TileCoordinates(x, y), "", true, true));
      }
    }
    Blinky ghost1 = new Blinky(new SpriteCoordinates(new Vec2(5.5, 0.5)), new Vec2(0, -1), 2);
    Blinky ghost2 = new Blinky(new SpriteCoordinates(new Vec2(5.5, 1.5)), new Vec2(0, -1), 2);
    pacmanIn = new PacmanBasicAI(grid, pacman);
    pacmanIn.addTarget(ghost1);
    pacmanIn.addTarget(ghost2);
  }

  @Test
  public void testMaximizeDistanceMoveLeft() {
    // Pac-Man attempts to move left to maximize the distance between it and the ghosts
    assertEquals(new Vec2(-1, 0), pacmanIn.getRequestedDirection());
  }
}
