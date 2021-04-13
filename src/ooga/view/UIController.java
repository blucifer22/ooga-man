package ooga.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ooga.controller.GameStateController;
import ooga.view.io.HumanInputConsumer;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.views.GameView;
import ooga.view.views.MenuView;

public class UIController implements MainMenuResponder {

  private final Stage primaryStage;
  private final GameView gameView;
  private final BundledLanguageService languageService;
  private final SerializedThemeService themeService;
  private final HumanInputConsumer inputConsumer;
  private final GameStateController gameController;

  public UIController(Stage primaryStage, GameStateController gameController,
      HumanInputConsumer inputConsumer) {
    // Configure Data Sources & Displays
    this.primaryStage = primaryStage;
    this.inputConsumer = inputConsumer;
    this.gameController = gameController;
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
    this.primaryStage.setScene(new Scene(new MenuView(this, this.themeService,
        this.languageService).getRenderingNode(),
        400, 400));
  }

  public void showGameView() {
    Scene gameViewScene = this.gameView.getRenderingNode().getScene();

    if (gameViewScene == null) {
      gameViewScene = new Scene(this.gameView.getRenderingNode(), 600, 600);
    }

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

  @Override
  public void startGame() {
    gameController.startGame();
    showGameView();
  }

  @Override
  public void openLevelBuilder() {
    // TODO: implement level builder
  }

  @Override
  public void openPreferences() {
    // TODO: open preferences
  }
}
