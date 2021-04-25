package ooga.view.views.sceneroots;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.ObservableGrid;
import ooga.model.api.ObservableSprite;
import ooga.model.api.SpriteExistenceObserver;
import ooga.view.internal_api.View;
import ooga.view.theme.api.ThemedObject;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.StyledButton;

public class LevelBuilderView implements SpriteExistenceObserver, GridRebuildObserver, View,
    ThemedObject {
  private final UIServiceProvider serviceProvider;
  private final GameGridView tileGridView;
  private final GridPane primaryView;

  public LevelBuilderView(UIServiceProvider serviceProvider) {
    this.serviceProvider = serviceProvider;
    this.tileGridView = new GameGridView(this.serviceProvider.themeService());
    this.primaryView = new GridPane();

    this.configureConstraints();
    this.renderViews();

    this.serviceProvider.themeService().addThemedObject(this);
    this.onThemeChange();
  }

  private void configureConstraints() {
    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(100);
    this.primaryView.getRowConstraints().add(rc);

    ColumnConstraints cc = new ColumnConstraints();
    cc.setPercentWidth(50);
    this.primaryView.getColumnConstraints().addAll(cc, cc);
  }

  private void renderViews() {
    VBox paletteBox = new VBox(
        new Label("Set Dimensions"),
        new StyledButton(this.serviceProvider, "mainMenu",
            e -> this.serviceProvider.viewStackManager().unwind())
    );
    this.primaryView.add(paletteBox, 0, 0);
    this.primaryView.add(this.tileGridView.getRenderingNode(), 1, 0);
    this.primaryView.getStyleClass().add("view");
  }

  @Override
  public void onGridRebuild(ObservableGrid grid) {
    this.tileGridView.onGridRebuild(grid);
  }

  @Override
  public void onSpriteCreation(ObservableSprite so) {
    this.tileGridView.onSpriteCreation(so);
  }

  @Override
  public void onSpriteDestruction(ObservableSprite so) {
    this.tileGridView.onSpriteDestruction(so);
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
