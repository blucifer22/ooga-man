package ooga.view;

import java.io.File;
import java.util.Stack;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import ooga.controller.GameStateController;
import ooga.model.leveldescription.LevelBuilder;
import ooga.model.leveldescription.Palette;
import ooga.view.audio.AudioService;
import ooga.view.audio.ThemedAudioService;
import ooga.view.exceptions.GraphicalExceptionService;
import ooga.view.exceptions.UIServicedException;
import ooga.view.internal_api.MainMenuResponder;
import ooga.view.internal_api.ViewStackService;
import ooga.view.io.HumanInputConsumer;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.uiservice.PreferenceService;
import ooga.view.uiservice.ServiceProvider;
import ooga.view.uiservice.UIPreferenceService;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.sceneroots.GameView;
import ooga.view.views.sceneroots.LevelBuilderView;
import ooga.view.views.sceneroots.MenuView;
import ooga.view.views.sceneroots.PreferenceView;

/**
 * Primary UI controller class. Manages the {@link javafx.application.Application}'s
 * {@link Stage}. Instantiates and de-instantiates views as appropriate. Not concretely
 * referenced by any other {@link ooga.view}. Implements {@link MainMenuResponder} and
 * {@link ViewStackService} to enable user interactions in other views to trigger view changes.
 *
 * Should <em>never</em> be referenced directly by either model or view classes.
 *
 * @author David Coffman
 */
public class UIController implements MainMenuResponder, ViewStackService {

  // constants
  private static final double DEFAULT_STAGE_SIZE = 600;

  // controlled elements
  private final Stage primaryStage;
  private final GameStateController gameController;
  private final Stack<Scene> viewStack;
  // shared UI dependencies
  private final HumanInputConsumer inputConsumer;
  private final UIPreferenceService preferenceService;
  private final UIServiceProvider serviceProvider;
  private GameView gameView;

  /**
   * Instantiates a {@link UIController} to manage the views associated with the game.
   *
   * @param primaryStage the {@link javafx.application.Application} primary {@link Stage}
   * @param gameController the {@link GameStateController} managing the game
   * @param inputConsumer the {@link HumanInputConsumer} to which keyboard input should be fed
   */
  public UIController(Stage primaryStage, GameStateController gameController,
      HumanInputConsumer inputConsumer) {
    // Configure Data Sources & Displays
    this.inputConsumer = inputConsumer;
    this.gameController = gameController;
    this.viewStack = new Stack<>();

    // initialize shared dependencies
    GraphicalExceptionService exceptionService = new GraphicalExceptionService();
    BundledLanguageService languageService = new BundledLanguageService(exceptionService);
    exceptionService.setLanguageService(languageService);
    SerializedThemeService themeService = new SerializedThemeService(exceptionService);
    AudioService audioService = new ThemedAudioService(themeService, exceptionService);
    this.serviceProvider =
        new ServiceProvider(exceptionService, audioService, themeService, languageService, this);
    this.preferenceService = new PreferenceService(themeService, languageService);

    // Stage Prep
    this.primaryStage = primaryStage;
    this.primaryStage.setScene(
        new Scene(
            new MenuView(this.serviceProvider, this).getRenderingNode(),
            DEFAULT_STAGE_SIZE,
            DEFAULT_STAGE_SIZE));
    this.primaryStage
        .titleProperty()
        .bind(this.serviceProvider.languageService().getLocalizedString("pacman"));
    this.viewStack.add(this.primaryStage.getScene());

    // Allow user interaction
    this.primaryStage.show();
  }

  @Override
  public void startGame() {
    this.gameView = new GameView(this.serviceProvider);
    showGameView();
    gameController.startGame(this.gameView);
  }

  /**
   *
   */
  @Override
  public void openLevelBuilder() {
    LevelBuilder builder = new LevelBuilder();
    showScene(
        new Scene(new LevelBuilderView(this.serviceProvider, builder).getRenderingNode()), true);
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

  public File requestUserFile(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new ExtensionFilter("JSON Files (*.json)", "*.json"));
    if (initialDirectory != null && initialDirectory.exists() && initialDirectory.isDirectory()) {
      fileChooser.setInitialDirectory(initialDirectory);
    }
    return fileChooser.showOpenDialog(new Stage());
  }

  public void handleException(String exceptionKey) {
    this.serviceProvider.exceptionService().handleWarning(new UIServicedException(exceptionKey));
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
    gameController.pauseGame();
    serviceProvider.audioService().stopAll();
    double oldWidth = primaryStage.getWidth();
    double oldHeight = primaryStage.getHeight();
    if (addToStack) {
      this.viewStack.push(this.primaryStage.getScene());
    }
    this.primaryStage.setScene(s);
    this.primaryStage.setWidth(oldWidth);
    this.primaryStage.setHeight(oldHeight);
    redirectInput(s);
  }
}
