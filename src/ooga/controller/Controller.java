package ooga.controller;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.model.PacmanGameState;
import ooga.model.Sprite;
import ooga.model.SpriteCoordinates;
import ooga.model.TileCoordinates;
import ooga.view.GameGridView;
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
    pgs.addExistenceObserver(gv);
    primaryStage.setScene(new Scene((Pane) (new GameGridView(10,10).getRenderingNode()), 400,
        400));
    gv.onSpriteCreation(new Sprite() {

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
        return new SpriteCoordinates() {

          @Override
          public TileCoordinates getTileCoordinates() {
            return new TileCoordinates();
          }
        };
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
