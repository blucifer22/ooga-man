package ooga.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ooga.view.io.HumanInputConsumer;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.views.GameView;

public class UIController {

  private final Stage primaryStage;
  private final GameView gameView;
  private final BundledLanguageService languageService;
  private final SerializedThemeService themeService;
  private final HumanInputConsumer inputConsumer;

  public UIController(Stage primaryStage, HumanInputConsumer inputConsumer) {
    // Configure Data Sources & Displays
    this.primaryStage = primaryStage;
    this.inputConsumer = inputConsumer;
    this.languageService = new BundledLanguageService();
    this.themeService = new SerializedThemeService();

    // Stage Prep
    this.primaryStage.titleProperty().bind(this.languageService.getLocalizedString("pacman"));

    // Prep Game View
    this.gameView = new GameView(this.themeService);

    // Allow user interaction
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

  // TODO: abstract GameView to an interface here
  public GameView getGameView() {
    return this.gameView;
  }

  private void redirectInput(Scene s) {
    s.setOnKeyPressed(e -> inputConsumer.onKeyPress(e.getCode()));
    s.setOnKeyReleased(e -> inputConsumer.onKeyRelease(e.getCode()));
  }
}
