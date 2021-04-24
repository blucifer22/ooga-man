package ooga.view.views.components;

import ooga.model.api.GameStateObservable;
import ooga.model.api.GameStateObserver;

public class ScoreboardCard implements GameStateObserver {
  public ScoreboardCard(GameStateObservable gameState) {

  }

  /**
   * Called when a game state change occurs, such as when the score or pacman lives remaining
   * changes
   *
   * @param state contains information about the Pac-Man game state
   */
  @Override
  public void onGameStateUpdate(GameStateObservable state) {
    // TODO: refresh everything
  }
}
