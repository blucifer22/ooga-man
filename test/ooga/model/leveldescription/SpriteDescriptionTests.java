package ooga.model.leveldescription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;
import org.junit.jupiter.api.Test;

/**
 * A simple test suite for the SpriteDescription class.
 *
 * @author Marc Chmielewski
 */
public class SpriteDescriptionTests {

  @Test
  public void testSpriteDescriptionConstructor() {
    String spriteClassName = "Ghost";
    String inputSource = "GHOST_AI";
    SpriteCoordinates startingCoordinates = new SpriteCoordinates(new Vec2(5, 5));
    SpriteDescription ghostDescription =
        new SpriteDescription(spriteClassName, inputSource, startingCoordinates);

    assertEquals(ghostDescription.getClassName(), spriteClassName);
    assertEquals(ghostDescription.getInputSource(), inputSource);
    assertEquals(ghostDescription.getCoordinates().getPosition().getX(), 5);
    assertEquals(ghostDescription.getCoordinates().getPosition().getY(), 5);
  }

  @Test
  public void testSpriteDescriptionJSON() {
    String path = "data/levels/sprites/test_sprite.json";

    String spriteClassName = "PacMan";
    String inputSource = "HUMAN";
    SpriteCoordinates startingCoordinates = new SpriteCoordinates(new Vec2(5, 5));
    SpriteDescription pacmanDescription =
        new SpriteDescription(spriteClassName, inputSource, startingCoordinates);

    try {
      pacmanDescription.toJSON(path);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    JSONDescriptionFactory jsonDescriptionFactory = new JSONDescriptionFactory();
    SpriteDescription description = null;
    try {
      description = jsonDescriptionFactory.getSpriteDescriptionFromJSON(path);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      fail();
    }

    assertEquals(description.getClassName(), spriteClassName);
    assertEquals(description.getInputSource(), inputSource);
    assertEquals(description.getCoordinates().getPosition().getX(), 5);
    assertEquals(description.getCoordinates().getPosition().getY(), 5);

    Sprite pacmanSprite = pacmanDescription.toSprite();
    assertEquals(pacmanSprite.getCoordinates().getTileCoordinates().getX(), 5);
    assertEquals(pacmanSprite.getCoordinates().getTileCoordinates().getY(), 5);
    assertEquals(pacmanSprite.getInputString(), "HUMAN");
  }
}
