package ooga.controller;

import ooga.model.api.GameStateObservationComposite;

public interface GameStateController {
  void startGame(GameStateObservationComposite rootObserver);

  void pauseGame();
}
