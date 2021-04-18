package ooga.model.api;

import ooga.model.ImmutablePlayer;

/**
 * Information to display about the current state of the Pac-man game can be read through this
 * interface.
 */
public interface GameStateObservable {

  /**
   * Gets the current Pacman player, providing access to their ID and score.
   *
   * @return Pacman player.  Null if game mode is single player and controlling ghosts.
   */
  ImmutablePlayer getPacmanPlayer();

  /**
   * Gets the current Ghost player, providing access to their ID and score
   *
   * @return Ghosts player.  Null if game mode is single player and controlling Pac-Man.
   */
  ImmutablePlayer getGhostsPlayer();

  /**
   * Gets the number of Pac-Man lives remaining
   *
   * @return number of lives remaining
   */
  int getPacmanLivesRemaining();
}
