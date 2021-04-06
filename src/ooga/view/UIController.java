package ooga.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ooga.view.io.HumanInputConsumer;
import ooga.view.theme.ConcreteThemeService;
import ooga.view.theme.ThemeService;
import ooga.view.views.GameView;

public class UIController {

  private final Stage primaryStage;
  private final GameView gameView;
  private final HumanInputConsumer inputConsumer;

  public UIController(Stage primaryStage, HumanInputConsumer inputConsumer) {
    this.primaryStage = primaryStage;
    this.inputConsumer = inputConsumer;

    ThemeService themeService = new ConcreteThemeService();
    this.gameView = new GameView(themeService);
    
    this.primaryStage.show();
  }

  public void showMenu() {
    // TODO: show menu
  }

  public void showGameView() {
    Scene gameViewScene = new Scene(this.gameView.getRenderingNode(), primaryStage.getWidth(),
        primaryStage.getHeight());

    this.primaryStage.setScene(gameViewScene);
    redirectInput(gameViewScene);
  }

  public GameView getGameView() {
    return this.gameView;
  }

  private void redirectInput(Scene s) {
    s.setOnKeyPressed(e -> inputConsumer.onKeyPress(e.getCode()));
    s.setOnKeyReleased(e -> inputConsumer.onKeyRelease(e.getCode()));
  }
}
