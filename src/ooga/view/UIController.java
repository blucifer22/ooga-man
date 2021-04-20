package ooga.view;

import java.util.Stack;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ooga.model.api.GameStateObservationComposite;
import ooga.view.internal_api.ViewStackManager;
import ooga.controller.GameStateController;
import ooga.view.internal_api.MainMenuResponder;
import ooga.view.io.HumanInputConsumer;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.uiservice.PreferenceService;
import ooga.view.uiservice.ServiceProvider;
import ooga.view.uiservice.UIPreferenceService;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.GameView;
import ooga.view.views.MenuView;
import ooga.view.views.PreferenceView;

public class UIController implements MainMenuResponder, ViewStackManager {

  // constants
  private static final double DEFAULT_STAGE_SIZE = 600;

  // controlled elements
  private final Stage primaryStage;
  private final GameStateController gameController;
  private GameView gameView;
  private final Stack<Scene> viewStack;

  // shared UI dependencies
  private final HumanInputConsumer inputConsumer;
  private final UIPreferenceService preferenceService;
  private final UIServiceProvider serviceProvider;

  public UIController(Stage primaryStage, GameStateController gameController,
      HumanInputConsumer inputConsumer) {
    // Configure Data Sources & Displays
    this.inputConsumer = inputConsumer;
    this.gameController = gameController;
    this.viewStack = new Stack<>();

    // initialize shared dependencies
    BundledLanguageService languageService = new BundledLanguageService();
    SerializedThemeService themeService = new SerializedThemeService();
    this.serviceProvider = new ServiceProvider(themeService, languageService, this);
    this.preferenceService = new PreferenceService(themeService, languageService);

    // Stage Prep
    this.primaryStage = primaryStage;
    this.primaryStage.setScene(new Scene(new MenuView(this.serviceProvider, this).getRenderingNode(),
        DEFAULT_STAGE_SIZE, DEFAULT_STAGE_SIZE));
    this.primaryStage.titleProperty().bind(this.serviceProvider.languageService().getLocalizedString("pacman"));
    this.viewStack.add(this.primaryStage.getScene());

    // Allow user interaction
    this.primaryStage.show();
  }

  @Override
  public void startGame() {
    this.gameView = new GameView(this.serviceProvider);
    gameController.startGame(this.gameView);
    showGameView();
  }

  @Override
  public void openLevelBuilder() {
    // TODO: implement level builder
  }

  @Override
  public void openPreferences() {
    PreferenceView prefView = new PreferenceView(this.serviceProvider, this.preferenceService);
    showScene(new Scene((prefView.getRenderingNode())), true);
  }

  @Override
  public void unwind() {
    showScene(viewStack.pop(), false);
  }

  private void redirectInput(Scene s) {
    s.setOnKeyPressed(e -> inputConsumer.onKeyPress(e.getCode()));
    s.setOnKeyReleased(e -> inputConsumer.onKeyRelease(e.getCode()));
  }

  private void showGameView() {
    Scene gameViewScene = new Scene(this.gameView.getRenderingNode());
    showScene(gameViewScene, true);
  }

  private void showScene(Scene s, boolean addToStack) {
    double oldWidth = primaryStage.getWidth();
    double oldHeight = primaryStage.getHeight();
    if(addToStack) {
      this.viewStack.push(this.primaryStage.getScene());
    }
    this.primaryStage.setScene(s);
    this.primaryStage.setWidth(oldWidth);
    this.primaryStage.setHeight(oldHeight);
    redirectInput(s);
  }
}
