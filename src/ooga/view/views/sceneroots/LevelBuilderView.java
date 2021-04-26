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
import ooga.model.leveldescription.LevelBuilder;
import ooga.model.leveldescription.LevelBuilder.BuilderState;
import ooga.model.leveldescription.Palette;
import ooga.view.exceptions.UIServicedException;
import ooga.view.internal_api.View;
import ooga.view.theme.api.ThemedObject;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.reusable.LabeledComboBoxCard;
import ooga.view.views.components.reusable.StyledButton;
import ooga.view.views.components.scenecomponents.GridDimensionPalette;

public class LevelBuilderView implements View, ThemedObject {
  private static final int FULL_WIDTH = 100;
  private static final int SMALL_COLUMN_WIDTH = 40;
  private static final int LARGE_COLUMN_WIDTH = 60;
  private final VBox stageSwapPanel;
  private final Palette spritePalette;
  private final LevelBuilder levelBuilder;
  private final UIServiceProvider serviceProvider;
  private final GameGridView tileGridView;
  private final GridPane primaryView;

  public LevelBuilderView(UIServiceProvider serviceProvider, LevelBuilder levelBuilder) {
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

  private void renderViews() {
    VBox buttonBox = new VBox(
        stageSwapPanel,
        new StyledButton(this.serviceProvider, "next", e -> nextStep()),
        new StyledButton(this.serviceProvider, "mainMenu",
            e -> this.serviceProvider.viewStackManager().unwind())
    );
    buttonBox.setStyle("level-builder-panel;");
    buttonBox.setId("level-builder-panel");

    this.primaryView.add(buttonBox, 0, 0);
    this.primaryView.add(this.tileGridView.getRenderingNode(), 1, 0);
    this.primaryView.getStyleClass().add("view");
  }

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

  private void refreshViews() {
    this.stageSwapPanel.getChildren().clear();

    Node paletteChild = switch(this.levelBuilder.getBuilderState()) {
      case TILING -> new GridDimensionPalette(this.serviceProvider,
          this.levelBuilder);
      case SPRITE_PLACEMENT -> new LabeledComboBoxCard(this.serviceProvider, "sprites",
          this.spritePalette.getSpriteNames(), spritePalette::setActiveSprite);
    };

    this.stageSwapPanel.getChildren().add(paletteChild);
  }

  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }

  @Override
  public void onThemeChange() {
    this.primaryView.getStylesheets().clear();
    String stylesheet = this.serviceProvider.themeService().getTheme().getStylesheet();
    if (stylesheet != null) {
      this.primaryView.getStylesheets().add(stylesheet);
    }
  }

  private void onSpriteClick(MouseEvent e, ObservableSprite sprite) {
    int tileX = sprite.getCoordinates().getTileCoordinates().getX();
    int tileY = sprite.getCoordinates().getTileCoordinates().getY();

    switch (e.getButton()) {
      case PRIMARY -> levelBuilder.addSprite(tileX, tileY);
      case SECONDARY -> levelBuilder.clearSpritesOnTile(tileX, tileY);
    }
  }

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
