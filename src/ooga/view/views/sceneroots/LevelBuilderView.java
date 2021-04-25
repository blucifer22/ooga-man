package ooga.view.views.sceneroots;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import ooga.model.leveldescription.LevelBuilder;
import ooga.view.internal_api.View;
import ooga.view.theme.api.ThemedObject;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.StyledButton;
import ooga.view.views.components.levelbuilder.GridDimensionPalette;

public class LevelBuilderView implements  View, ThemedObject {
  private final LevelBuilder levelBuilder;
  private final UIServiceProvider serviceProvider;
  private final GameGridView tileGridView;
  private final GridPane primaryView;

  public LevelBuilderView(UIServiceProvider serviceProvider, LevelBuilder levelBuilder) {
    this.serviceProvider = serviceProvider;
    this.levelBuilder = levelBuilder;
    this.tileGridView = new GameGridView(this.serviceProvider.themeService());
    this.primaryView = new GridPane();
    this.levelBuilder.addGridRebuildObserver(tileGridView);
    this.levelBuilder.addSpriteExistenceObserver(tileGridView);

    this.configureConstraints();
    this.renderViews();

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
    GridDimensionPalette dimensionPalette = new GridDimensionPalette(this.serviceProvider,
        this.levelBuilder);

    VBox buttonBox = new VBox(
        new StyledButton(this.serviceProvider, "next",
            e -> System.out.println("next")),
        new StyledButton(this.serviceProvider, "mainMenu",
            e -> this.serviceProvider.viewStackManager().unwind())
    );

    this.primaryView.add(dimensionPalette, 0, 0);
    this.primaryView.add(buttonBox, 0, 1);
    this.primaryView.add(this.tileGridView.getRenderingNode(), 1, 0, 1, 2);
    this.primaryView.getStyleClass().add("view");
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
}
