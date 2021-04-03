package ooga.controller;

import javafx.stage.Stage;
import ooga.model.PacmanGameState;
import ooga.view.GameView;

public class Controller {
  private final Stage primaryStage;

  public Controller(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void startGame() {
    PacmanGameState pgs = new PacmanGameState();
    GameView gv = new GameView();
    pgs.addExistenceObserver(gv);
    // start game through a call to PacmanGameState!
  }
}
