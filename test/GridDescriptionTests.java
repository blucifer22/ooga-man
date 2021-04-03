import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import ooga.model.GridDescription;
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
            new Tile(new TileCoordinates(0, 0), "Tile 0", false),
            new Tile(new TileCoordinates(1, 0), "Tile 1", true),
            new Tile(new TileCoordinates(0, 1), "Tile 2", false),
            new Tile(new TileCoordinates(1, 1), "Tile 3", false));

    GridDescription gridDescription = new GridDescription(width, height, tileList);

    Tile[][] tiles = gridDescription.getGrid();

    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        assertEquals("Tile " + ((i * width) + j), tiles[i][j].getType());
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
            new Tile(new TileCoordinates(0, 0), "Tile 0", false),
            new Tile(new TileCoordinates(1, 0), "Tile 1", true),
            new Tile(new TileCoordinates(0, 1), "Tile 2", false),
            new Tile(new TileCoordinates(1, 1), "Tile 3", false));

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
            new Tile(new TileCoordinates(0, 0), "Tile 0", false),
            new Tile(new TileCoordinates(1, 0), "Tile 1", true),
            new Tile(new TileCoordinates(0, 1), "Tile 2", false),
            new Tile(new TileCoordinates(1, 1), "Tile 3", false));

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          GridDescription gridDescription = new GridDescription(width, height, tileList);
        });
  }
}
