package ooga.view.views;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import ooga.model.api.ObservableTile;
import ooga.model.api.TileEvent;
import ooga.model.api.TileObserver;
import ooga.view.theme.ThemeService;
import ooga.view.theme.ThemedObject;

public class TileView implements Renderable, TileObserver, ThemedObject {

  private final Rectangle tileRect;
  private ThemeService themeService;

  public TileView(int gridX, int gridY, DoubleProperty tileSize, ThemeService themeService) {
    // Configure tile geometry
    this.tileRect = new Rectangle(0, 0, 0, 0);
    this.tileRect.widthProperty().bind(tileSize);
    this.tileRect.heightProperty().bind(tileSize);
    this.tileRect.layoutXProperty().bind(tileSize.multiply(gridX));
    this.tileRect.layoutYProperty().bind(tileSize.multiply(gridY));

    // DEBUG ONLY
    this.tileRect.setStroke(Color.PINK);
    this.tileRect.setStrokeWidth(1.0);
    this.tileRect.setStrokeType(StrokeType.INSIDE);
    // END DEBUG ONLY

    // Initial render
    this.setThemeService(themeService);
    this.onTypeChange();
  }

  public TileView(ObservableTile tile, DoubleProperty tileSize, ThemeService themeService) {
    this(tile.getCoordinates().getX(), tile.getCoordinates().getY(), tileSize, themeService);
  }

  private void onTypeChange() {
    this.tileRect.setFill(themeService.getFillForObjectOfType("tile"));
    // TODO: implement variable tile types
  }

  @Override
  public void onThemeChange() {
    this.onTypeChange();
  }

  @Override
  public void setThemeService(ThemeService themeService) {
    this.themeService = themeService;
    this.themeService.addThemedObject(this);
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
