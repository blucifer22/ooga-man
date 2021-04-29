package ooga.model;

import java.util.List;
import ooga.model.api.GameEventObserver;
import ooga.model.audio.AudioManager;
import ooga.model.grid.PacmanGrid;
import ooga.model.sprites.Sprite;
import ooga.util.Clock;

/**
 * Interface that selectively exposes parts of a game state, as needed
 * by classes which must update the state.
 *
 * @author Franklin Wei
 * @author George Hong
 * @author Marc Chmielewski
 * @author Matthew Belissary
 */
public interface MutableGameState {

  /**
   * Increase score.
   * @param score Increment.
   */
  void incrementScore(int score);

  /**
   * Get current score.
   * @return Current score.
   */
  int getScore();

  /**
   * Mark a sprite for deletion.
   * @param sprite Sprite to delete.
   */
  void prepareRemove(Sprite sprite);

  /**
   * Gets the list of other Sprites that resides in the same list as this sprite
   *
   * @param sprite Main sprite.
   * @return Colliding sprites.
   */
  List<Sprite> getCollidingWith(Sprite sprite);

  /**
   * Add a sprite to the game.
   * @param sprite Sprite to add.
   */
  void addSprite(Sprite sprite);

  /**
   * Get grid on which the game takes place.
   * @return Grid.
   */
  PacmanGrid getGrid();

  /**
   * Get game clock.
   * @return Game clock.
   */
  Clock getClock();

  /**
   * Add an event listener.
   * @param listener Game event listener to add.
   */
  void registerEventListener(GameEventObserver listener);

  /**
   * Broadcast a game-wide event.
   * @param type Type of event to broadcast.
   */
  void broadcastEvent(GameEvent type);

  /**
   * Mark pacman as dead.
   * @param isPacmanDead Whether pacman is dead.
   */
  void isPacmanDead(boolean isPacmanDead);

  /**
   * Retrieve the audio manager.
   * @return Audio manager.
   */
  AudioManager getAudioManager();
}
