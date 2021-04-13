package ooga.model.leveldescription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import ooga.model.PacmanGameState;
import ooga.model.TileCoordinates;
import ooga.model.leveldescription.GridDescription;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.leveldescription.SpriteLayoutDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A simple test suite for the LevelDescription class
 *
 * @author Marc Chmielewski
 */
public class LevelDescriptionTests {

  JSONDescriptionFactory jsonDescriptionFactory;

  @BeforeEach
  public void setup() {
    jsonDescriptionFactory = new JSONDescriptionFactory();
  }

  @Test
  public void testLevelDescriptionConstructor() {
    String gridDescriptionPath = "data/levels/grids/test_grid.json";
    String spriteLayoutDescriptionPath = "data/levels/sprite-layouts/test_sprite_layout.json";
    String levelName = "Test Level";
    GridDescription gridDescription = null;
    SpriteLayoutDescription spriteLayoutDescription = null;

    try {
      gridDescription = jsonDescriptionFactory.getGridDescriptionFromJSON(gridDescriptionPath);
      spriteLayoutDescription =
          jsonDescriptionFactory.getSpriteLayoutDescriptionFromJSON(spriteLayoutDescriptionPath);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    LevelDescription levelDescription =
        new LevelDescription(levelName, gridDescription, spriteLayoutDescription);

    assertEquals(levelDescription.getLevelName(), levelName);
    assertEquals(levelDescription.getGridDescription().getGridName(), "testGrid");
    assertEquals(spriteLayoutDescription.getSprites().get(0).getClassName(), "Ghost");
  }

  @Test
  public void testLevelDescriptionJSON() {
    String gridDescriptionPath = "data/levels/grids/test_grid.json";
    String spriteLayoutDescriptionPath = "data/levels/sprite-layouts/test_sprite_layout.json";
    String levelName = "Test Level";
    GridDescription gridDescription = null;
    SpriteLayoutDescription spriteLayoutDescription = null;

    try {
      gridDescription = jsonDescriptionFactory.getGridDescriptionFromJSON(gridDescriptionPath);
      spriteLayoutDescription =
          jsonDescriptionFactory.getSpriteLayoutDescriptionFromJSON(spriteLayoutDescriptionPath);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    LevelDescription levelDescription =
        new LevelDescription(levelName, gridDescription, spriteLayoutDescription);

    assertEquals(levelDescription.getLevelName(), levelName);
    assertEquals(levelDescription.getGridDescription().getGridName(), "testGrid");
    assertEquals(spriteLayoutDescription.getSprites().get(0).getClassName(), "Ghost");

    String levelDescriptionPath = "data/levels/test_level.json";
    LevelDescription levelDescriptionFromJSON = null;
    try {
      levelDescription.toJSON(levelDescriptionPath);
      levelDescriptionFromJSON =
          jsonDescriptionFactory.getLevelDescriptionFromJSON(levelDescriptionPath);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    assertEquals(levelDescriptionFromJSON.getLevelName(), levelName);
    assertEquals(
        levelDescriptionFromJSON.getGridDescription().getHeight(), gridDescription.getHeight());
    assertEquals(
        levelDescriptionFromJSON.getSpriteLayoutDescription().getSprites().size(),
        spriteLayoutDescription.getSprites().size());
  }

  @Test
  public void testLevelDescriptionFromJSONToPacmanGameState() {
    String levelDescriptionPath = "data/levels/test_level.json";
    LevelDescription levelDescription = null;

    String gridDescriptionPath = "data/levels/grids/test_grid.json";
    String spriteLayoutDescriptionPath = "data/levels/sprite-layouts/test_sprite_layout.json";
    String levelName = "Test Level";
    GridDescription gridDescription = null;
    SpriteLayoutDescription spriteLayoutDescription = null;

    try {
      gridDescription = jsonDescriptionFactory.getGridDescriptionFromJSON(gridDescriptionPath);
      spriteLayoutDescription =
          jsonDescriptionFactory.getSpriteLayoutDescriptionFromJSON(spriteLayoutDescriptionPath);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    try {
      levelDescription = jsonDescriptionFactory.getLevelDescriptionFromJSON(levelDescriptionPath);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    assertEquals(levelDescription.getLevelName(), levelName);
    assertEquals(levelDescription.getGridDescription().getHeight(), gridDescription.getHeight());
    assertEquals(
        levelDescription.getSpriteLayoutDescription().getSprites().size(),
        spriteLayoutDescription.getSprites().size());

    PacmanGameState pgs = levelDescription.toPacmanGameState();

    assertEquals(pgs.getSprites().size(), 2);
    assertEquals(pgs.getGrid().getHeight(), 6);
    assertFalse(pgs.getGrid().getTile(new TileCoordinates(0, 0)).isOpenToPacman());
  }
}
