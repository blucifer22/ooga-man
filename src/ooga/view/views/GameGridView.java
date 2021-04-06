package ooga.view.views;

import java.util.HashMap;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import ooga.model.TileCoordinates;
import ooga.model.api.ObservableGrid;
import ooga.model.api.ObservableTile;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.api.ObservableSprite;
import ooga.model.api.TileEvent.EventType;
import ooga.model.api.TileObserver;
import ooga.view.internal_api.View;
import ooga.view.theme.ThemeService;
import ooga.view.theme.ThemedObject;

/**
 * GameGridView lays out the grid and the Sprites on the grid (a necessary combination because only
 * the GameGridView knows where the grid is!).
 */
public class GameGridView implements View, SpriteExistenceObserver, ThemedObject {

  private final Group tileGrid;
  private final DoubleProperty tileSize;
  private final HashMap<ObservableSprite, SpriteView> spriteViews;
  private final Group spriteNodes;
  private final Pane primaryView;
  private ThemeService themeService;

  public GameGridView(int rows, int cols, ThemeService themeService) {
    this.primaryView = new Pane();
    this.tileGrid = new Group();
    this.tileSize = new SimpleDoubleProperty();
    this.tileSize.bind(Bindings.min(primaryView.widthProperty().divide(cols),
        primaryView.heightProperty().divide(rows)));

    this.spriteViews = new HashMap<>();
    this.spriteNodes = new Group();

    this.primaryView.getChildren().addAll(tileGrid, spriteNodes);

    setThemeService(themeService);

    addGridTiles(rows, cols);
  }

  public GameGridView(ObservableGrid grid, ThemeService themeService) {
    this(grid.getHeight(), grid.getWidth(), themeService);
  }

  private void addGridTiles(int rows, int cols) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        //TileView tv = new TileView(j, i, tileSize, this.themeService);
        int finalJ = j;
        int finalI = i;
        ObservableTile tile = new ObservableTile() {

          @Override
          public TileCoordinates getCoordinates() {
            return new TileCoordinates(finalJ, finalI);
          }

          @Override
          public String getType() {
            return "tile";
          }

          @Override
          public void addTileObserver(TileObserver observer, EventType... events) {
          }
        };
        TileView tv = new TileView(tile, tileSize, themeService);
        tileGrid.getChildren().add(tv.getRenderingNode());
      }
    }
  }

  @Override
  public void onSpriteCreation(ObservableSprite so) {
    SpriteView createdSpriteView = new SpriteView(so, this.themeService, tileSize);
    spriteViews.put(so, createdSpriteView);
    spriteNodes.getChildren().add(createdSpriteView.getRenderingNode());
  }

  @Override
  public void onSpriteDestruction(ObservableSprite so) {
    spriteNodes.getChildren().remove(spriteViews.get(so).getRenderingNode());
    spriteViews.remove(so);
  }

  @Override
  public Pane getRenderingNode() {
    return primaryView;
  }

  @Override
  public void onThemeChange() {
    // TODO: re-skin tiles!
  }

  @Override
  public void setThemeService(ThemeService themeService) {
    this.themeService = themeService;
    this.themeService.addThemedObject(this);
  }
}