import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import ooga.model.leveldescription.GridDescription;
import ooga.model.leveldescription.JSONGridDescriptionFactory;
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
    String name = "testGrid";
    int width = 2;
    int height = 2;
    List<List<Tile>> tileList =
        List.of(
            List.of(
                new Tile(new TileCoordinates(0, 0), "Tile 0", false, true),
                new Tile(new TileCoordinates(1, 0), "Tile 1", true, false)),
            List.of(
                new Tile(new TileCoordinates(0, 1), "Tile 2", false, false),
                new Tile(new TileCoordinates(1, 1), "Tile 3", false, true)));

    GridDescription gridDescription = new GridDescription(name, width, height, tileList);

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
    String name = "overflowingTestGrid";
    int width = 1;
    int height = 1;
    List<List<Tile>> tileList =
        List.of(
            List.of(
                new Tile(new TileCoordinates(0, 0), "Tile 0", false, true),
                new Tile(new TileCoordinates(1, 0), "Tile 1", true, false)),
            List.of(
                new Tile(new TileCoordinates(0, 1), "Tile 2", false, false),
                new Tile(new TileCoordinates(1, 1), "Tile 3", false, true)));

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          GridDescription gridDescription = new GridDescription(name, width, height, tileList);
        });
  }

  @Test
  public void testGridDescriptionUnderflowException() {
    // Simulate loading in a small grid from JSON
    String name = "underflowingTestGrid";
    int width = 10;
    int height = 1;
    List<List<Tile>> tileList =
        List.of(
            List.of(
                new Tile(new TileCoordinates(0, 0), "Tile 0", false, true),
                new Tile(new TileCoordinates(1, 0), "Tile 1", true, false)),
            List.of(
                new Tile(new TileCoordinates(0, 1), "Tile 2", false, false),
                new Tile(new TileCoordinates(1, 1), "Tile 3", false, true)));

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          GridDescription gridDescription = new GridDescription(name, width, height, tileList);
        });
  }

  @Test
  public void testGridDescriptionJSON() {
    String path = "data/levels/grids/test_grid.json";
    String name = "testGrid";
    int width = 2;
    int height = 2;
    List<List<Tile>> tileList =
        List.of(
            List.of(
                new Tile(new TileCoordinates(0, 0), "Tile 0", false, true),
                new Tile(new TileCoordinates(0, 1), "Tile 1", true, false)),
            List.of(
                new Tile(new TileCoordinates(1, 0), "Tile 2", false, false),
                new Tile(new TileCoordinates(1, 1), "Tile 3", false, true)));

    GridDescription gridDescription = new GridDescription(name, width, height, tileList);

    try {
      gridDescription.toJSON(path);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    JSONGridDescriptionFactory JSONGridDescriptionFactory = new JSONGridDescriptionFactory();
    GridDescription description = null;

    try {
      description = JSONGridDescriptionFactory.getGridDescriptionFromJSON(path);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    assertEquals(description.getWidth(), 2);
    assertEquals(description.getHeight(), 2);
    List<List<Tile>> grid = description.getGrid();
    assertEquals(grid.get(0).get(1).getType(), "Tile 1");
    assertEquals(grid.get(1).get(1).getCoordinates(), new TileCoordinates(1, 1));
    assertTrue(grid.get(0).get(1).isOpenToPacman());
    assertFalse(grid.get(0).get(1).isOpenToGhosts());
  }
}
