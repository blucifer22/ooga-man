package ooga.model;

import java.io.IOException;
import ooga.controller.HumanInputManager;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;

/**
 * The rules for Pac-Man: Adversarial Mode are given by
 * <ol>
 *   <li>This is a two-player game mode with 3 rounds for each player as Pac-Man</li>
 *   <li>A round ends when the Pac-Man is consumed by a ghost or Pac-Man consumes all of the dots.  This incentivizes the player controlling the ghosts to consume Pac-Man as quickly as possible to limit the other player's total score</li>
 *   <li>At the conclusion of a round, the map transitions to the next level</li>
 *   <li>At the end of all 6 rounds, the player with the most points as Pac-Man wins. </li>
 * </ol>
 *
 * @author George Hong
 */
public class PacmanGameStateAdversarial extends PacmanGameState {

  public static final int TOTAL_ROUNDS = 6;

  @Override
  public void initPacmanLevelFromJSON(String filepath, HumanInputManager player1,
      HumanInputManager player2) throws IOException {
    Sprite initGhost = null;
    Sprite initPacman = null;
    super.initPacmanLevelFromJSON(filepath, player1, player2);
    for (Sprite sprite : getSprites()) {
      if (sprite.getSwapClass() == SwapClass.GHOST){
        initGhost = sprite;
        break;
      } else if (sprite.getSwapClass() == SwapClass.PACMAN){
        initPacman = sprite;
        break;
      }
    }
    if (initGhost == null || initPacman == null){
      throw new IllegalArgumentException("Ghost or Pac-Man sprites not found");
    }
    initGhost.setInputSource(player1);
    initPacman.setInputSource(player2);
  }

  /**
   * Defines the logic used to determine round end/next level.  Does nothing if conditions to end
   * level are not satisfied.
   */
  @Override
  protected void checkProceedToNextLevel() {
    if (isPacmanConsumed()) {
      if (getRoundNumber() == TOTAL_ROUNDS) {
        // TODO: PRESENT WIN SCREEN LOGIC FOR WINNER
      } else {
        incrementLevel();
        try {
          loadNextLevel();
        } catch (IOException e) {

        }
        swapPlayerControls();
      }
    }
  }

  private void swapPlayerControls() {
    // Note: load level likely sets the AI ahead of time
  }
}
