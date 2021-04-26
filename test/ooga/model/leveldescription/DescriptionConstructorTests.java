package ooga.model.leveldescription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import ooga.model.grid.PacmanGrid;
import ooga.model.PacmanLevel;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;
import org.junit.jupiter.api.Test;

public class DescriptionConstructorTests {

  @Test
  public void testSpriteFromSpriteDescription() {
    PacMan pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(0, 0), 5.0);
    SpriteDescription description = new SpriteDescription(pacman);
    Sprite reconstructedSprite = description.toSprite();

    assertEquals(reconstructedSprite.getClass().getSimpleName(), "PacMan");
    assertEquals(reconstructedSprite.getCoordinates().getPosition().getX(), 1.5);
  }

  @Test
  public void testGridFromGridDescription() {
    PacmanGrid grid = null;
    try {
      grid =
          new JSONDescriptionFactory()
              .getGridDescriptionFromJSON("data/levels/grids/demo_grid.json")
              .toGrid();
    } catch (IOException e) {
      fail();
    }
    GridDescription gridDescription = new GridDescription(grid);

    assertEquals(gridDescription.getHeight(), grid.getHeight());
    assertEquals(gridDescription.getWidth(), grid.getWidth());
  }

  @Test
  public void testLevelToLevelDescription() {
    JSONDescriptionFactory factory = new JSONDescriptionFactory();
    LevelDescription levelDescription = null;
    try {
      levelDescription = factory.getLevelDescriptionFromJSON("data/levels/test_level_1.json");
    } catch (IOException e) {
      fail();
    }

    PacmanLevel level = new PacmanLevel(levelDescription);
    assertEquals(
        level.getSprites().size(),
        levelDescription.getSpriteLayoutDescription().getSprites().size());
  }
}
