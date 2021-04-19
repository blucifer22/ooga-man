package ooga.model;

/**
 * The rules for Pac-Man: Chase Mode are given by
 * <ol>
 *   <li>This is a single-player game mode with a single round.  Players control ghosts.</li>
 *   <li>The player is on a time-limit to consume Pac-Man.</li>
 *   <li>The game ends when time runs out or the player successfully manages to eat Pac-Man </li>
 * </ol>
 *
 * @author George Hong
 */
public class PacmanGameStateChase extends PacmanGameState {

  public static final int TIME_LIMIT = 45;

  @Override
  protected void endLevel() {
    if (getClock().getTime() >= TIME_LIMIT) {
      // TODO: PRESENT LOSE SCREEN
    } else if (isPacmanConsumed()){
      // TODO: PRESENT WIN SCREEN
    }
  }
}
