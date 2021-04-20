package ooga.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import ooga.model.ai.GhostAI;
import ooga.model.PacmanGameState;
import ooga.model.ai.PinkyAI;
import ooga.model.Player;
import ooga.model.SpriteCoordinates;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.sprites.Blinky;
import ooga.model.sprites.Clyde;
import ooga.model.sprites.Dot;
import ooga.model.sprites.Inky;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Pinky;
import ooga.model.sprites.PowerPill;
import ooga.model.sprites.TeleporterOverlay;
import ooga.util.Vec2;
import ooga.view.UIController;
import ooga.view.views.GameView;

public class Controller implements GameStateController {

  private static final double TIMESTEP = 1.0 / 60.0;
  private final UIController uiController;
  private final HumanInputManager inputManager;
  private GhostAI blinkyAI;
  private GhostAI inkyAI;
  private GhostAI pinkyAI;
  private GhostAI clydeAI;

  public Controller(Stage primaryStage) {
    this.inputManager = new HumanInputManager(KeybindingType.PLAYER_1);
    this.uiController = new UIController(primaryStage, this, this.inputManager);
    // startGame();
  }

  public void startGame() {
    PacmanGameState pgs = new PacmanGameState();
    GameView gv = uiController.getGameView(); // TODO: abstract GameView to an interface here

    pgs.addSpriteExistenceObserver(gv.getSpriteExistenceObserver());
    pgs.addGridRebuildObserver(gv.getGridRebuildObserver());

    try {
      pgs.loadGrid(
          new JSONDescriptionFactory()
              .getGridDescriptionFromJSON("data/levels/grids/demo_grid.json"));
    } catch (Exception e) {

    }

    PacMan pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(0, 0), 5.0);

    Blinky blinky = new Blinky(new SpriteCoordinates(new Vec2(11.5, 11.5)), new Vec2(0, 0), 5.0);
    Inky inky = new Inky(new SpriteCoordinates(new Vec2(11.5, 11.5)), new Vec2(0, 0), 5.0);
    Pinky pinky = new Pinky(new SpriteCoordinates(new Vec2(11.5, 11.5)), new Vec2(0, 0), 5.0);
    Clyde clyde = new Clyde(new SpriteCoordinates(new Vec2(11.5, 11.5)), new Vec2(0, 0), 5.0);

    Dot dot1 = new Dot(new SpriteCoordinates(new Vec2(4.5, 4.5)), new Vec2(0, 0));
    Dot dot2 = new Dot(new SpriteCoordinates(new Vec2(1.5, 3.5)), new Vec2(0, 0));
    Dot dot3 = new Dot(new SpriteCoordinates(new Vec2(1.5, 8.5)), new Vec2(0, 0));

    PowerPill powerPill = new PowerPill(new SpriteCoordinates(new Vec2(1.5, 6.5)), new Vec2(0, 0));

    pacman.setInputSource(this.inputManager);

//    PacmanAI pacmanBasicAI = new PacmanAI(pgs.getGrid(), pacman);
//    pacmanBasicAI.addTarget(blinky);
//    pacmanBasicAI.addTarget(pinky);
//    pacmanBasicAI.addTarget(clyde);
//    pacmanBasicAI.addTarget(inky);
//    pacman.setInputSource(pacmanBasicAI);

    this.blinkyAI = new GhostAI(pgs.getGrid(), blinky);
    blinkyAI.setTarget(pacman);
    blinky.setInputSource(this.blinkyAI);
    this.inkyAI = new PinkyAI(pgs.getGrid(), inky);
    inkyAI.setTarget(pacman);
    inky.setInputSource(this.inkyAI);
    this.pinkyAI = new GhostAI(pgs.getGrid(), pinky);
    pinkyAI.setTarget(pacman);
    pinky.setInputSource(this.pinkyAI);
    this.clydeAI = new PinkyAI(pgs.getGrid(), clyde);
    clydeAI.setTarget(pacman);
    clyde.setInputSource(this.clydeAI);

    TeleporterOverlay teleporter1 = new TeleporterOverlay(new SpriteCoordinates(new Vec2(1.5, 8.5)));
    TeleporterOverlay teleporter2 = new TeleporterOverlay(new SpriteCoordinates(new Vec2(15.5, 8.5)));
    teleporter1.connectTeleporter(teleporter2);
    teleporter2.connectTeleporter(teleporter1);
    pgs.addSprite(teleporter1);
    pgs.addSprite(teleporter2);

    pgs.addSprite(pacman);
    pgs.addSprite(blinky);
    pgs.addSprite(inky);
    pgs.addSprite(pinky);
    pgs.addSprite(clyde);
    pgs.addSprite(dot1);
    pgs.addSprite(dot2);
    pgs.addSprite(dot3);
    pgs.addSprite(powerPill);

    //uiController.showGameView();

    //    pgs.step(TIMESTEP);
    //    pgs.step(TIMESTEP);
    pgs.setPlayers(new Player(1, new HumanInputManager(KeybindingType.PLAYER_1)), null);

    KeyFrame frame = new KeyFrame(Duration.seconds(TIMESTEP), e -> pgs.step(TIMESTEP)); //
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }
}
