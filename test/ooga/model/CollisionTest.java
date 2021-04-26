package ooga.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import ooga.model.ai.BlinkyAI;
import ooga.model.grid.PacmanGrid;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.grid.Tile;
import ooga.model.grid.TileCoordinates;
import ooga.model.sprites.Blinky;
import ooga.model.sprites.Dot;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.TeleporterOverlay;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CollisionTest {

  private PacMan pacMan;
  private Dot dot;
  private PacmanGameState state;
  private PacmanGrid grid;

  @BeforeEach
  public void setupGame() {
    int[][] protoGrid = {
        {1, 1, 1, 1, 1, 1},
        {1, 0, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1},
    };
    grid = new PacmanGrid(6, 4);
    for (int j = 0; j < protoGrid.length; j++) {
      for (int k = 0; k < protoGrid[0].length; k++) {
        Tile tile = protoGrid[j][k] == 0 ? new Tile(new TileCoordinates(k, j), null, true, false)
            : new Tile(new TileCoordinates(k, j), null, false, false);
        grid.setTile(k, j, tile);
      }
    }
    state = new PacmanGameState();
    dot = new Dot(new SpriteCoordinates(new Vec2(1.5, 1.5)), Vec2.ZERO);
    Dot otherDot = new Dot(new SpriteCoordinates(new Vec2(3.5, 0.5)), Vec2.ZERO);
    state.loadGrid(grid);
    state.addSprite(dot);
    state.addSprite(otherDot);
    state.setPlayers(new Player(1, new HumanInputManager(KeybindingType.PLAYER_1)), null);
  }

  @Test
  public void doubleCollision() {
    /*
    X X X X X X
    X _ X X X X
    X P _ _ _ X
    X X X X X X
     */
    pacMan = new PacMan(new SpriteCoordinates(new Vec2(1.5, 2.5)), new Vec2(-1, 0), 5);
    Blinky blinky1 = new Blinky(new SpriteCoordinates(new Vec2(1.5, 2.5)), new Vec2(-1, 0), 5);
    Blinky blinky2 = new Blinky(new SpriteCoordinates(new Vec2(1.5, 2.5)), new Vec2(-1, 0), 5);
    BlinkyAI ai = new BlinkyAI(grid, blinky1);
    ai.addTarget(pacMan);
    blinky1.setInputSource(ai);
    blinky2.setInputSource(ai);
    pacMan.uponNewLevel(1, state);


    SeededTestInputSource pacmanAI = new SeededTestInputSource();
    for (int k = 0; k < 3000; k++) {
      pacmanAI.addActions(new Vec2(0, 0));
    }
    pacMan.setInputSource(pacmanAI);
//    PacmanBFSAI pacmanBFSAI = new PacmanBFSAI(grid, pacMan);
//    pacmanBFSAI.addTarget(blinky1);
//    pacmanBFSAI.addTarget(blinky2);
//    pacMan.setInputSource(pacmanBFSAI);

    state.addSprite(pacMan);
    state.addSprite(blinky1);
    state.addSprite(blinky2);

    for (int k = 0; k < 840; k++) {
      state.step(1 / 60.);
    }
    state.step(1 / 60.);
    assertEquals(1, state.getSprites().size());
  }

  @Test
  public void collisionWithDotIncrementsScore() {

    Vec2 position = new Vec2(1.5, 2.5);
    Vec2 direction = new Vec2(-1, 0);
    SpriteCoordinates spriteCoordinates = new SpriteCoordinates(position);
    pacMan = new PacMan(spriteCoordinates, direction, 11);
    pacMan.unfreeze();
    state.addSprite(pacMan);

    List<Vec2> prepopulatedActions = new ArrayList<>();

    for (int j = 0; j < 1000; j++) {
      prepopulatedActions.add(new Vec2(0, -1));
    }
    SeededTestInputSource input = new SeededTestInputSource();
    input.addActions(prepopulatedActions);
    pacMan.setInputSource(input);

    for (int k = 0; k < 1000; k++) {
      //pacMan.step(1 / 60., state);
      state.step(1 / 60.);
    }
    assertTrue(state.getScore() > 0);
    assertTrue(state.getSprites().size() == 2);
  }

  @Test
  public void teleporterBasicTest() {
    List<Vec2> prepopulatedActions = new ArrayList<>();

    for (int j = 0; j < 10; j++) {
      prepopulatedActions.add(new Vec2(-1, 0));
    }
    SeededTestInputSource input = new SeededTestInputSource();
    input.addActions(prepopulatedActions);

    Vec2 position = new Vec2(2.5, 2.5);
    Vec2 direction = new Vec2(-1, 0);
    SpriteCoordinates spriteCoordinates = new SpriteCoordinates(position);
    pacMan = new PacMan(spriteCoordinates, direction, 5);
    pacMan.setInputSource(input);
    state.addSprite(pacMan);
    pacMan.unfreeze();

    TeleporterOverlay teleporter1 = new TeleporterOverlay(
        new SpriteCoordinates(new Vec2(1.5, 2.5)));
    TeleporterOverlay teleporter2 = new TeleporterOverlay(
        new SpriteCoordinates(new Vec2(4.5, 2.5)));
    teleporter1.connectTeleporter(teleporter2);
    teleporter2.connectTeleporter(teleporter1);
    state.addSprite(teleporter1);
    state.addSprite(teleporter2);

    for (int k = 0; k < 6; k++) {
      state.step(1 / 60.);
    }
    assertTrue(pacMan.getCoordinates().getPosition().getX() > 2.5);
  }
}
