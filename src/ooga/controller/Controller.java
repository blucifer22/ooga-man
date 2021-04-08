package ooga.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;
import ooga.view.UIController;
import ooga.view.views.GameView;

public class Controller {

  private static final double TIMESTEP = 1.0/60.0;
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
    pgs.addGridRebuildObserver(gv.getGridRebuildObserver());

    try {
      pgs.loadGrid(new JSONDescriptionFactory().getGridDescriptionFromJSON("data/levels/grids/test_grid.json"));
    } catch(Exception e) {

    }

    PacMan pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(0,0), 5.0);
    Ghost blinky = new Ghost(new SpriteCoordinates(new Vec2(1.5, 4.5)), new Vec2(0,0), 5.0);

    pacman.setInputSource(this.inputManager);
    blinky.setInputSource(this.inputManager);

    pgs.addSprite(pacman);
    pgs.addSprite(blinky);

    uiController.showGameView();

//    pgs.step(TIMESTEP);
//    pgs.step(TIMESTEP);

    KeyFrame frame = new KeyFrame(Duration.seconds(TIMESTEP), e -> pgs.step(TIMESTEP)); //
    // TODO: remove grid from step parameter
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }
}
