package ooga.controller;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import ooga.model.InputSource;
import ooga.model.PacmanGameState;
import ooga.model.PacmanLevel;
import ooga.model.Player;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.sprites.Sprite;
import ooga.view.UIController;
import ooga.view.views.GameView;

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

      InputSource him = new HumanInputManager(KeybindingType.PLAYER_1);
      for(Sprite sprite : pgs.getSprites()) {
        sprite.setInputSource(him);
      }

      pgs.setPlayers(new Player(1, new HumanInputManager(KeybindingType.PLAYER_1)), null);

      System.out.println(pgs.getGrid());
      System.out.println(pgs.getSprites());

      KeyFrame frame = new KeyFrame(Duration.seconds(TIMESTEP), e -> pgs.step(TIMESTEP)); //
      // TODO: remove grid from step parameter
      Timeline animation = new Timeline();
      animation.setCycleCount(Timeline.INDEFINITE);
      animation.getKeyFrames().add(frame);
      animation.play();
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private PacmanLevel loadLevelFromJSON(String filepath) throws IOException {
    LevelDescription levelDescription = jsonDescriptionFactory.getLevelDescriptionFromJSON(filepath);
    return new PacmanLevel(levelDescription);
  }
}
