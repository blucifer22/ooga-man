package ooga.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ooga.view.theme.ConcreteThemeService;
import ooga.view.theme.ThemeService;
import ooga.view.views.GameView;

public class UIController {

  private final Stage primaryStage;
  private final Scene gameScene;
  private final GameView gameView;
  private final ThemeService themeService;

  public UIController(Stage primaryStage) {
    this.primaryStage = primaryStage;

    this.themeService = new ConcreteThemeService();
    this.gameView = new GameView(this.themeService);
    this.gameScene = new Scene(this.gameView.getRenderingNode(), 400.0, 400.0);
    
    this.primaryStage.show();
  }

  public void showMenu() {

  }

  public void showGameView() {
    this.primaryStage.setScene(this.gameScene);
  }


}
