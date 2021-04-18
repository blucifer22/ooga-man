package ooga.model;

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

  /**
   * Defines the logic used to determine round end/next level.
   */
  @Override
  protected void endLevel() {
    if (isPacmanConsumed()) {
      if (getLevel() == TOTAL_ROUNDS) {
        // TODO: PRINT WIN SCREEN
      } else {
        incrementLevel();
        swapPlayerControls();
        loadNextLevel();
      }
    }
  }

  private void swapPlayerControls() {

  }
}
