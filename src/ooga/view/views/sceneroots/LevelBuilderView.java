package ooga.view.views.sceneroots;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import ooga.model.api.ObservableSprite;
import ooga.model.api.ObservableTile;
import ooga.model.leveldescription.LevelBuilder;
import ooga.model.leveldescription.LevelBuilder.BuilderState;
import ooga.model.leveldescription.Palette;
import ooga.view.internal_api.View;
import ooga.view.theme.api.ThemedObject;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.LabeledComboBoxCard;
import ooga.view.views.components.StyledButton;
import ooga.view.views.components.levelbuilder.GridDimensionPalette;

public class LevelBuilderView implements View, ThemedObject {
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

    this.configureConstraints();
    this.renderViews();
    this.refreshViews();

    this.serviceProvider.themeService().addThemedObject(this);
    this.onThemeChange();
  }

  private void configureConstraints() {
    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(70);
    this.primaryView.getRowConstraints().addAll(rc, new RowConstraints());

    ColumnConstraints cc = new ColumnConstraints();
    cc.setPercentWidth(50);
    this.primaryView.getColumnConstraints().addAll(cc, cc);
  }

  private void renderViews() {
    VBox buttonBox = new VBox(
        new StyledButton(this.serviceProvider, "next",
            e -> nextStep()),
        new StyledButton(this.serviceProvider, "mainMenu",
            e -> this.serviceProvider.viewStackManager().unwind())
    );

    this.primaryView.add(stageSwapPanel, 0, 0);
    this.primaryView.add(buttonBox, 0, 1);
    this.primaryView.add(this.tileGridView.getRenderingNode(), 1, 0, 1, 2);
    this.primaryView.getStyleClass().add("view");
  }

  private void nextStep() {
    this.levelBuilder.advanceState();
    this.refreshViews();
  }

  private void refreshViews() {
    this.stageSwapPanel.getChildren().clear();

    Node paletteChild = switch(this.levelBuilder.getBuilderState()) {
      case DIMENSIONING, TILING -> new GridDimensionPalette(this.serviceProvider,
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
      case DIMENSIONING, TILING -> levelBuilder.pokeTile(tileX, tileY);
      case SPRITE_PLACEMENT -> levelBuilder.addSprite(tileX, tileY);
    }
  }
}
