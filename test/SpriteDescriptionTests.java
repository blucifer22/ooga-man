import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.TileCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import org.junit.jupiter.api.Test;

/**
 * A simple test suite for the SpriteDescription class.
 *
 * @author Marc Chmielewski
 */
public class SpriteDescriptionTests {

  @Test
  public void testSpriteDescriptionConstructor() {
    String spriteClassName = "ooga.model.sprites.Ghost";
    String inputSource = "GHOST_AI";
    TileCoordinates startingCoordinates = new TileCoordinates(5, 5);
    SpriteDescription ghostDescription =
        new SpriteDescription(spriteClassName, inputSource, startingCoordinates);

    assertEquals(ghostDescription.getClassName(), spriteClassName);
    assertEquals(ghostDescription.getInputSource(), inputSource);
    assertEquals(ghostDescription.getCoordinates().getX(), 5);
    assertEquals(ghostDescription.getCoordinates().getY(), 5);
  }
}
