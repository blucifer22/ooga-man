package ooga.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import ooga.model.InputSource;
import ooga.model.PacmanGameState;
import ooga.model.PacmanGrid;
import ooga.model.PacmanLevel;
import ooga.model.Player;
import ooga.model.ai.BlinkyAI;
import ooga.model.ai.GhostAI;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Sprite;
import ooga.view.UIController;
import ooga.view.views.GameView;

/**
 * This is a controller implementation that is able to load Pac-Man levels from JSON!
 *
 * @author Marc Chmielewski
 * @author George Hong
 */
public class JSONController implements GameStateController {

  private static final double TIMESTEP = 1.0 / 60.0;
  private final UIController uiController;
  private final JSONDescriptionFactory jsonDescriptionFactory;
  private final HumanInputManager player1;
  private final HumanInputManager player2;
  private InputSourceFactory inputSourceFactory;

  public JSONController(Stage primaryStage) {
    this.player1 = new HumanInputManager(KeybindingType.PLAYER_1);
    player2 = new HumanInputManager(KeybindingType.PLAYER_2);
    this.uiController = new UIController(primaryStage, this, this.player1);
    jsonDescriptionFactory = new JSONDescriptionFactory();
  }

  @Override
  public void startGame() {
    try {
      PacmanGameState pgs = new PacmanGameState();
      GameView gv = uiController.getGameView(); // TODO: abstract GameView to an interface here

      pgs.addSpriteExistenceObserver(gv.getSpriteExistenceObserver());
      pgs.addGridRebuildObserver(gv.getGridRebuildObserver());

      pgs.loadPacmanLevel(loadLevelFromJSON("data/levels/test_level_1.json"));
      inputSourceFactory = new InputSourceFactory(pgs, player1, player2);

      pgs.setPlayers(new Player(1, player1), null);

      KeyFrame frame = new KeyFrame(Duration.seconds(TIMESTEP), e -> pgs.step(TIMESTEP)); //
      // TODO: remove grid from step parameter
      Timeline animation = new Timeline();
      animation.setCycleCount(Timeline.INDEFINITE);
      animation.getKeyFrames().add(frame);
      animation.play();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private PacmanLevel loadLevelFromJSON(String filepath) throws IOException {
    LevelDescription levelDescription =
        jsonDescriptionFactory.getLevelDescriptionFromJSON(filepath);
    return new PacmanLevel(levelDescription);
  }
}
