package ooga.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

  private PacmanGameState pgs;
  private Player player1;
  private Player player2;
  private InputSource player1In;
  private InputSource player2In;

  @BeforeEach
  public void setup() {
    player1In = new HumanInputManager(KeybindingType.PLAYER_1);
    player1 = new Player(1, player1In);
    player2In = new HumanInputManager(KeybindingType.PLAYER_2);
    player2 = new Player(2, player2In);
    pgs = new PacmanGameState();
  }

  @Test
  public void testScoreTracking() {
    assertEquals(1, player1.getID());
    assertEquals(2, player2.getID());

    player1.setScore(500);
    player2.setScore(300);

    assertEquals(500, player1.getScore());
    assertEquals(300, player2.getScore());

    player1.setRoundWins(4);
    player2.setRoundWins(6);

    assertEquals(4, player1.getRoundWins());
    assertEquals(6, player2.getRoundWins());
  }

  @Test
  public void testVerifyKeybindings() {
    assertEquals(player1In, player1.getInputSource());
    assertEquals(player2In, player2.getInputSource());
  }

  @Test
  public void testPacmanPlayerList(){
    pgs.setPlayers(player1, player2);
    List<ImmutablePlayer> players = pgs.getPlayers();
    assertEquals(2, players.size());
    assertEquals(player1, players.get(0));

    pgs.setPlayers(player2, player1);
    players = pgs.getPlayers();
    assertEquals(2, players.size());
    assertEquals(player2, players.get(0));
  }
}

