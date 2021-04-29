package ooga.model;

import java.io.IOException;
import ooga.controller.HumanInputManager;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;

/**
 * The rules for Pac-Man: Chase Mode are given by
 *
 * <ol>
 *   <li>This is a single-player game mode with a single round. Players control ghosts.
 *   <li>The player is on a time-limit to consume Pac-Man.
 *   <li>The game ends when time runs out or the player successfully manages to eat Pac-Man
 * </ol>
 *
 * @author George Hong
 * @author Marc Chmielewski
 */
public class PacmanGameStateChase extends PacmanGameState {

  /**
   * Length of a round.
   */
  public static final int TIME_LIMIT = 45;

  /**
   * This method instantiates a new chase-mode level from JSON. This entails loading the actual
   * file, which is largely handled by the superclass implementation of this method, setting the
   * player up with their input manager, and handling the initial swapping configuration for the
   * Ghosts.
   *
   * <p>In the event that the provided JSON contains no Ghosts, then an IllegalArgumentException
   * will be thrown.
   *
   * @param filepath The filepath of the JSON that contains the serialized PacmanLevel to load in.
   * @param player1 A HumanInputManager for Player 1
   * @param player2 A HumanInputManager for Player 2
   * @throws IOException In the event that the filepath points to an invalid location
   */
  @Override
  public void initPacmanLevelFromJSON(
      String filepath, HumanInputManager player1, HumanInputManager player2)
      throws IOException, IllegalArgumentException {
    Sprite initGhost = null;
    super.initPacmanLevelFromJSON(filepath, player1, player2);
    for (Sprite sprite : getSprites()) {
      if (sprite.getSwapClass() == SwapClass.GHOST) {
        initGhost = sprite;
        break;
      }
    }
    if (initGhost == null) {
      throw new IllegalArgumentException("NO GHOSTS WERE FOUND!!!!!");
    }
    initGhost.setInputSource(player1);
    setLives(1);
  }

  /**
   * checkProceedToNextLevel checks to see if either of the two win conditions for a chase mode game
   * have been reached and then responds accordingly. In the event that neither is reached, then
   * gameplay continues unfettered.
   *
   * <p>If the game clock has exceeded the TIME_LIMIT, then this method causes this
   * PacmanGameStateChase to clean itself up and show the PacmanWin Sprite.
   *
   * <p>If Pac-Man is killed by the Ghosts, then this method causes this PacmanGameStateChase to
   * clean itself up and show the GhostsWin Sprite.
   */
  @Override
  protected void checkProceedToNextLevel() {
    if (getClock().getTime() >= TIME_LIMIT) {
      gameOverCleanup();
      showPacmanWin();
    } else if (isPacmanDead()) {
      gameOverCleanup();
      showGhostWin();
    }
  }
}
