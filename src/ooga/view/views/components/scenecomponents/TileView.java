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
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

public class TileView implements Renderable, TileObserver, ThemedObject {

  private final Rectangle tileRect;
  private final ObservableTile tile;
  private final ThemeService themeService;

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

  private void onTypeChange() {
    this.tileRect.setFill(
        themeService.getTheme().getCostumeForObjectOfType(this.tile.getType()).getFill());
  }

  @Override
  public void onThemeChange() {
    this.onTypeChange();
  }

  @Override
  public Node getRenderingNode() {
    return this.tileRect;
  }

  @Override
  public void onTileEvent(TileEvent e) {
    this.onTypeChange();
    // TODO: potentially add other types of tile event
  }
}
