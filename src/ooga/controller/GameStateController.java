package ooga.controller;

import ooga.model.api.GameStateObservationComposite;

/**
 * GameStateController defines the interface between objects that can start and pause Pac-Man games.
 * These objects are the only objects that are able to start and pause Pac-Man games.
 *
 * <p>Contains two methods:
 *
 * <p>startGame - starts games
 *
 * <p>pauseGame - pauses games
 *
 * @author David Coffman
 */
public interface GameStateController {

  /**
   * startGame starts the Pac-Man game by acting on the GameStateObservationComposite and handling
   * loading of sprites, animations, etc.
   *
   * @param rootObserver The GameStateObservationComposite that represents the in-game objects of
   *                     the PacmanGameState that is being played
   */
  void startGame(GameStateObservationComposite rootObserver);

  /**
   * pauseGame pauses the currently running Pac-Man game. This consists of stopping animations and
   * keeping back-end entities from colliding, etc.
   */
  void pauseGame();
}
