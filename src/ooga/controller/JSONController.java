package ooga.controller;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import ooga.model.BlinkyAI;
import ooga.model.InputSource;
import ooga.model.PacmanGameState;
import ooga.model.PacmanLevel;
import ooga.model.Player;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.sprites.Ghost;
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
  private final HumanInputManager inputManager;
  private final JSONDescriptionFactory jsonDescriptionFactory;

  public JSONController(Stage primaryStage) {
    this.inputManager = new HumanInputManager(KeybindingType.PLAYER_1);
    this.uiController = new UIController(primaryStage, this, this.inputManager);
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

      // TODO: Implement input source assignment
      pgs.getSprites().get(2).setInputSource(inputManager);

      InputSource source =
          new BlinkyAI(
              pgs.getGrid(),
              (Ghost) pgs.getSprites().get(1),
              pgs.getSprites().get(2),
              pgs.getSprites().get(0));
      pgs.getSprites().get(1).setInputSource(source);

      pgs.setPlayers(new Player(1, inputManager), null);

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
