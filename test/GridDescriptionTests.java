import java.util.List;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import org.junit.jupiter.api.Test;

public class GridDescriptionTests {

  @Test
  public void testGridDescriptionConstructor() {
    int width = 2;
    int height = 2;
    List<Tile> tileList = List.of(new Tile(new TileCoordinates(), "Empty", false));
  }

}
