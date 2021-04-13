package ooga.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import ooga.model.GhostAI;
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
import ooga.model.sprites.Blinky;
import ooga.model.sprites.Clyde;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Dot;
import ooga.model.sprites.Inky;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Pinky;
import ooga.model.sprites.PowerPill;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;
import ooga.view.UIController;
import ooga.view.views.GameView;

public class Controller implements GameStateController {

  private static final double TIMESTEP = 1.0/60.0;
  private final UIController uiController;
  private final HumanInputManager inputManager;
  private GhostAI blinkyAI;
  private GhostAI inkyAI;
  private GhostAI pinkyAI;
  private GhostAI clydeAI;

  public Controller(Stage primaryStage) {
    this.inputManager = new HumanInputManager();
    this.uiController = new UIController(primaryStage, this, this.inputManager);
    uiController.showMenu();
    //startGame();
  }

  public void startGame() {
    PacmanGameState pgs = new PacmanGameState();
    GameView gv = uiController.getGameView(); // TODO: abstract GameView to an interface here

    pgs.addSpriteExistenceObserver(gv.getSpriteExistenceObserver());
    pgs.addGridRebuildObserver(gv.getGridRebuildObserver());

    try {
      pgs.loadGrid(new JSONDescriptionFactory().getGridDescriptionFromJSON("data/levels/grids/demo_grid.json"));
    } catch(Exception e) {

    }

    PacMan pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(0,0), 5.0);

    Blinky blinky = new Blinky(new SpriteCoordinates(new Vec2(11.5, 11.5)), new Vec2(0,0), 5.0);
    Inky inky = new Inky(new SpriteCoordinates(new Vec2(11.5, 11.5)), new Vec2(0,0), 5.0);
    Pinky pinky = new Pinky(new SpriteCoordinates(new Vec2(11.5, 11.5)), new Vec2(0,0), 5.0);
    Clyde clyde = new Clyde(new SpriteCoordinates(new Vec2(11.5, 11.5)), new Vec2(0,0), 5.0);

    Dot dot1 = new Dot(new SpriteCoordinates(new Vec2(4.5, 4.5)), new Vec2(0, 0));
    Dot dot2 = new Dot(new SpriteCoordinates(new Vec2(1.5, 3.5)), new Vec2(0, 0));
    Dot dot3 = new Dot(new SpriteCoordinates(new Vec2(1.5, 8.5)), new Vec2(0, 0));

    PowerPill powerPill = new PowerPill(new SpriteCoordinates(new Vec2(1.5, 6.5)), new Vec2(0, 0));

    pacman.setInputSource(this.inputManager);

    this.blinkyAI = new GhostAI(pgs.getGrid(), blinky, pacman, 0.9);
    blinky.setInputSource(this.blinkyAI);
    this.inkyAI = new GhostAI(pgs.getGrid(), inky, pacman, 0.8);
    inky.setInputSource(this.inkyAI);
    this.pinkyAI = new GhostAI(pgs.getGrid(), pinky, pacman, 0.7);
    pinky.setInputSource(this.pinkyAI);
    this.clydeAI = new GhostAI(pgs.getGrid(), clyde, pacman, 0.6);
    clyde.setInputSource(this.clydeAI);

    pgs.addSprite(pacman);
    pgs.addSprite(blinky);
    pgs.addSprite(inky);
    pgs.addSprite(pinky);
    pgs.addSprite(clyde);
    pgs.addSprite(dot1);
    pgs.addSprite(dot2);
    pgs.addSprite(dot3);
    pgs.addSprite(powerPill);

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
