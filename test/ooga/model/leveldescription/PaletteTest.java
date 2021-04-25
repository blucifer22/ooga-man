package ooga.model.leveldescription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import ooga.model.PacmanGameState;
import ooga.model.PacmanLevel;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaletteTest {

  private Palette palette;
  private LevelBuilder levelBuilder;

  @BeforeEach
  public void beforeEach() {
    palette = new Palette();
    levelBuilder = new LevelBuilder();
  }

  @Test
  public void loadKeysTest() {
    List<String> names = palette.getSpriteNames();
    String[] allNames = {
        "Blinky",
        "Inky",
        "Clyde",
        "Pinky",
        "PacMan",
        "Cherry",
        "Dot",
        "PowerPill"
    };
    for (String requiredName : allNames) {
      assertTrue(names.contains(requiredName));
    }
  }

  @Test
  public void testPalette(){
    levelBuilder.setGridSize(5, 5);
    levelBuilder.addSprite(3,3);
    assertEquals(1, levelBuilder.getLevel().getSprites().size());
    for (int i=1; i < 10; i++){
      levelBuilder.addSprite(3,3);
      assertEquals(i + 1, levelBuilder.getLevel().getSprites().size());
    }
    levelBuilder.clearSpritesOnTile(3,3);
    assertEquals(0, levelBuilder.getLevel().getSprites().size());
  }

  @Test
  public void testPokeTile() {
    levelBuilder.setGridSize(5, 5);
    Tile tile = new Tile(new TileCoordinates(3, 3), "tileclosed", false, false);
    levelBuilder.getLevel().getGrid()
        .setTile(tile.getCoordinates().getX(), tile.getCoordinates().getY(), tile);

    List<String> tileOptions = List.of("tileclosed", "tile", "tilepermeable");
    List<Boolean> pacmanTileOptions = List.of(false, true, false);
    List<Boolean> ghostTileOptions = List.of(false, true, true);

    // Validate that initial conditions are true
    assertEquals(new TileCoordinates(3, 3), tile.getCoordinates());
    assertEquals("tileclosed", tile.getType());
    assertFalse(tile.isOpenToPacman());
    assertFalse(tile.isOpenToGhosts());

    for (int i = 0; i < 9; i++) {
      levelBuilder.pokeTile(tile);

      // Validate that poking increments
      assertEquals(new TileCoordinates(3, 3), tile.getCoordinates());
      assertEquals(tileOptions.get((i + 1) % tileOptions.size()), tile.getType());
      assertEquals(pacmanTileOptions.get((i + 1) % tileOptions.size()), tile.isOpenToPacman());
      assertEquals(ghostTileOptions.get((i + 1) % tileOptions.size()), tile.isOpenToGhosts());
    }
  }

}
