package ooga.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import ooga.model.sprites.Dot;
import ooga.model.sprites.PacMan;
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
    Vec2 position = new Vec2(1.5, 2.5);
    Vec2 direction = new Vec2(-1, 0);
    SpriteCoordinates spriteCoordinates = new SpriteCoordinates(position);
    pacMan = new PacMan(spriteCoordinates, direction, 11);
    dot = new Dot(new SpriteCoordinates(new Vec2(1.5, 1.5)), Vec2.ZERO);
    state.addSprite(pacMan);
    state.addSprite(dot);
  }

  @Test
  public void collisionWithDotIncrementsScore() {

    class TestInputSource implements InputSource {

      private final List<Vec2> prepopulatedActions = new ArrayList<>();
      private int dex = 0;

      public TestInputSource() {
        for (int j = 0; j < 1000; j++) {
          prepopulatedActions.add(new Vec2(0, -1));
        }
      }

      @Override
      public Vec2 getRequestedDirection() {
        return prepopulatedActions.get(dex++);
      }

      @Override
      public boolean isActionPressed() {
        return false;
      }
    }
    TestInputSource input = new TestInputSource() {
    };
    pacMan.setInputSource(input);

    for (int k = 0; k < 1000; k++){
      pacMan.step(1 / 60., grid, state);
    }
    assertTrue(state.getScore() > 0);
  }
}
