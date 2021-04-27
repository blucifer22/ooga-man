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

  /** @param score */
  void incrementScore(int score);

  int getScore();

  void prepareRemove(Sprite sprite);

  /**
   * Gets the list of other Sprites that resides in the same list as this sprite
   *
   * @param sprite
   * @return
   */
  List<Sprite> getCollidingWith(Sprite sprite);

  void addSprite(Sprite sprite);

  PacmanGrid getGrid();

  Clock getClock();

  void registerEventListener(GameEventObserver listener);

  void broadcastEvent(GameEvent type);

  void isPacmanDead(boolean isPacmanDead);

  AudioManager getAudioManager();
}
