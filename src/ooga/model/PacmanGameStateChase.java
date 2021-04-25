package ooga.model;

import java.io.IOException;
import ooga.controller.HumanInputManager;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;
import ooga.model.sprites.status.GhostWin;
import ooga.model.sprites.status.PacmanWin;
import ooga.util.Vec2;

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

  public static final int TIME_LIMIT = 45;

  /**
   * Initializes Pac-Man game state from a JSON file. Performs all of the AI/human input linkages
   * and sets up teleporters and other map elements.
   *
   * @param filepath
   * @param player1
   * @param player2
   * @throws IOException
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
  }

  @Override
  protected void checkProceedToNextLevel() {
    if (getClock().getTime() >= TIME_LIMIT) {
      System.out.println("PACMAN RAN AWAY!!!");
      // TODO: PRESENT LOSE SCREEN
      showPacmanWin();
    } else if (isPacmanDead()) {
      // TODO: PRESENT WIN SCREEN
      System.out.println("PACMAN WAS EATEN!!!!");
      showGhostWin();
    }
  }
}
