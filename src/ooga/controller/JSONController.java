package ooga.controller;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import ooga.model.PacmanGameState;
import ooga.model.PacmanGameStateAdversarial;
import ooga.model.PacmanGameStateChase;
import ooga.model.Player;
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

  private static final String FILEPATH = "data/levels/test_chase_level_2.json";

  public JSONController(Stage primaryStage) {
    // instantiate composite input receiver
    this.compositeConsumer = new HumanInputConsumerComposite();
    this.uiController = new UIController(primaryStage, this, compositeConsumer);
    jsonDescriptionFactory = new JSONDescriptionFactory();
  }

  @Override
  public void startGame(GameStateObservationComposite rootObserver) {
    if (animation != null) {
      animation.stop();
    }
    try {
      HumanInputManager player1 = new HumanInputManager(KeybindingType.PLAYER_1);
      HumanInputManager player2 = new HumanInputManager(KeybindingType.PLAYER_2);
      compositeConsumer.clearConsumers();
      compositeConsumer.addConsumers(player1, player2);

      LevelDescription description = jsonDescriptionFactory.getLevelDescriptionFromJSON(FILEPATH);
      PacmanGameState pgs = switch (description.getGameMode()) {
        case "CLASSIC" -> new PacmanGameState();
        case "CHASE" -> new PacmanGameStateChase();
        case "ADVERSARIAL" -> new PacmanGameStateAdversarial();
        default -> throw new IllegalArgumentException("YOU HAVE AN INVALID GAME MODE!");
      };

      pgs.addSpriteExistenceObserver(rootObserver.spriteExistenceObserver());
      pgs.addGridRebuildObserver(rootObserver.gridRebuildObserver());
      pgs.addAudioObserver(rootObserver.audioObserver());
      pgs.addGameStateObserver(rootObserver.gameStateObserver());

      pgs.initPacmanLevelFromJSON(FILEPATH, player1, player2);

      pgs.setPlayers(new Player(1, player1), null);

      PacmanGameState finalPgs = pgs;
      KeyFrame frame = new KeyFrame(Duration.seconds(TIMESTEP), e -> {
        finalPgs.step(TIMESTEP);
      }); //
      // TODO: remove grid from step parameter
      this.animation = new Timeline();
      animation.setCycleCount(Timeline.INDEFINITE);
      animation.getKeyFrames().add(frame);
      animation.play();
    } catch (IOException e) {
      // TODO: Pop-up exception handling!
      System.err.println(e.getMessage());
    }
  }


}
