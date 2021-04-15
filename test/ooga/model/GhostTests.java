package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.Blinky;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Ghost.GhostBehavior;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A simple test suite for the Ghost abstract class, through its various subclasses.
 *
 * @author Marc Chmielewski
 */
public class GhostTests {

  SpriteDescription defaultSpriteDescription;

  @BeforeEach
  public void init() {
    String spriteClassName = "Blinky";
    String inputSource = "GHOST_AI";
    SpriteCoordinates startingCoordinates = new SpriteCoordinates(new Vec2(5, 5));
    defaultSpriteDescription =
        new SpriteDescription(spriteClassName, inputSource, startingCoordinates);
  }

  @Test
  public void testBlinkySpriteDescriptionConstructor() {
    Ghost blinky = new Blinky(defaultSpriteDescription);
    // Assert that Blinky starts by WAITing in the correct location!
    assertEquals(blinky.getCoordinates().getPosition().getX(), 5);
    assertEquals(blinky.getCoordinates().getPosition().getY(), 5);
    assertEquals(blinky.getGhostBehavior(), GhostBehavior.WAIT);
  }
}
