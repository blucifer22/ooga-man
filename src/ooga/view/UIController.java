package ooga.view;

import java.util.Stack;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ooga.ViewStackManager;
import ooga.controller.GameStateController;
import ooga.view.internal_api.MainMenuResponder;
import ooga.view.internal_api.PreferenceResponder;
import ooga.view.io.HumanInputConsumer;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.views.GameView;
import ooga.view.views.MenuView;
import ooga.view.views.PreferenceView;

public class UIController implements MainMenuResponder, PreferenceResponder, ViewStackManager {

  // constants
  private static final double DEFAULT_STAGE_SIZE = 600;

  // controlled elements
  private final Stage primaryStage;
  private final GameStateController gameController;
  private final GameView gameView;
  private final Stack<Scene> viewStack;

  // shared UI dependencies
  private final BundledLanguageService languageService;
  private final SerializedThemeService themeService;
  private final HumanInputConsumer inputConsumer;

  public UIController(Stage primaryStage, GameStateController gameController,
      HumanInputConsumer inputConsumer) {
    // Configure Data Sources & Displays
    this.primaryStage = primaryStage;
    this.primaryStage.setWidth(DEFAULT_STAGE_SIZE);
    this.primaryStage.setHeight(DEFAULT_STAGE_SIZE);
    this.inputConsumer = inputConsumer;
    this.gameController = gameController;
    this.languageService = new BundledLanguageService();
    this.themeService = new SerializedThemeService();
    this.viewStack = new Stack<>();

    // Stage Prep
    this.primaryStage.titleProperty().bind(this.languageService.getLocalizedString("pacman"));

    // Prep Game View
    this.gameView = new GameView(this.themeService);

    // Allow user interaction
    this.primaryStage.show();
  }

  public void showMenu() {
    this.viewStack.add(this.primaryStage.getScene());
    this.primaryStage.setScene(new Scene(new MenuView(this, this.themeService,
        this.languageService).getRenderingNode(), primaryStage.getWidth(), primaryStage.getHeight()));
  }

  public void showGameView() {
    this.viewStack.add(this.primaryStage.getScene());
    Scene gameViewScene = this.gameView.getRenderingNode().getScene();

    if (gameViewScene == null) {
      gameViewScene = new Scene(this.gameView.getRenderingNode(), primaryStage.getWidth(),
          primaryStage.getHeight());
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
    this.viewStack.add(primaryStage.getScene());
    this.primaryStage.setScene(new Scene((new PreferenceView(this, themeService, languageService,
        this).getRenderingNode()), primaryStage.getWidth(), primaryStage.getHeight()));
  }

  @Override
  public void setLanguage(String language) {
    this.languageService.setLanguage(language);
  }

  @Override
  public void unwind() {
    System.out.println(viewStack);
    this.primaryStage.setScene(viewStack.pop());
    this.primaryStage.setResizable(true);
  }
}
