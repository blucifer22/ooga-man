package ooga.controller;

import javafx.stage.Stage;
import ooga.view.UIController;

public class JSONController implements GameStateController {

  private static final double TIMESTEP = 1.0 / 60.0;
  private final UIController uiController;
  private final HumanInputManager inputManager;

  public JSONController(Stage primaryStage) {
    this.inputManager = new HumanInputManager(KeybindingType.PLAYER_1);
    this.uiController = new UIController(primaryStage, this, this.inputManager);
  }

  @Override
  public void startGame() {

  }
}
