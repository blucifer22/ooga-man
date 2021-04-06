package ooga.view.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import ooga.model.TileCoordinates;
import ooga.model.api.ObservableGrid;
import ooga.model.api.ObservableTile;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.api.TileEvent.EventType;
import ooga.model.api.TileObserver;
import ooga.view.internal_api.View;
import ooga.view.theme.ThemeService;
import ooga.view.theme.ThemedObject;

/**
 * GameView lays out how a round appears (the GridView in the center, information about
 * lives/round/score above and below).
 */
public class GameView implements View, ThemedObject {

  private final GridPane primaryView;
  private final GameGridView gridView;
  private ThemeService themeService;

  public GameView(ThemeService themeService) {
    this.primaryView = new GridPane();

    setThemeService(themeService);

    ObservableGrid grid = new ObservableGrid() {

      @Override
      public int getWidth() {
        return 10;
      }

      @Override
      public int getHeight() {
        return 10;
      }

      @Override
      public ObservableTile getTile(TileCoordinates tileCoordinates) {
        return new ObservableTile() {

          @Override
          public TileCoordinates getCoordinates() {
            return tileCoordinates;
          }

          @Override
          public String getType() {
            return "tile";
          }

          @Override
          public void addTileObserver(TileObserver observer, EventType... events) {
          }
        };
      }
    };

    this.gridView = new GameGridView(grid, this.themeService);


    ColumnConstraints cc = new ColumnConstraints();
    cc.setPercentWidth(80);

    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(80);

    this.primaryView.getRowConstraints().add(rc);
    this.primaryView.getColumnConstraints().add(cc);
    this.primaryView.add(this.gridView.getRenderingNode(), 0, 0);
    this.primaryView.setAlignment(Pos.CENTER);
    this.primaryView.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY,
        Insets.EMPTY)));
  }

  public SpriteExistenceObserver getSpriteExistenceObserver() {
    return this.gridView;
  }

  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }

  @Override
  public void onThemeChange() {
    // TODO: figure out if anything needs to change on a theme change
  }

  @Override
  public void setThemeService(ThemeService themeService) {
    this.themeService = themeService;
    this.themeService.addThemedObject(this);
  }
}
