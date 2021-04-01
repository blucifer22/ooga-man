package ooga.controller;

import ooga.model.PacmanGameState;
import ooga.view.GameView;

public class Controller {

  public void startGame() {
    PacmanGameState pgs = new PacmanGameState();
    GameView gv = new GameView();
    pgs.addExistenceObserver(gv);
    // start game through a call to PacmanGameState!
  }
}
