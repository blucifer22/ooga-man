package ooga.controller;

import javafx.stage.Stage;
import ooga.model.PacmanGameState;
import ooga.view.GameGridView;

public class Controller {
  private final Stage primaryStage;

  public Controller(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void startGame() {
    PacmanGameState pgs = new PacmanGameState();
    GameGridView gv = new GameGridView(0, 0);
    pgs.addExistenceObserver(gv);
    // start game through a call to PacmanGameState!
  }
}
