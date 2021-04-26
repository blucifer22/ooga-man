package ooga.model.api;

/**
 * Defines interface that the view can interact with when displaying player information
 *
 * @author George Hong
 */
public interface ImmutablePlayer {

  /**
   * Gets the current score of the player across all levels and rounds
   *
   * @return current score of the player
   */
  int getScore();

  /**
   * Gets the tracked rounds that a player has won over multiple rounds for certain game modes.
   *
   * @return number of rounds won
   */
  int getRoundWins();

  /**
   * Get the ID of the current player.
   *
   * @return ID of the player
   */
  int getID();
}
