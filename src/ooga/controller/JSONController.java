package ooga.controller;

import java.io.File;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import ooga.model.PacmanGameState;
import ooga.model.PacmanGameStateAdversarial;
import ooga.model.PacmanGameStateChase;
import ooga.model.api.GameStateObservationComposite;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.view.UIController;

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
  private final HumanInputConsumerComposite compositeConsumer;
  private Timeline animation;

  /**
   * This is the primary constructor for the JSONController class, which is effectively the only
   * middleware for the application. This controller requires a JavaFX Stage on which to act, and
   * will then attach the appropriate observable components to it to instantiate a PacmanGameState.
   *
   * @param primaryStage The JavaFX Stage on which to create the new GameState.
   */
  public JSONController(Stage primaryStage) {
    // instantiate composite input receiver
    this.compositeConsumer = new HumanInputConsumerComposite();
    this.uiController = new UIController(primaryStage, this, compositeConsumer);
    jsonDescriptionFactory = new JSONDescriptionFactory();
  }

  /**
   * This method is responsible for starting a new game of Pac-Man. The particular game mode is
   * encoded within the JSON that encodes the specific level, and the appropriate PacmanGameState
   * is constructed accordingly.
   *
   * @param rootObserver The root GameStateObservation object will monitor the currently live
   *                     PacmanGameState
   */
  @Override
  public void startGame(GameStateObservationComposite rootObserver) {
    if (animation != null) {
      animation.stop();
    }
    try {
      File levelFile = uiController.requestUserFile(new File("data/levels"));
      if (levelFile == null || !levelFile.exists() || !levelFile.isFile()) {
        throw new IllegalArgumentException("badFileError");
      }

      HumanInputManager player1 = new HumanInputManager(KeybindingType.PLAYER_1);
      HumanInputManager player2 = new HumanInputManager(KeybindingType.PLAYER_2);
      compositeConsumer.clearConsumers();
      compositeConsumer.addConsumers(player1, player2);

      LevelDescription description = jsonDescriptionFactory.getLevelDescriptionFromJSON(levelFile.getPath());
      final PacmanGameState pgs = switch (description.getGameMode()) {
        case "CLASSIC" -> new PacmanGameState();
        case "CHASE" -> new PacmanGameStateChase();
        case "ADVERSARIAL" -> new PacmanGameStateAdversarial();
        default -> throw new IllegalArgumentException("invalidGameModeJSONError");
      };

      pgs.addSpriteExistenceObserver(rootObserver.spriteExistenceObserver());
      pgs.addGridRebuildObserver(rootObserver.gridRebuildObserver());
      pgs.addAudioObserver(rootObserver.audioObserver());
      pgs.addGameStateObserver(rootObserver.gameStateObserver());
      pgs.initPacmanLevelFromJSON(levelFile.getPath(), player1, player2);

      KeyFrame frame = new KeyFrame(Duration.seconds(TIMESTEP), e -> {
        try {
          pgs.step(TIMESTEP);
        } catch (Exception exception) {
          uiController.handleException("backendInternalError");
        }
      });
      this.animation = new Timeline();
      animation.setCycleCount(Timeline.INDEFINITE);
      animation.getKeyFrames().add(frame);
      animation.play();
    } catch (Exception e) {
      uiController.handleException(e.getMessage());
    }
  }
}
