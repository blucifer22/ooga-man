package ooga.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ooga.model.PacmanGameState;
import ooga.model.Sprite;
import ooga.model.SpriteCoordinates;
import ooga.model.TileCoordinates;
import ooga.model.api.ObservableGrid;
import ooga.model.api.ObservableTile;
import ooga.model.api.TileEvent.EventType;
import ooga.model.api.TileObserver;
import ooga.util.Vec2;
import ooga.view.theme.ConcreteThemeService;
import ooga.view.theme.ThemeService;
import ooga.view.views.GameView;

public class Controller {

  private final Stage primaryStage;

  public Controller(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.show();
    startGame();
  }

  public void startGame() {
    PacmanGameState pgs = new PacmanGameState();
    ThemeService ts = new ConcreteThemeService();
    GameView gv = new GameView(ts);
    pgs.addSpriteExistenceObserver(gv.getSpriteExistenceObserver());
    pgs.addGridRebuildObserver(gv.getGridRebuildObserver());
    primaryStage.setScene(new Scene(gv.getRenderingNode(), 400.0,
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
        return "pacman";
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
        return "ghost";
      }

      @Override
      public SpriteCoordinates getCenter() {
        return new SpriteCoordinates(new Vec2(1.5, 5.5));
      }

      @Override
      public void step(double dt) {

      }

      @Override
      public boolean mustBeConsumed() {
        return false;
      }
    });

    ObservableGrid grid = new ObservableGrid() {

      @Override
      public int getWidth() {
        return 10;
      }

      @Override
      public int getHeight() {
        return 10;
      }

      @Override
      public ObservableTile getTile(TileCoordinates tileCoordinates) {
        return new ObservableTile() {

          @Override
          public TileCoordinates getCoordinates() {
            return tileCoordinates;
          }

          @Override
          public String getType() {
            return "tile";
          }

          @Override
          public void addTileObserver(TileObserver observer, EventType... events) {
          }
        };
      }
    };

    gv.getGridRebuildObserver().onGridRebuild(grid);
  }
}
