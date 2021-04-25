package ooga.view.views.sceneroots;

import java.util.HashMap;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import ooga.model.TileCoordinates;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.ObservableGrid;
import ooga.model.api.ObservableSprite;
import ooga.model.api.ObservableTile;
import ooga.model.api.SpriteExistenceObserver;
import ooga.view.internal_api.View;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.views.components.SpriteView;
import ooga.view.views.components.TileView;

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
  private final ThemeService themeService;
  private SpriteClickHandler spriteClickHandler;
  private TileClickHandler tileClickHandler;

  public GameGridView(ThemeService themeService) {
    this.primaryView = new Pane();
    this.tileSize = new SimpleDoubleProperty();
    this.tileGrid = new Group();
    this.tileSize.bind(Bindings.min(primaryView.widthProperty(), primaryView.heightProperty()));

    this.spriteViews = new HashMap<>();
    this.spriteNodes = new Group();

    this.primaryView.getChildren().addAll(tileGrid, spriteNodes);

    this.themeService = themeService;
    this.themeService.addThemedObject(this);
  }

  private void createTileGraphics(ObservableGrid grid) {
    for (int row = 0; row < grid.getHeight(); row++) {
      for (int col = 0; col < grid.getWidth(); col++) {
        ObservableTile tile = grid.getTile(new TileCoordinates(col, row));
        TileView tv = new TileView(tile, tileSize, themeService);
        tileGrid.getChildren().add(tv.getRenderingNode());
        tv.getRenderingNode().setOnMouseClicked(e -> {
          if (tileClickHandler != null) {
            tileClickHandler.handle(e, tile);
          }
        });
      }
    }
  }

  public void setOnTileClicked(TileClickHandler tileClickHandler) {
    this.tileClickHandler = tileClickHandler;
  }

  public void setOnSpriteClicked(SpriteClickHandler spriteClickHandler) {
    this.spriteClickHandler = spriteClickHandler;
  }

  @Override
  public void onSpriteCreation(ObservableSprite so) {
    SpriteView createdSpriteView = new SpriteView(so, this.themeService, tileSize);
    spriteViews.put(so, createdSpriteView);
    spriteNodes.getChildren().add(createdSpriteView.getRenderingNode());
    createdSpriteView.getRenderingNode().setOnMouseClicked(e -> {
      spriteClickHandler.handle(e, so);
    });
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

  @FunctionalInterface
  public interface TileClickHandler {
    void handle(MouseEvent e, ObservableTile tile);
  }

  @FunctionalInterface
  public interface SpriteClickHandler {
    void handle(MouseEvent e, ObservableSprite sprite);
  }

}