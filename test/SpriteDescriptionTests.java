import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import ooga.model.TileCoordinates;
import ooga.model.leveldescription.JSONDescriptionFactory;
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

  @Test
  public void testSpriteDescriptionJSON() {
    String path = "data/levels/sprites/test_sprite.json";

    String spriteClassName = "ooga.model.sprites.Ghost";
    String inputSource = "GHOST_AI";
    TileCoordinates startingCoordinates = new TileCoordinates(5, 5);
    SpriteDescription ghostDescription =
        new SpriteDescription(spriteClassName, inputSource, startingCoordinates);

    try {
      ghostDescription.toJSON(path);
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    JSONDescriptionFactory jsonDescriptionFactory = new JSONDescriptionFactory();
    SpriteDescription description = null;
    try{
      description = jsonDescriptionFactory.getSpriteDescriptionFromJSON(path);
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    assertEquals(description.getClassName(), spriteClassName);
    assertEquals(description.getInputSource(), inputSource);
    assertEquals(description.getCoordinates().getX(), 5);
    assertEquals(description.getCoordinates().getY(), 5);
  }
}
