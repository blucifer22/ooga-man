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
   * @return Pacman player
   */
  ImmutablePlayer getPacmanPlayer();

  /**
   * Gets the current Ghost player, providing access to their ID and score
   *
   * @return Ghosts player
   */
  ImmutablePlayer getGhostsPlayer();

  /**
   * Gets the number of Pac-Man lives remaining
   *
   * @return number of lives remaining
   */
  int getPacmanLivesRemaining();
}
