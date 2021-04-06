package ooga.controller;

import javafx.stage.Stage;
import ooga.model.PacmanGameState;
import ooga.model.SpriteCoordinates;
import ooga.model.TileCoordinates;
import ooga.model.api.ObservableGrid;
import ooga.model.api.ObservableSprite;
import ooga.model.api.ObservableTile;
import ooga.model.api.SpriteEvent.EventType;
import ooga.model.api.SpriteObserver;
import ooga.model.api.TileEvent;
import ooga.model.api.TileObserver;
import ooga.util.Vec2;
import ooga.view.UIController;
import ooga.view.views.GameView;

public class Controller {

  private final UIController uiController;
  private final HumanInputManager inputManager;

  public Controller(Stage primaryStage) {
    this.inputManager = new HumanInputManager();
    this.uiController = new UIController(primaryStage, this.inputManager);
    startGame();
  }

  public void startGame() {
    PacmanGameState pgs = new PacmanGameState();
    GameView gv = uiController.getGameView(); // TODO: abstract GameView to an interface here

    pgs.addSpriteExistenceObserver(gv.getSpriteExistenceObserver());
    gv.getSpriteExistenceObserver()
        .onSpriteCreation(
            new ObservableSprite() {

              @Override
              public boolean isVisible() {
                return true;
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
              public Vec2 getDirection() {
                return new Vec2(1, 0);
              }

              @Override
              public void addObserver(SpriteObserver so, EventType... observedEvents) {}

              @Override
              public void removeObserver(SpriteObserver so) {}
            });

    gv.getSpriteExistenceObserver()
        .onSpriteCreation(
            new ObservableSprite() {

              @Override
              public boolean isVisible() {
                return true;
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
              public Vec2 getDirection() {
                return new Vec2(1, 0);
              }

              @Override
              public void addObserver(SpriteObserver so, EventType... observedEvents) {}

              @Override
              public void removeObserver(SpriteObserver so) {}
            });
    pgs.addGridRebuildObserver(gv.getGridRebuildObserver());

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
          public void addTileObserver(TileObserver observer, TileEvent.EventType... events) {

          }
        };
      }
    };

    gv.getGridRebuildObserver().onGridRebuild(grid);

    uiController.showGameView();
  }
}
