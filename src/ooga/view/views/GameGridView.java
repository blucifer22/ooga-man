package ooga.view.views;

import java.util.HashMap;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import ooga.model.TileCoordinates;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.ObservableGrid;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.api.ObservableSprite;
import ooga.view.internal_api.View;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

/**
 * GameGridView lays out the grid and the Sprites on the grid (a necessary combination because only
 * the GameGridView knows where the grid is!).
 */
public class GameGridView implements View, GridRebuildObserver, SpriteExistenceObserver,
    ThemedObject {

  private final Group tileGrid;
  private final DoubleProperty tileSize;
  private final HashMap<ObservableSprite, SpriteView> spriteViews;
  private final Group spriteNodes;
  private final Pane primaryView;
  private ThemeService themeService;

  public GameGridView(ThemeService themeService) {
    this.primaryView = new Pane();
    this.tileSize = new SimpleDoubleProperty();
    this.tileGrid = new Group();
    this.tileSize.bind(Bindings.min(primaryView.widthProperty(), primaryView.heightProperty()));

    this.spriteViews = new HashMap<>();
    this.spriteNodes = new Group();

    this.primaryView.getChildren().addAll(tileGrid, spriteNodes);

    setThemeService(themeService);
  }

  private void createTileGraphics(ObservableGrid grid) {
    for (int row = 0; row < grid.getHeight(); row++) {
      for (int col = 0; col < grid.getWidth(); col++) {
        TileView tv = new TileView(grid.getTile(new TileCoordinates(row, col)), tileSize,
            themeService);
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
  public void onGridRebuild(ObservableGrid grid) {
    this.tileGrid.getChildren().clear();
    this.tileSize.bind(Bindings.min(primaryView.widthProperty().divide(grid.getWidth()),
        primaryView.heightProperty().divide(grid.getHeight())));
    createTileGraphics(grid);
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