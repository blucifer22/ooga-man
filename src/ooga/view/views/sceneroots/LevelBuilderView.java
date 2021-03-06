package ooga.view.views.sceneroots;

import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ooga.model.api.ObservableSprite;
import ooga.model.api.ObservableTile;
import ooga.model.leveldescription.LevelBuilder.BuilderState;
import ooga.model.leveldescription.LevelEditor;
import ooga.model.leveldescription.Palette;
import ooga.view.exceptions.UIServicedException;
import ooga.view.internal_api.View;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.reusable.LabeledComboBoxCard;
import ooga.view.views.components.reusable.StyledButton;
import ooga.view.views.components.scenecomponents.GridDimensionPalette;

/**
 * Primary view for interfacing with a {@link LevelEditor}. Allows the user to resize a game grid,
 * add or remove sprites of various types, and eventually save the grid to a new level file.
 *
 * @author David Coffman
 */
public class LevelBuilderView implements View, ThemedObject {

  private static final int FULL_WIDTH = 100;
  private static final int SMALL_COLUMN_WIDTH = 40;
  private static final int LARGE_COLUMN_WIDTH = 60;
  private final VBox stageSwapPanel;
  private final Palette spritePalette;
  private final LevelEditor levelBuilder;
  private final UIServiceProvider serviceProvider;
  private final GameGridView tileGridView;
  private final GridPane primaryView;

  /**
   * Sole constructor for {@link LevelBuilderView}.
   *
   * @param serviceProvider the {@link UIServiceProvider} to provide services as desired
   * @param levelBuilder    the {@link LevelEditor} to poke in response to user interaction
   */
  public LevelBuilderView(UIServiceProvider serviceProvider, LevelEditor levelBuilder) {
    this.serviceProvider = serviceProvider;
    this.levelBuilder = levelBuilder;
    this.spritePalette = levelBuilder.getPalette();
    this.tileGridView = new GameGridView(this.serviceProvider.themeService());
    this.tileGridView.setOnSpriteClicked(this::onSpriteClick);
    this.tileGridView.setOnTileClicked(this::onTileClick);

    this.primaryView = new GridPane();
    this.levelBuilder.addGridRebuildObserver(tileGridView);
    this.levelBuilder.addSpriteExistenceObserver(tileGridView);
    this.stageSwapPanel = new VBox();
    this.stageSwapPanel.setId("stage-swap-panel");

    this.configureConstraints();
    this.renderViews();
    this.refreshViews();

    this.serviceProvider.themeService().addThemedObject(this);
    this.onThemeChange();
  }

  // Configure the level builder primary view's GridPane constraints.
  private void configureConstraints() {
    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(FULL_WIDTH);
    this.primaryView.getRowConstraints().addAll(rc);

    ColumnConstraints cc40 = new ColumnConstraints();
    ColumnConstraints cc60 = new ColumnConstraints();
    cc40.setPercentWidth(SMALL_COLUMN_WIDTH);
    cc60.setPercentWidth(LARGE_COLUMN_WIDTH);
    this.primaryView.getColumnConstraints().addAll(cc40, cc60);
  }

  // Render the button box and swappable panes.
  private void renderViews() {
    VBox buttonBox = new VBox(
        stageSwapPanel,
        new StyledButton(this.serviceProvider, "next", e -> nextStep()),
        new StyledButton(this.serviceProvider, "mainMenu",
            e -> this.serviceProvider.viewStackManager().unwind())
    );
    buttonBox.getStyleClass().add("level-builder-panel");
    buttonBox.setId("level-builder-panel");

    this.primaryView.add(buttonBox, 0, 0);
    this.primaryView.add(this.tileGridView.getRenderingNode(), 1, 0);
    this.primaryView.getStyleClass().add("view");
  }

  // Advance to next level builder step.
  private void nextStep() {
    if (this.levelBuilder.getBuilderState() == BuilderState.SPRITE_PLACEMENT) {
      try {
        this.levelBuilder.writeToJSON((new FileChooser()).showSaveDialog(new Stage()));
      } catch (IOException | NullPointerException e) {
        this.serviceProvider.exceptionService().handleWarning(new UIServicedException(
            "fileSaveError"));
      }
    } else {
      this.levelBuilder.advanceState();
      this.refreshViews();
    }
  }

  // Refresh view components when the level builder advances to the next step.
  private void refreshViews() {
    this.stageSwapPanel.getChildren().clear();

    Node paletteChild = switch (this.levelBuilder.getBuilderState()) {
      case TILING -> new GridDimensionPalette(this.serviceProvider,
          this.levelBuilder);
      case SPRITE_PLACEMENT -> new LabeledComboBoxCard(this.serviceProvider, "sprites",
          this.spritePalette.getSpriteNames(), spritePalette::setActiveSprite);
    };

    this.stageSwapPanel.getChildren().add(paletteChild);
  }

  /**
   * Returns the {@link LevelBuilderView}'s managed {@link Pane}.
   *
   * @return the {@link LevelBuilderView}'s managed {@link Pane}.
   */
  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }

  /**
   * Observer callback. Called when the theme changes. Re-queries the {@link ThemeService} for a new
   * {@link Theme} when this method is called.
   */
  @Override
  public void onThemeChange() {
    this.primaryView.getStylesheets().clear();
    String stylesheet = this.serviceProvider.themeService().getTheme().getStylesheet();
    if (stylesheet != null) {
      this.primaryView.getStylesheets().add(stylesheet);
    }
  }

  // Sprite click handler supplied to GameGridView
  private void onSpriteClick(MouseEvent e, ObservableSprite sprite) {
    int tileX = sprite.getCoordinates().getTileCoordinates().getX();
    int tileY = sprite.getCoordinates().getTileCoordinates().getY();

    switch (e.getButton()) {
      case PRIMARY -> levelBuilder.addSprite(tileX, tileY);
      case SECONDARY -> levelBuilder.clearSpritesOnTile(tileX, tileY);
    }
  }

  // Tile click handler supplied to GameGridView
  private void onTileClick(MouseEvent e, ObservableTile tile) {
    BuilderState builderState = levelBuilder.getBuilderState();
    int tileX = tile.getCoordinates().getX();
    int tileY = tile.getCoordinates().getY();

    switch (builderState) {
      case TILING -> levelBuilder.pokeTile(tileX, tileY);
      case SPRITE_PLACEMENT -> levelBuilder.addSprite(tileX, tileY);
    }
  }
}
