package ooga.view.views.sceneroots;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ooga.model.api.AudioObserver;
import ooga.model.api.GameStateObservationComposite;
import ooga.model.api.GameStateObserver;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.SpriteExistenceObserver;
import ooga.view.internal_api.Renderable;
import ooga.view.internal_api.View;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.audioplayer.ViewBoundAudioPlayer;
import ooga.view.views.components.reusable.StyledButton;
import ooga.view.views.components.scenecomponents.ScoreboardCard;

/**
 * Renders the visual representation of a game round (the GridView in the center, information about
 * lives/round/score to the left). Sub-views respond to various aspects of game state: see
 * {@link GameGridView}, {@link ScoreboardCard}, and {@link ViewBoundAudioPlayer} for details on
 * game state response specifics; this class only serves to lay out the former on a grid.
 *
 * @author David Coffman
 */
public class GameView implements View, ThemedObject, GameStateObservationComposite {

  private final GridPane primaryView;
  private final GameGridView gridView;
  private final ScoreboardCard scoreboardCard;
  private final ViewBoundAudioPlayer audioPlayer;
  private final UIServiceProvider serviceProvider;

  /**
   * Sole constructor for {@link GameView}.
   *
   * @param serviceProvider the {@link UIServiceProvider} to provide UI services as desired
   */
  public GameView(UIServiceProvider serviceProvider) {
    this.primaryView = new GridPane();
    this.serviceProvider = serviceProvider;
    this.serviceProvider.themeService().addThemedObject(this);
    this.gridView = new GameGridView(this.serviceProvider.themeService());
    this.scoreboardCard = new ScoreboardCard(this.serviceProvider);
    this.audioPlayer = new ViewBoundAudioPlayer(serviceProvider.audioService());
    configureGridConstraints();
    addGridElements();
  }

  // Configures the GameView's grid constraints
  private void configureGridConstraints() {
    ColumnConstraints cc = new ColumnConstraints();
    cc.setPercentWidth(70);
    this.primaryView.getColumnConstraints().addAll(new ColumnConstraints(), cc);
    this.primaryView.setHgap(12.0);

    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(80);
    this.primaryView
        .getRowConstraints()
        .addAll(new RowConstraints(), rc, new RowConstraints(), new RowConstraints());

    this.primaryView.setAlignment(Pos.CENTER);
    this.primaryView.setBackground(
        new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    this.primaryView.getStyleClass().add("view");
  }

  // Initial configuration of grid elements.
  private void addGridElements() {
    this.primaryView.add(this.scoreboardCard.getRenderingNode(), 0, 0);
    this.primaryView.add(this.gridView.getRenderingNode(), 1, 0, 1, 3);

    VBox buttonBox =
        new VBox(
            new StyledButton(
                this.serviceProvider,
                "mainMenu",
                e -> this.serviceProvider.viewStackManager().unwind()));
    buttonBox.setAlignment(Pos.CENTER);

    this.primaryView.add(buttonBox, 0, 1);
  }

  /**
   * Returns this composite's {@link SpriteExistenceObserver}.
   *
   * @return this composite's {@link SpriteExistenceObserver}.
   */
  @Override
  public SpriteExistenceObserver spriteExistenceObserver() {
    return this.gridView;
  }

  /**
   * Returns this composite's {@link GridRebuildObserver}.
   *
   * @return this composite's {@link GridRebuildObserver}.
   */
  @Override
  public GridRebuildObserver gridRebuildObserver() {
    return this.gridView;
  }

  /**
   * Returns this composite's {@link AudioObserver}.
   *
   * @return this composite's {@link AudioObserver}.
   */
  @Override
  public AudioObserver audioObserver() {
    return this.audioPlayer;
  }

  /**
   * Returns this composite's {@link GameStateObserver}.
   *
   * @return this composite's {@link GameStateObserver}.
   */
  @Override
  public GameStateObserver gameStateObserver() {
    return this.scoreboardCard;
  }

  /**
   * Returns the {@link GameView}'s managed {@link Pane}. Stricter requirement than that of the
   * overridden method in {@link Renderable}, which guarantees only a {@link javafx.scene.Node}.
   *
   * @return the {@link GameView}'s managed {@link Pane}.
   */
  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }

  /**
   * Observer callback. Called when the theme changes. Implementing classes should re-query the
   * {@link ThemeService} for a new {@link Theme} when this method is called.
   */
  @Override
  public void onThemeChange() {
    this.primaryView.getStylesheets().clear();
    this.primaryView
        .getStylesheets()
        .add(this.serviceProvider.themeService().getTheme().getStylesheet());
  }
}
