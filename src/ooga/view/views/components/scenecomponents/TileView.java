package ooga.view.views.components.scenecomponents;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import ooga.model.api.ObservableTile;
import ooga.model.api.TileEvent;
import ooga.model.api.TileObserver;
import ooga.view.internal_api.Renderable;
import ooga.view.theme.api.Costume;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

/**
 * Handles the rendering of a single {@link TileView}, by responding (through observation) to
 * changes in its size and type. Also responds to theme changes and updates the view's {@link
 * Costume} accordingly.
 *
 * @author David Coffman
 */
public class TileView implements Renderable, TileObserver, ThemedObject {

  private final Rectangle tileRect;
  private final ObservableTile tile;
  private final ThemeService themeService;

  /**
   * Sole {@link TileView} constructor.
   *
   * @param tile         the {@link ObservableTile} to act as this view's data source
   * @param tileSize     the bound size of this tile
   * @param themeService the {@link ThemeService} to query for {@link Costume}s
   */
  public TileView(ObservableTile tile, DoubleProperty tileSize, ThemeService themeService) {
    // Configure data source
    this.tile = tile;
    this.tile.addTileObserver(this);

    // Configure tile geometry
    this.tileRect = new Rectangle(0, 0, 0, 0);
    this.tileRect.widthProperty().bind(tileSize);
    this.tileRect.heightProperty().bind(tileSize);
    this.tileRect.layoutXProperty().bind(tileSize.multiply(tile.getCoordinates().getX()));
    this.tileRect.layoutYProperty().bind(tileSize.multiply(tile.getCoordinates().getY()));

    // DEBUG ONLY
    this.tileRect.setStroke(Color.PINK);
    this.tileRect.setStrokeWidth(1.0);
    this.tileRect.setStrokeType(StrokeType.INSIDE);
    // END DEBUG ONLY

    // Initial render
    this.themeService = themeService;
    this.themeService.addThemedObject(this);
    this.onTypeChange();
  }

  // Re-sets this tile's fill when its type changes.
  private void onTypeChange() {
    this.tileRect.setFill(
        themeService.getTheme().getCostumeForObjectOfType(this.tile.getType()).getFill());
  }

  /**
   * Observer callback for {@link ThemedObject}s. Called when the theme changes. Re-queries the
   * {@link ThemeService} for a new {@link Theme} when this method is called.
   */
  @Override
  public void onThemeChange() {
    this.onTypeChange();
  }

  /**
   * Returns the {@link TileView}'s managed {@link Node}.
   *
   * @return the {@link TileView}'s managed {@link Node}.
   */
  @Override
  public Node getRenderingNode() {
    return this.tileRect;
  }

  /**
   * Called when a tile event occurs. Updates the {@link TileView}'s state to match that of the
   * observed {@link ObservableTile}.
   *
   * @param e Event that occurred.
   */
  @Override
  public void onTileEvent(TileEvent e) {
    this.onTypeChange();
  }
}
