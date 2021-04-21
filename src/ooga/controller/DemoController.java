package ooga.controller;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import ooga.model.ai.BlinkyAI;
import ooga.model.PacmanGameState;
import ooga.model.ai.PinkyAI;
import ooga.model.Player;
import ooga.model.SpriteCoordinates;
import ooga.model.api.GameStateObservationComposite;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.sprites.Blinky;
import ooga.model.sprites.Cherry;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Dot;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Pinky;
import ooga.model.sprites.PowerPill;
import ooga.model.sprites.TeleporterOverlay;
import ooga.util.Vec2;
import ooga.view.UIController;
import ooga.view.views.GameView;

public class DemoController implements GameStateController {

  private static final double TIMESTEP = 1.0 / 60.0;
  private final UIController uiController;
  private final HumanInputManager inputManager;

  public DemoController(Stage primaryStage) {
    this.inputManager = new HumanInputManager(KeybindingType.PLAYER_1);
    this.uiController = new UIController(primaryStage, this, this.inputManager);
  }

  @Override
  public void startGame(GameStateObservationComposite rootObserver) {
    PacmanGameState pgs = new PacmanGameState();

    pgs.addSpriteExistenceObserver(rootObserver.spriteExistenceObserver());
    pgs.addGridRebuildObserver(rootObserver.gridRebuildObserver());

    try {
      pgs.loadGrid(new JSONDescriptionFactory()
          .getGridDescriptionFromJSON("data/levels/grids/demo_grid.json"));
    } catch (Exception e) {

    }

    PacMan pacman = new PacMan(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(0, 0), 5.0);
    Ghost blinky = new Blinky(new SpriteCoordinates(new Vec2(8.5, 8.5)), new Vec2(0, 0), 3.9);
    Ghost pinky = new Pinky(new SpriteCoordinates(new Vec2(8.5, 8.5)), new Vec2(0, 0), 4);
    Dot dot1 = new Dot(new SpriteCoordinates(new Vec2(4.5, 4.5)), new Vec2(0, 0));
    Dot dot2 = new Dot(new SpriteCoordinates(new Vec2(1.5, 3.5)), new Vec2(0, 0));
    PowerPill powerPill1 = new PowerPill(new SpriteCoordinates(new Vec2(1.5, 6.5)), new Vec2(0, 0));
    PowerPill powerPill2 = new PowerPill(new SpriteCoordinates(new Vec2(6.5, 9.5)), new Vec2(0, 0));
    PowerPill powerPill3 = new PowerPill(new SpriteCoordinates(new Vec2(4.5, 15.5)), new Vec2(0, 0));
    PowerPill powerPill4 = new PowerPill(new SpriteCoordinates(new Vec2(6.5, 6.5)), new Vec2(0, 0));
    Cherry cherry1 = new Cherry(new SpriteCoordinates(new Vec2(4.5, 8.5)), new Vec2(0, 0));
    Cherry cherry2 = new Cherry(new SpriteCoordinates(new Vec2(8.5, 1.5)), new Vec2(0, 0));

    TeleporterOverlay teleporter1 = new TeleporterOverlay(new SpriteCoordinates(new Vec2(1.5, 8.5)));
    TeleporterOverlay teleporter2 = new TeleporterOverlay(new SpriteCoordinates(new Vec2(15.5, 8.5)));
    teleporter1.connectTeleporter(teleporter2);
    teleporter2.connectTeleporter(teleporter1);
    pgs.addSprite(teleporter1);
    pgs.addSprite(teleporter2);


//    PacmanAI pacmanBasicAI = new PacmanAI(pgs.getGrid(), pacman);
//    pacmanBasicAI.addTarget(blinky);
//    pacmanBasicAI.addTarget(pinky);
//    pacman.setInputSource(pacmanBasicAI);

    pacman.setInputSource(this.inputManager);
    BlinkyAI inBlinky = new BlinkyAI(pgs.getGrid(), blinky);
    PinkyAI inPinky = new PinkyAI(pgs.getGrid(), pinky);

    inBlinky.setTarget(pacman);
    inPinky.setTarget(pacman);

    blinky.setInputSource(inBlinky);
    pinky.setInputSource(inPinky);

    pgs.addSprite(pacman);
    //pgs.registerEventListener(pacman);

    pgs.addSprite(blinky);
    //pgs.registerEventListener(blinky);
    pgs.addSprite(pinky);
    //pgs.registerEventListener(pinky);

    pgs.addSprite(dot1);
    //pgs.registerEventListener(dot1);
    pgs.addSprite(dot2);
    //pgs.registerEventListener(dot1);
    pgs.addSprite(cherry1);
    //pgs.registerEventListener(cherry1);
    pgs.addSprite(cherry2);
    //pgs.registerEventListener(cherry2);

    pgs.addSprite(powerPill1);
    pgs.addSprite(powerPill2);
    pgs.addSprite(powerPill3);
    pgs.addSprite(powerPill4);

    //pgs.registerEventListener(pacman);

    pgs.setPlayers(new Player(1, new HumanInputManager(KeybindingType.PLAYER_1)), null);

    KeyFrame frame = new KeyFrame(Duration.seconds(TIMESTEP), e -> pgs.step(TIMESTEP)); //
    // TODO: remove grid from step parameter
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }
}
