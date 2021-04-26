package ooga.view.views.components.scenecomponents;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ooga.model.leveldescription.LevelBuilder;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.reusable.IntegerLockedSlider;
import ooga.view.views.components.reusable.StyledBoundLabel;

public class GridDimensionPalette extends StackPane {
  private final LevelBuilder levelBuilder;
  private static final int MIN_SIZE = 5;
  private static final int MAX_SIZE = 40;
  private static final int DEFAULT_SIZE = 20;
  private int rows = DEFAULT_SIZE;
  private int cols = DEFAULT_SIZE;

  public GridDimensionPalette(UIServiceProvider serviceProvider, LevelBuilder levelBuilder) {
    this.levelBuilder = levelBuilder;
    IntegerLockedSlider rowDim = new IntegerLockedSlider(MIN_SIZE, MAX_SIZE, DEFAULT_SIZE);
    IntegerLockedSlider colDim = new IntegerLockedSlider(MIN_SIZE, MAX_SIZE, DEFAULT_SIZE);

    VBox paletteBox = new VBox(
        new StyledBoundLabel(serviceProvider.languageService().getLocalizedString(
            "dimensions"), "heading"),
        new StyledBoundLabel(serviceProvider.languageService().getLocalizedString(
            "rows"), "body"),
        rowDim,
        new StyledBoundLabel(serviceProvider.languageService().getLocalizedString(
            "columns"), "body"),
        colDim,
        new StyledBoundLabel(serviceProvider.languageService().getLocalizedString(
            "tilingInstructions"), "heading").wrap(true)
    );

    this.getChildren().add(paletteBox);

    rowDim.addListener((newVal) -> updateLevelBuilder(newVal, cols));
    colDim.addListener((newVal) -> updateLevelBuilder(rows, newVal));
    updateLevelBuilder(rows, cols);
  }

  private void updateLevelBuilder(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    levelBuilder.setGridSize(rows, cols);
  }
}
