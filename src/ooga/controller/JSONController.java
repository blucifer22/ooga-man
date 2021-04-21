package ooga.controller;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import ooga.model.PacmanGameState;
import ooga.model.Player;
import ooga.model.api.GameStateObservationComposite;
import ooga.model.leveldescription.JSONDescriptionFactory;
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
  private final HumanInputManager player1;
  private final HumanInputManager player2;

  public JSONController(Stage primaryStage) {
    this.player1 = new HumanInputManager(KeybindingType.PLAYER_1);
    player2 = new HumanInputManager(KeybindingType.PLAYER_2);
    this.uiController = new UIController(primaryStage, this, this.player1);
    jsonDescriptionFactory = new JSONDescriptionFactory();
  }

  @Override
  public void startGame(GameStateObservationComposite rootObserver) {
    try {
      PacmanGameState pgs = new PacmanGameState();

      pgs.addSpriteExistenceObserver(rootObserver.spriteExistenceObserver());
      pgs.addGridRebuildObserver(rootObserver.gridRebuildObserver());

      pgs.initPacmanLevelFromJSON("data/levels/test_level_1.json", player1, player2);


      pgs.setPlayers(new Player(1, player1), null);

      KeyFrame frame = new KeyFrame(Duration.seconds(TIMESTEP), e -> {
        pgs.step(TIMESTEP);
      }); //
      // TODO: remove grid from step parameter
      Timeline animation = new Timeline();
      animation.setCycleCount(Timeline.INDEFINITE);
      animation.getKeyFrames().add(frame);
      animation.play();
    } catch (IOException e) {
      // TODO: Pop-up exception handling!
      System.err.println(e.getMessage());
    }
  }


}
