package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.Blinky;
import ooga.model.sprites.Clyde;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Ghost.GhostBehavior;
import ooga.model.sprites.Home;
import ooga.model.sprites.Inky;
import ooga.model.sprites.PacMan;
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

  public static final double DT = .005;
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

  private SpriteDescription createDefaultGhostDescription(String spriteClassName) {
    String inputSource = "GHOST_AI";
    SpriteCoordinates startingCoordinates = new SpriteCoordinates(new Vec2(5, 5));
    return new SpriteDescription(spriteClassName, inputSource, startingCoordinates);
  }

  private SpriteDescription createDefaultPacmanDescription() {
    String inputSource = "HUMAN";
    SpriteCoordinates startingCoordinates = new SpriteCoordinates(new Vec2(4.9, 5));
    return new SpriteDescription("PacMan", inputSource, startingCoordinates);
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

  @Test
  public void testGhostInvariants() {
    Ghost blinky = new Blinky(blinkySpriteDescription);

    assertFalse(blinky.mustBeConsumed());
    assertTrue(blinky.hasMultiplicativeScoring());
    assertFalse(blinky.isRespawnTarget());
    assertFalse(blinky.eatsGhosts());
  }

  @Test
  public void testGhostPowerUpResponses() {
    PacmanGameState pgs = new PacmanGameState();
    Home home = new Home(new SpriteCoordinates(new Vec2(8.5, 8.5)), new Vec2(0, 0));

    PacMan pacMan = new PacMan(createDefaultPacmanDescription());
    pacMan.setInputSource(new HumanInputManager(KeybindingType.PLAYER_1));

    Ghost blinky = new Blinky(blinkySpriteDescription);
    blinky.setInputSource(new BlinkyAI(pgs.getGrid(), blinky, pacMan, home, 1.0));

    pgs.addSprite(pacMan);
    pgs.addSprite(blinky);

    // Make sure we're starting off on the right foot
    assertEquals(blinky.getGhostBehavior(), GhostBehavior.WAIT);
    double defaultMoveSpeed = blinky.getMovementSpeed();
    pgs.step(DT);

    // Spoof Pac-Man eating a Ghost Slowdown power-up
    blinky.respondToPowerEvent(PacmanPowerupEvent.GHOST_SLOWDOWN_ACTIVATED);
    pgs.step(DT);
    assertEquals(defaultMoveSpeed * .5, blinky.getMovementSpeed());

    // Make sure we return to pre-power-up at some point
    blinky.respondToPowerEvent(PacmanPowerupEvent.GHOST_SLOWDOWN_DEACTIVATED);
    pgs.step(DT);
    assertEquals(defaultMoveSpeed, blinky.getMovementSpeed());

    // Spoof Pac-Man eating a Power-Pill and check for transition to FRIGHTENED state
    blinky.respondToPowerEvent(PacmanPowerupEvent.FRIGHTEN_ACTIVATED);
    pgs.step(DT);
    assertEquals(blinky.getGhostBehavior(), GhostBehavior.FRIGHTENED);
    assertEquals(blinky.getCurrentAnimation().getCurrentCostume(), "frightened_1");
  }
}
