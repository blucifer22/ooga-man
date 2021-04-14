package ooga.model;

import ooga.model.sprites.Sprite;

import java.util.List;
import ooga.util.GameClock;

public interface MutableGameState {

  /**
   * @param score
   */
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

  GameClock getClock();

  void registerEventListener(Sprite listener);

  void notifyPowerupListeners(PacmanPowerupEvent type);
}