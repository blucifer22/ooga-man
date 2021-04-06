package ooga.view.views;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ooga.model.SpriteCoordinates;
import ooga.model.api.SpriteEvent;
import ooga.model.api.ObservableSprite;
import ooga.model.api.SpriteObserver;
import ooga.util.Vec2;
import ooga.view.theme.ThemeService;
import ooga.view.theme.ThemedObject;

/**
 * SpriteView handles the rendering of a single Sprite (SpriteObservable, technically).
 */
public class SpriteView implements SpriteObserver, ThemedObject, Renderable {

  private final Rectangle viewGraphic;
  private final ObservableSprite dataSource;
  private final DoubleProperty size;
  private ThemeService themeService;

  public SpriteView(ObservableSprite so, ThemeService themeService, DoubleProperty size) {
    // configure data sourcing
    so.addObserver(this);
    this.dataSource = so;
    this.size = size;

    // render
    this.viewGraphic = new Rectangle();
    this.viewGraphic.setFill(Color.BLUE);
    this.viewGraphic.widthProperty().bind(size);
    this.viewGraphic.heightProperty().bind(size);

    // theme service
    setThemeService(themeService);

    // initial positioning
    updateType();
    updatePosition();
    updateOrientation();
    updateVisibility();
  }

  @Override
  public void onSpriteUpdate(SpriteEvent e) {
    switch (e.getEventType()) {
      case TYPE_CHANGE -> updateType();
      case TRANSLATE -> updatePosition();
      case ROTATE -> updateOrientation();
      case VISIBILITY -> updateVisibility();
    }
  }

  @Override
  public void onThemeChange() {
    updateType();
  }

  @Override
  public void setThemeService(ThemeService themeService) {
    this.themeService = themeService;
    this.themeService.addThemedObject(this);
  }

  private void updateType() {
    this.viewGraphic.setFill(themeService.getFillForObjectOfType(dataSource.getType()));
  }

  private void updatePosition() {
    SpriteCoordinates coordinates = dataSource.getCenter();
    this.viewGraphic.translateXProperty()
        .bind(size.multiply(coordinates.getExactCoordinates().getX()));
    this.viewGraphic.translateYProperty()
        .bind(size.multiply(coordinates.getExactCoordinates().getY()));
  }

  private void updateOrientation() {
    Vec2 direction = dataSource.getDirection();
    viewGraphic.setRotate(Math.atan2(direction.getY(), direction.getX()) * 180.0 / Math.PI);
  }

  private void updateVisibility() {
    viewGraphic.setVisible(dataSource.isVisible());
  }

  @Override
  public Node getRenderingNode() {
    return this.viewGraphic;
  }
}