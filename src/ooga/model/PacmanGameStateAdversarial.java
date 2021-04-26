package ooga.model;

import java.io.IOException;
import ooga.controller.HumanInputManager;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;

/**
 * The rules for Pac-Man: Adversarial Mode are given by
 *
 * <ol>
 *   <li>One player always controls Pac-Man and another player always controls the ghost
 *   <li>The ghost player wins the game if Pac-Man is consumed before he consumes all of the
 *       required dots
 *   <li>The Pac-man player wins the game if he consumes all of the dots
 *   <li>
 * </ol>
 *
 * @author George Hong
 */
public class PacmanGameStateAdversarial extends PacmanGameState {

  /**
   * This method instantiates a new adversarial-mode level from JSON. This entails loading the
   * actual file, which is largely handled by the superclass implementation of this method, setting
   * the two players to their respective HumanInputManagers, and then establishing an initial Ghost
   * for Player 2 to control.
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
      String filepath, HumanInputManager player1, HumanInputManager player2) throws IOException {
    Sprite initGhost = null;
    super.initPacmanLevelFromJSON(filepath, player1, player2);
    setPlayers(new Player(1, player1), new Player(2, player2));
    for (Sprite sprite : getSprites()) {
      if (sprite.getSwapClass() == SwapClass.GHOST) {
        initGhost = sprite;
        break;
      }
    }
    if (initGhost == null) {
      throw new IllegalArgumentException("NO GHOSTS WERE FOUND!!!!!");
    }
    initGhost.setInputSource(player2);

    setLives(1);
  }

  /**
   * checkPacmanDead checks to see if Pac-Man is, in fact dead, and in that case the this
   * PacmanGameStateAdversarial will clean up and then display the GhostsWin Sprite. Alternatively,
   * if all of the consumables have been consumed, presumably because Pac-Man has consumed them,
   * then PacmanGameStateAdversarial will clean up and then display the PacmanWin Sprite.
   */
  @Override
  protected void checkPacmanDead() {
    if (isPacmanDead()) {
      gameOverCleanup();
      showGhostWin();
    } else if (getRemainingConsumablesCount() == 0) {
      gameOverCleanup();
      showPacmanWin();
    }
  }

  /**
   * Defines the logic used to determine round end/next level. Does nothing if conditions to end
   * level are not satisfied.
   */
  @Override
  protected void checkProceedToNextLevel() {
    // Does nothing
  }
}
