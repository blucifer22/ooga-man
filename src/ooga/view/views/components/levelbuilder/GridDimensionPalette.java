package ooga.view.views.components.levelbuilder;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ooga.model.leveldescription.LevelBuilder;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.IntegerLockedSlider;
import ooga.view.views.components.StyledBoundLabel;

public class GridDimensionPalette extends StackPane {
  private final LevelBuilder levelBuilder;
  private int rows = 15;
  private int cols = 15;

  public GridDimensionPalette(UIServiceProvider serviceProvider, LevelBuilder levelBuilder) {
    this.levelBuilder = levelBuilder;
    IntegerLockedSlider rowDim = new IntegerLockedSlider(5, 25, 15);
    IntegerLockedSlider colDim = new IntegerLockedSlider(5, 25, 15);

    VBox paletteBox = new VBox(
        new StyledBoundLabel(serviceProvider.languageService().getLocalizedString(
            "dimensions"), "heading"),
        new StyledBoundLabel(serviceProvider.languageService().getLocalizedString(
            "rows"), "body"),
        rowDim,
        new StyledBoundLabel(serviceProvider.languageService().getLocalizedString(
            "columns"), "body"),
        colDim
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
