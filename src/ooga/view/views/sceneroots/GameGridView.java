package ooga.view.views.sceneroots;

import java.util.HashMap;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.ObservableGrid;
import ooga.model.api.ObservableSprite;
import ooga.model.api.ObservableTile;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.grid.TileCoordinates;
import ooga.view.internal_api.View;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.views.components.scenecomponents.SpriteView;
import ooga.view.views.components.scenecomponents.TileView;

/**
 * Handles the rendering of the grid and the {@link SpriteView}s on the grid (a necessary
 * combination because only the {@link GameGridView} knows where the grid is). Handles resizing,
 * re-theming, {@link SpriteView} creation and destruction, and {@link ObservableGrid} rebuilds.
 *
 * @author David Coffman
 */
public class GameGridView
    implements View, GridRebuildObserver, SpriteExistenceObserver, ThemedObject {

  private final Group tileGrid;
  private final DoubleProperty tileSize;
  private final HashMap<ObservableSprite, SpriteView> spriteViews;
  private final Group spriteNodes;
  private final Pane primaryView;
  private final ThemeService themeService;
  private SpriteClickHandler spriteClickHandler;
  private TileClickHandler tileClickHandler;

  /**
   * Sole {@link GameGridView} constructor.
   *
   * @param themeService the {@link ThemeService} to query for {@link ooga.view.theme.api.Costume}s
   *                     and stylesheets
   */
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

  // Create tile graphics based on a supplied observable grid (through the observer interface).
  private void createTileGraphics(ObservableGrid grid) {
    for (int row = 0; row < grid.getHeight(); row++) {
      for (int col = 0; col < grid.getWidth(); col++) {
        ObservableTile tile = grid.getTile(new TileCoordinates(col, row));
        TileView tv = new TileView(tile, tileSize, themeService);
        tileGrid.getChildren().add(tv.getRenderingNode());
        tv.getRenderingNode()
            .setOnMouseClicked(
                e -> {
                  if (tileClickHandler != null) {
                    tileClickHandler.handle(e, tile);
                  }
                });
      }
    }
  }

  /**
   * Installs a {@link TileClickHandler} to observe clicks on tiles. Useful for interacting with
   * {@link ObservableTile} objects.
   *
   * @param tileClickHandler the {@link TileClickHandler} to observe tile clicks
   */
  public void setOnTileClicked(TileClickHandler tileClickHandler) {
    this.tileClickHandler = tileClickHandler;
  }

  /**
   * Installs a {@link SpriteClickHandler} to observe clicks on sprites. Useful for interacting with
   * {@link ObservableSprite} objects.
   *
   * @param spriteClickHandler the {@link SpriteClickHandler} to observe tile clicks
   */
  public void setOnSpriteClicked(SpriteClickHandler spriteClickHandler) {
    this.spriteClickHandler = spriteClickHandler;
  }

  /**
   * Called on creation of an {@link ObservableSprite}.
   *
   * @param so {@link ObservableSprite} that was created
   */
  @Override
  public void onSpriteCreation(ObservableSprite so) {
    SpriteView createdSpriteView = new SpriteView(so, this.themeService, tileSize);
    spriteViews.put(so, createdSpriteView);
    spriteNodes.getChildren().add(createdSpriteView.getRenderingNode());
    createdSpriteView
        .getRenderingNode()
        .setOnMouseClicked(
            e -> {
              spriteClickHandler.handle(e, so);
            });
  }

  /**
   * Called on destruction of an {@link ObservableSprite}.
   *
   * @param so {@link ObservableSprite} that was destroyed
   */
  @Override
  public void onSpriteDestruction(ObservableSprite so) {
    spriteNodes.getChildren().remove(spriteViews.get(so).getRenderingNode());
    spriteViews.remove(so);
  }

  /**
   * Called when a new grid should be displayed in place of an old grid.
   *
   * @param grid the post-rebuild grid
   */
  @Override
  public void onGridRebuild(ObservableGrid grid) {
    this.tileGrid.getChildren().clear();
    this.tileSize.bind(
        Bindings.min(
            primaryView.widthProperty().divide(grid.getWidth()),
            primaryView.heightProperty().divide(grid.getHeight())));
    createTileGraphics(grid);
  }

  /**
   * Returns the {@link GameGridView}'s managed {@link Pane}.
   *
   * @return the {@link GameGridView}'s managed {@link Pane}.
   */
  @Override
  public Pane getRenderingNode() {
    return primaryView;
  }

  /**
   * Observer callback. Called when the theme changes. Re-queries the {@link ThemeService} for a new
   * {@link Theme} when this method is called.
   */
  @Override
  public void onThemeChange() {
  }

  @FunctionalInterface
  public interface TileClickHandler {

    /**
     * Handler for tile click observation events.
     *
     * @param e    the {@link MouseEvent} associated with the tile click
     * @param tile the {@link ObservableTile} whose graphical representation was clicked
     */
    void handle(MouseEvent e, ObservableTile tile);
  }

  @FunctionalInterface
  public interface SpriteClickHandler {

    /**
     * Handler for sprite click observation events.
     *
     * @param e      the {@link MouseEvent} associated with the tile click
     * @param sprite the {@link ObservableSprite} whose graphical representation was clicked
     */
    void handle(MouseEvent e, ObservableSprite sprite);
  }
}
