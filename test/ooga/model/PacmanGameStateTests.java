package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import ooga.model.sprites.SwapClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A simple test suite that makes sure that the various PacmanGameStates are able to be successfully
 * instantiated.
 *
 * @author Marc Chmielewski
 */
public class PacmanGameStateTests {

  PacmanGameState pgs;
  PacmanGameStateChase pgsc;
  PacmanGameStateAdversarial pgsa;

  @BeforeEach
  public void init() {
    pgs = new PacmanGameState();
    pgsc = new PacmanGameStateChase();
    pgsa = new PacmanGameStateAdversarial();
  }

  @Test
  public void testPGSInitialization() throws IOException {
    HumanInputManager player1 = new HumanInputManager(KeybindingType.PLAYER_1);
    HumanInputManager player2 = new HumanInputManager(KeybindingType.PLAYER_2);

    pgs.initPacmanLevelFromJSON("data/levels/test_level_1.json", player1, player2);
    assertEquals(pgs.getSprites().get(0).getSwapClass(), SwapClass.GHOST);
    assertEquals(pgs.getPacmanLivesRemaining(), 3);
    assertEquals(pgs.getRoundNumber(), 1);
    pgs.step(1.0 / 60.0);
    assertEquals(pgs.getSprites().get(0).getSwapClass(), SwapClass.GHOST);
    assertEquals(pgs.getPacmanLivesRemaining(), 3);
    assertEquals(pgs.getRoundNumber(), 1);
  }

  @Test
  public void testPGSCInitialization() throws IOException {
    HumanInputManager player1 = new HumanInputManager(KeybindingType.PLAYER_1);
    HumanInputManager player2 = new HumanInputManager(KeybindingType.PLAYER_2);

    pgsc.initPacmanLevelFromJSON("data/levels/test_level_1.json", player1, player2);
    assertEquals(pgsc.getSprites().get(0).getSwapClass(), SwapClass.GHOST);
    assertEquals(pgsc.getPacmanLivesRemaining(), 3);
    assertEquals(pgsc.getRoundNumber(), 1);
    pgsc.step(1.0 / 60.0);
    assertEquals(pgsc.getSprites().get(0).getSwapClass(), SwapClass.GHOST);
    assertEquals(pgsc.getPacmanLivesRemaining(), 3);
    assertEquals(pgsc.getRoundNumber(), 1);
  }

  @Test
  public void testPGSAInitialization() throws IOException {
    HumanInputManager player1 = new HumanInputManager(KeybindingType.PLAYER_1);
    HumanInputManager player2 = new HumanInputManager(KeybindingType.PLAYER_2);

    pgsa.initPacmanLevelFromJSON("data/levels/test_level_1.json", player1, player2);
    assertEquals(pgsa.getSprites().get(0).getSwapClass(), SwapClass.GHOST);
    assertEquals(pgsa.getPacmanLivesRemaining(), 3);
    assertEquals(pgsa.getRoundNumber(), 1);
    pgsa.step(1.0 / 60.0);
    assertEquals(pgsa.getSprites().get(0).getSwapClass(), SwapClass.GHOST);
    assertEquals(pgsa.getPacmanLivesRemaining(), 3);
    assertEquals(pgsa.getRoundNumber(), 1);
  }
}
