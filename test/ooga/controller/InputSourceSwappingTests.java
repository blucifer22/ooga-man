package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.BlinkyAI;
import ooga.model.InputSource;
import ooga.model.PacmanGameState;
import ooga.model.PinkyAI;
import ooga.model.SpriteCoordinates;
import ooga.model.sprites.Blinky;
import ooga.model.sprites.Ghost;
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
    PacMan pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(0, 0), 5.0);
    Ghost blinky = new Blinky(new SpriteCoordinates(new Vec2(13.5, 13.5)), new Vec2(0, 0), 3.9);
    Ghost pinky = new Pinky(new SpriteCoordinates(new Vec2(1.5, 13.5)), new Vec2(0, 0), 4);

    InputSource pacmanInput = new HumanInputManager();
    InputSource blinkyInput = new BlinkyAI(pgs.getGrid(), blinky, pacman, 0.9);
    InputSource pinkyInput = new PinkyAI(pgs.getGrid(), pinky, pacman, 0.9);
    pacman.setInputSource(pacmanInput);
    blinky.setInputSource(blinkyInput);
    pinky.setInputSource(pinkyInput);

    assertEquals(pacman.getInputSource(), pacmanInput);
    assertEquals(blinky.getInputSource(), blinkyInput);
    assertEquals(pinky.getInputSource(), pinkyInput);

    assertEquals(pacman.getDefaultInputSource(), pacmanInput);
    assertEquals(blinky.getDefaultInputSource(), blinkyInput);
    assertEquals(pinky.getDefaultInputSource(), pinkyInput);


    InputSource ghostHIM = new HumanInputManager();
  }
}
