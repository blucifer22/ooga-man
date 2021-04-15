package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.Blinky;
import ooga.model.sprites.Clyde;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Ghost.GhostBehavior;
import ooga.model.sprites.Inky;
import ooga.model.sprites.Pinky;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A simple test suite for the Ghost abstract class, through its various subclasses.
 *
 * @author Marc Chmielewski
 */
public class GhostTests {

  SpriteDescription blinkySpriteDescription;
  SpriteDescription inkySpriteDescription;
  SpriteDescription pinkySpriteDescription;
  SpriteDescription clydeSpriteDescription;

  @BeforeEach
  public void init() {

    // Create SpriteDescriptions for each Ghost type
    blinkySpriteDescription = createDefaultGhostDescription("Blinky");
    inkySpriteDescription = createDefaultGhostDescription("Inky");
    pinkySpriteDescription = createDefaultGhostDescription("Pinky");
    clydeSpriteDescription = createDefaultGhostDescription("Clyde");
  }

  private SpriteDescription createDefaultGhostDescription(
      String spriteClassName) {
    String inputSource = "GHOST_AI";
    SpriteCoordinates startingCoordinates = new SpriteCoordinates(new Vec2(5, 5));
    return new SpriteDescription(spriteClassName, inputSource, startingCoordinates);
  }

  @Test
  public void testBlinkySpriteDescriptionConstructor() {
    Ghost blinky = new Blinky(blinkySpriteDescription);

    // Assert that Blinky starts by WAITing in the correct location!
    assertEquals(blinky.getCurrentAnimation().getCurrentCostume(), "blinky_right_1");
    assertEquals(blinky.getCoordinates().getPosition().getX(), 5);
    assertEquals(blinky.getCoordinates().getPosition().getY(), 5);
    assertEquals(blinky.getGhostBehavior(), GhostBehavior.WAIT);
  }

  @Test
  public void testInkySpriteDescriptionConstructor() {
    Ghost inky = new Inky(inkySpriteDescription);

    // Assert that Inky starts by WAITing in the correct location!
    assertEquals(inky.getCurrentAnimation().getCurrentCostume(), "inky_right_1");
    assertEquals(inky.getCoordinates().getPosition().getX(), 5);
    assertEquals(inky.getCoordinates().getPosition().getY(), 5);
    assertEquals(inky.getGhostBehavior(), GhostBehavior.WAIT);
  }

  @Test
  public void testPinkySpriteDescriptionConstructor() {
    Ghost pinky = new Pinky(pinkySpriteDescription);

    // Assert that Pinky starts by WAITing in the correct location!
    assertEquals(pinky.getCurrentAnimation().getCurrentCostume(), "pinky_right_1");
    assertEquals(pinky.getCoordinates().getPosition().getX(), 5);
    assertEquals(pinky.getCoordinates().getPosition().getY(), 5);
    assertEquals(pinky.getGhostBehavior(), GhostBehavior.WAIT);
  }

  @Test
  public void testClydeSpriteDescriptionConstructor() {
    Ghost clyde = new Clyde(clydeSpriteDescription);

    // Assert that Pinky starts by WAITing in the correct location!
    assertEquals(clyde.getCurrentAnimation().getCurrentCostume(), "clyde_right_1");
    assertEquals(clyde.getCoordinates().getPosition().getX(), 5);
    assertEquals(clyde.getCoordinates().getPosition().getY(), 5);
    assertEquals(clyde.getGhostBehavior(), GhostBehavior.WAIT);
  }
}
