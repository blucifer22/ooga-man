package ooga.controller;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.model.PacmanGameState;
import ooga.model.Sprite;
import ooga.model.SpriteCoordinates;
import ooga.model.TileCoordinates;
import ooga.util.Vec2;
import ooga.view.GameView;

public class Controller {
  private final Stage primaryStage;

  public Controller(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.show();
    startGame();
  }

  public void startGame() {
    PacmanGameState pgs = new PacmanGameState();
    GameView gv = new GameView(10, 10);
    pgs.addSpriteExistenceObserver(gv.getSpriteExistenceObserver());
    primaryStage.setScene(new Scene((Pane) gv.getRenderingNode(), 400.0,
        400.0));
    gv.getSpriteExistenceObserver().onSpriteCreation(new Sprite() {

      @Override
      public boolean isVisible() {
        return true;
      }

      @Override
      public boolean isStationary() {
        return false;
      }

      @Override
      public String getType() {
        return null;
      }

      @Override
      public SpriteCoordinates getCenter() {
        return new SpriteCoordinates(new Vec2(1.5, 3.5));
      }

      @Override
      public void step(double dt) {

      }

      @Override
      public boolean mustBeConsumed() {
        return false;
      }
    });
  }
}
