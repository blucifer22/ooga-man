package ooga.model.api;

import java.util.List;
import ooga.model.ImmutablePlayer;

/**
 * Information to display about the current state of the Pac-man game can be read through this
 * interface.
 */
public interface GameStateObservable {


  /**
   * Get a list of all players for this Pac-Man mode.  Each player provides access to a player ID
   * and score.  If applicable to the game-mode, the first Player corresponds to the player
   * controlling Pac-Man.
   *
   * @return List of all players
   */
  List<ImmutablePlayer> getPlayers();

  /**
   * Gets the number of Pac-Man lives remaining
   *
   * @return number of lives remaining
   */
  int getPacmanLivesRemaining();
}
