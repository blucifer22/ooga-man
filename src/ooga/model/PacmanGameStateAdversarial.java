package ooga.model;

import java.io.IOException;
import ooga.controller.HumanInputManager;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;

/**
 * The rules for Pac-Man: Adversarial Mode are given by
 * <ol>
 *   <li> One player always controls Pac-Man and another player always controls the ghost</li>
 *   <li> The ghost player wins the game if Pac-Man is consumed before he consumes all of the required dots</li>
 *   <li> The Pac-man player wins the game if he consumes all of the dots<li>
 * </ol>
 *
 * @author George Hong
 */
public class PacmanGameStateAdversarial extends PacmanGameState {

  @Override
  public void initPacmanLevelFromJSON(String filepath, HumanInputManager player1,
      HumanInputManager player2) throws IOException {
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
    initGhost.setInputSource(player2);
  }


  @Override
  protected void checkPacmanDead() {
    // Does nothing
    if (isPacmanDead()) {
      System.out.println("GHOSTS WIN");
    } else if (getRemainingConsumablesCount() == 0) {
      System.out.println("PAC-MAN WINS");
    }
  }

  /**
   * Defines the logic used to determine round end/next level.  Does nothing if conditions to end
   * level are not satisfied.
   */
  @Override
  protected void checkProceedToNextLevel() {
    // DOes nothing
  }
}
