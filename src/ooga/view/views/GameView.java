package ooga.view.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ooga.ViewStackManager;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.SpriteExistenceObserver;
import ooga.view.internal_api.View;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

/**
 * GameView lays out how a round appears (the GridView in the center, information about
 * lives/round/score above and below).
 */
public class GameView implements View, ThemedObject {

  private final GridPane primaryView;
  private final GameGridView gridView;
  private final ViewStackManager viewStackManager;
  private ThemeService themeService;

  public GameView(ThemeService themeService, ViewStackManager viewStackManager) {
    this.primaryView = new GridPane();

    this.themeService = themeService;
    this.themeService.addThemedObject(this);
    this.viewStackManager = viewStackManager;

    this.gridView = new GameGridView(this.themeService);

    ColumnConstraints cc = new ColumnConstraints();
    cc.setPercentWidth(80);

    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(80);

    this.primaryView.getRowConstraints().addAll(new RowConstraints(), rc, new RowConstraints());
    this.primaryView.getColumnConstraints().addAll(new ColumnConstraints(), cc,
        new ColumnConstraints());
    this.primaryView.add(this.gridView.getRenderingNode(), 1, 1);
    this.primaryView.setAlignment(Pos.CENTER);
    this.primaryView.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY,
        Insets.EMPTY)));
    this.primaryView.getStyleClass().add("view");

    Button backButton = new Button("Main Menu");
    backButton.getStyleClass().add("menu-button");
    backButton.setOnMouseClicked(e -> viewStackManager.unwind());

    VBox backButtonBox = new VBox(
      backButton
    );
    backButtonBox.setAlignment(Pos.CENTER);

    this.primaryView.add(backButtonBox, 1, 2, 3, 1);

  }

  public SpriteExistenceObserver getSpriteExistenceObserver() {
    return this.gridView;
  }

  public GridRebuildObserver getGridRebuildObserver() {
    return this.gridView;
  }

  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }

  @Override
  public void onThemeChange() {
    this.primaryView.getStylesheets().clear();
    this.primaryView.getStylesheets().add(themeService.getTheme().getStylesheet());
  }

}
