package ooga.model.leveldescription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import ooga.model.leveldescription.LevelBuilder.BuilderState;
import ooga.model.sprites.SwapClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A simple suite of tests for the LevelBuilder
 *
 * @author George Hong
 * @author Marc Chmielewski
 */
public class LevelBuilderTests {

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
      "Blinky", "Inky", "Clyde", "Pinky", "PacMan", "Cherry", "Dot", "PowerPill"
    };
    for (String requiredName : allNames) {
      assertTrue(names.contains(requiredName));
    }
  }

  @Test
  public void testPacmanLimit() {
    levelBuilder.advanceState();
    levelBuilder.advanceState();
    levelBuilder.setGridSize(5, 5);
    levelBuilder.getPalette().setActiveSprite("PacMan");
    levelBuilder.addSprite(3, 3);
    assertThrows(IllegalStateException.class, () -> levelBuilder.addSprite(3, 3));
    levelBuilder.clearSpritesOnTile(3, 3);
    levelBuilder.addSprite(3, 3);
  }

  @Test
  public void testPalette() {
    levelBuilder.advanceState();
    levelBuilder.advanceState();
    levelBuilder.setGridSize(5, 5);
    levelBuilder.getPalette().setActiveSprite("Blinky");
    levelBuilder.addSprite(3, 3);
    assertEquals(1, levelBuilder.getLevel().getSprites().size());
    for (int i = 1; i < 10; i++) {
      levelBuilder.addSprite(3, 3);
      assertEquals(i + 1, levelBuilder.getLevel().getSprites().size());
    }
    levelBuilder.clearSpritesOnTile(3, 3);
    assertEquals(0, levelBuilder.getLevel().getSprites().size());
  }

  @Test
  public void testPokeTile() {
    levelBuilder.setGridSize(5, 5);
    Tile tile = new Tile(new TileCoordinates(3, 3), "tileclosed", false, false);
    levelBuilder
        .getLevel()
        .getGrid()
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
      levelBuilder.pokeTile(3, 3);

      // Validate that poking increments
      assertEquals(new TileCoordinates(3, 3), tile.getCoordinates());
      assertEquals(tileOptions.get((i + 1) % tileOptions.size()), tile.getType());
      assertEquals(pacmanTileOptions.get((i + 1) % tileOptions.size()), tile.isOpenToPacman());
      assertEquals(ghostTileOptions.get((i + 1) % tileOptions.size()), tile.isOpenToGhosts());
    }
  }

  @Test
  public void saveLevelToFile() {
    // Emulate the naming and DIMENSIONING phase of the level builder
    File testFile = new File("data/levels/test_level_builder.json");
    int dimension = 50;
    levelBuilder.setGridSize(dimension, dimension);
    assertEquals(levelBuilder.getBuilderState(), BuilderState.DIMENSIONING);
    assertEquals(levelBuilder.getLevel().getGrid().getHeight(), 50);
    assertEquals(levelBuilder.getLevel().getGrid().getWidth(), 50);

    // Check that an edge Tile starts out blocked
    assertEquals(
        levelBuilder.getLevel().getGrid().getTile(new TileCoordinates(0, 0)).getType(),
        "tileclosed");

    // Check that a central Tile starts out empty
    assertEquals(
        levelBuilder.getLevel().getGrid().getTile(new TileCoordinates(10, 10)).getType(), "tile");

    // Advance the state to TILING and drop a couple of tiles on the grid
    levelBuilder.advanceState();
    assertEquals(levelBuilder.getBuilderState(), BuilderState.TILING);

    // Test a centrally located Tile
    levelBuilder.select(10, 10);
    assertEquals(
        levelBuilder.getLevel().getGrid().getTile(new TileCoordinates(10, 10)).getType(),
        "tilepermeable");
    assertEquals(levelBuilder.getBuilderState(), BuilderState.TILING);

    // Test another centrally located Tile
    levelBuilder.select(30, 40);
    assertEquals(
        levelBuilder.getLevel().getGrid().getTile(new TileCoordinates(30, 40)).getType(),
        "tilepermeable");

    // Test an edge Tile
    levelBuilder.select(0, 0);
    assertEquals(
        levelBuilder.getLevel().getGrid().getTile(new TileCoordinates(0, 0)).getType(), "tile");

    // Advance the state to SPRITE_PLACEMENT and place some Sprites using the Palette
    levelBuilder.advanceState();
    assertEquals(levelBuilder.getBuilderState(), BuilderState.SPRITE_PLACEMENT);

    // Add a PacMan to an empty Tile
    levelBuilder.getPalette().setActiveSprite("PacMan");
    levelBuilder.select(1, 1);
    assertEquals(levelBuilder.getLevel().getSprites().get(0).getSwapClass(), SwapClass.PACMAN);

    // Add a Blinky to another empty Tile
    levelBuilder.getPalette().setActiveSprite("Blinky");
    levelBuilder.select(10, 30);
    assertEquals(levelBuilder.getLevel().getSprites().get(1).getSwapClass(), SwapClass.GHOST);

    // Add a bunch of Cherries to one Row
    levelBuilder.getPalette().setActiveSprite("Cherry");
    for(int i = 1; i < 16; i++) {
      levelBuilder.select(i, 21);
      assertEquals(levelBuilder.getLevel().getSprites().get(i + 1).getSwapClass(), SwapClass.NONE);
    }

    // Add a bunch of PowerPills to one Column
    levelBuilder.getPalette().setActiveSprite("Cherry");
    for(int i = 1; i < 16; i++) {
      levelBuilder.select(46, i);
      assertEquals(levelBuilder.getLevel().getSprites().get(i + 16).getSwapClass(), SwapClass.NONE);
    }

    // Add a bunch of Dots to the same Tile
    levelBuilder.getPalette().setActiveSprite("Dot");
    for(int i = 0; i < 15; i++) {
      levelBuilder.select(20, 25);
      assertEquals(levelBuilder.getLevel().getSprites().get(i + 32).getSwapClass(), SwapClass.NONE);
    }

    // Verify that a Dot was on the Tile before erasure
    assertEquals(levelBuilder.getLevel().getSprites().get(35).getSwapClass(), SwapClass.NONE);

    // Remove all the Dots from aforementioned Tile and verify that they're gone
    levelBuilder.clearSpritesOnTile(20,25);
    assertThrows(IndexOutOfBoundsException.class, () -> levelBuilder.getLevel().getSprites().get(35));

  }
}
