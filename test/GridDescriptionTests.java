import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import ooga.model.GridDescription;
import ooga.model.GridDescriptionFactory;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import org.junit.jupiter.api.Test;

/**
 * A simple test suite for the GridDescription class.
 *
 * @author Marc Chmielewski
 */
public class GridDescriptionTests {

  @Test
  public void testGridDescriptionConstructor() {
    // Simulate loading in a small grid from JSON
    int width = 2;
    int height = 2;
    List<Tile> tileList =
        List.of(
            new Tile(new TileCoordinates(0, 0), "Tile 0", false, true),
            new Tile(new TileCoordinates(1, 0), "Tile 1", true, true),
            new Tile(new TileCoordinates(0, 1), "Tile 2", false, false),
            new Tile(new TileCoordinates(1, 1), "Tile 3", false, false));

    GridDescription gridDescription = new GridDescription(width, height, tileList);

    List<List<Tile>> tiles = gridDescription.getGrid();

    for (int i = 0; i < tiles.size(); i++) {
      for (int j = 0; j < tiles.get(0).size(); j++) {
        assertEquals("Tile " + ((i * width) + j), tiles.get(i).get(j).getType());
      }
    }
  }

  @Test
  public void testGridDescriptionOverflowException() {
    // Simulate loading in a small grid from JSON
    int width = 1;
    int height = 1;
    List<Tile> tileList =
        List.of(
            new Tile(new TileCoordinates(0, 0), "Tile 0", false, false),
            new Tile(new TileCoordinates(1, 0), "Tile 1", true, false),
            new Tile(new TileCoordinates(0, 1), "Tile 2", false, true),
            new Tile(new TileCoordinates(1, 1), "Tile 3", false, true));

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          GridDescription gridDescription = new GridDescription(width, height, tileList);
        });
  }

  @Test
  public void testGridDescriptionUnderflowException() {
    // Simulate loading in a small grid from JSON
    int width = 10;
    int height = 1;
    List<Tile> tileList =
        List.of(
            new Tile(new TileCoordinates(0, 0), "Tile 0", false, true),
            new Tile(new TileCoordinates(1, 0), "Tile 1", true, false),
            new Tile(new TileCoordinates(0, 1), "Tile 2", false, false),
            new Tile(new TileCoordinates(1, 1), "Tile 3", false, true));

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          GridDescription gridDescription = new GridDescription(width, height, tileList);
        });
  }

  @Test
  public void testGridDescriptionJSON() {
    String path = "data/levels/grids/test_grid.json";
    int width = 2;
    int height = 2;
    List<Tile> tileList =
        List.of(
            new Tile(new TileCoordinates(0, 0), "Tile 0", false, true),
            new Tile(new TileCoordinates(1, 0), "Tile 1", true, true),
            new Tile(new TileCoordinates(0, 1), "Tile 2", false, false),
            new Tile(new TileCoordinates(1, 1), "Tile 3", false, false));

    GridDescription gridDescription = new GridDescription(width, height, tileList);

    try {
      gridDescription.toJSON(path);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    GridDescriptionFactory gridDescriptionFactory = new GridDescriptionFactory();
    GridDescription description = null;

    try {
      description = gridDescriptionFactory.getGridDescriptionFromJSON(path);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    assertEquals(description.getWidth(), 2);
    assertEquals(description.getHeight(), 2);
    List<List<Tile>> grid = description.getGrid();
    assertEquals(grid.get(0).get(1).getType(), "Tile 0");
    assertEquals(grid.get(1).get(1).getCoordinates(), new TileCoordinates(1, 1));
    assertTrue(grid.get(1).get(0).isOpenToPacman());
    assertTrue(grid.get(1).get(0).isOpenToGhosts());
  }
}
