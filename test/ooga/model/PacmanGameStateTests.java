package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import ooga.model.sprites.SwapClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PacmanGameStateTests {

  PacmanGameState pgs;

  @BeforeEach
  public void init() {
    pgs = new PacmanGameState();
  }

  @Test
  public void testPGSInitialization() throws IOException {
    HumanInputManager player1 = new HumanInputManager(KeybindingType.PLAYER_1);
    HumanInputManager player2 = new HumanInputManager(KeybindingType.PLAYER_2);

    pgs.initPacmanLevelFromJSON("data/levels/test_level_1.json", player1, player2);
    assertEquals(pgs.getSprites().get(0).getSwapClass(), SwapClass.GHOST);
    assertEquals(pgs.getPacmanLivesRemaining(), 3);
    assertEquals(pgs.getRoundNumber(), 1);
  }
}
