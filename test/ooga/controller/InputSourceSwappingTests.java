package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.scene.input.KeyCode;
import ooga.model.BlinkyAI;
import ooga.model.GhostAI;
import ooga.model.InputSource;
import ooga.model.PacmanGameState;
import ooga.model.PinkyAI;
import ooga.model.SpriteCoordinates;
import ooga.model.sprites.Blinky;
import ooga.model.sprites.Clyde;
import ooga.model.sprites.Dot;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Inky;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Pinky;
import ooga.util.Vec2;
import org.junit.jupiter.api.Test;

/**
 * This is a simple test suite that verifies that the "swapping" behavior of the HumanInputManager
 * is maintained.
 *
 * @author Marc Chmielewski
 */
public class InputSourceSwappingTests {

  @Test
  public void testSwap() {
    PacmanGameState pgs = new PacmanGameState();

    // Add some non-movable sprites to make sure they get ignored
    Dot dot1 = new Dot(new SpriteCoordinates(new Vec2(4.5, 4.5)), new Vec2(0, 0));
    Dot dot2 = new Dot(new SpriteCoordinates(new Vec2(1.5, 3.5)), new Vec2(0, 0));

    // Add the actual Sprites
    PacMan pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(0, 0), 5.0);
    Ghost blinky = new Blinky(new SpriteCoordinates(new Vec2(13.5, 13.5)), new Vec2(0, 0), 3.9);
    Ghost pinky = new Pinky(new SpriteCoordinates(new Vec2(1.5, 13.5)), new Vec2(0, 0), 4);
    Ghost inky = new Inky(new SpriteCoordinates(new Vec2(11.5, 11.5)), new Vec2(0, 0), 5.0);
    Ghost clyde = new Clyde(new SpriteCoordinates(new Vec2(11.5, 11.5)), new Vec2(0, 0), 5.0);

    InputSource pacmanInput = new HumanInputManager();
    InputSource blinkyInput = new BlinkyAI(pgs.getGrid(), blinky, pacman, 0.9);
    InputSource pinkyInput = new PinkyAI(pgs.getGrid(), pinky, pacman, 0.9);
    InputSource inkyInput = new GhostAI(pgs.getGrid(), inky, pacman, 0.5);
    InputSource clydeInput = new GhostAI(pgs.getGrid(), inky, pacman, 0.3);

    pacman.setInputSource(pacmanInput);
    blinky.setInputSource(blinkyInput);
    pinky.setInputSource(pinkyInput);
    inky.setInputSource(inkyInput);
    clyde.setInputSource(clydeInput);

    pgs.addSprite(dot1);
    pgs.addSprite(dot2);

    pgs.addSprite(pacman);
    pgs.addSprite(blinky);
    pgs.addSprite(pinky);
    pgs.addSprite(inky);
    pgs.addSprite(clyde);

    // Make sure that our input sources have been correctly assigned
    assertEquals(pacman.getInputSource(), pacmanInput);
    assertEquals(blinky.getInputSource(), blinkyInput);
    assertEquals(pinky.getInputSource(), pinkyInput);

    // Make sure that the *default* input sources have been correctly assigned
    assertEquals(pacman.getDefaultInputSource(), pacmanInput);
    assertEquals(blinky.getDefaultInputSource(), blinkyInput);
    assertEquals(pinky.getDefaultInputSource(), pinkyInput);

    // Assign Blinky the "ghostHIM" and make sure that the his default persists!
    HumanInputManager ghostHIM = new HumanInputManager();
    blinky.setInputSource(ghostHIM);
    assertEquals(blinky.getInputSource(), ghostHIM);
    assertEquals(blinky.getDefaultInputSource(), blinkyInput);

    // Spoof a press of the "action button" to force a swap!
    ghostHIM.onKeyPress(KeyCode.SPACE);
    pgs.handleSwaps();
    assertEquals(blinky.getInputSource(), blinkyInput);
    assertEquals(pinky.getInputSource(), ghostHIM);
    ghostHIM.onKeyRelease(KeyCode.SPACE);

    // Make sure that we "cycle" through the ghosts on subsequent presses
    ghostHIM.onKeyPress(KeyCode.SPACE);
    pgs.handleSwaps();
    assertEquals(pinky.getInputSource(), pinkyInput);
    assertEquals(inky.getInputSource(), ghostHIM);
    ghostHIM.onKeyRelease(KeyCode.SPACE);

    ghostHIM.onKeyPress(KeyCode.SPACE);
    pgs.handleSwaps();
    assertEquals(inky.getInputSource(), inkyInput);
    assertEquals(clyde.getInputSource(), ghostHIM);
    ghostHIM.onKeyRelease(KeyCode.SPACE);

    // Wraparound edge case!
    ghostHIM.onKeyPress(KeyCode.SPACE);
    pgs.handleSwaps();
    assertEquals(clyde.getInputSource(), clydeInput);
    assertEquals(blinky.getInputSource(), ghostHIM);
    ghostHIM.onKeyRelease(KeyCode.SPACE);
  }
}
