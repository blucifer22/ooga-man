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
import ooga.view.internal_api.Renderable;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

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
    this.viewGraphic.setFill(themeService.getCostumeForObjectOfType(dataSource.getType()).getFill());
  }

  private void updatePosition() {
    SpriteCoordinates coordinates = dataSource.getCenter();
    this.viewGraphic.translateXProperty()
        .bind(size.multiply(coordinates.getPosition().getX()-0.5));
    this.viewGraphic.translateYProperty()
        .bind(size.multiply(coordinates.getPosition().getY()-0.5));
  }

  private void updateOrientation() {
    Vec2 direction = dataSource.getDirection();
    double rotation = Math.atan2(direction.getY(), direction.getX()) * 180.0 / Math.PI;
    if (rotation > 90 && rotation < 270) {
      this.viewGraphic.setScaleX(-1);
      rotation = ((rotation - 180) + 180) % 180;
    } else {
      this.viewGraphic.setScaleX(1);
    }
    viewGraphic.setRotate(rotation);
  }

  private void updateVisibility() {
    viewGraphic.setVisible(dataSource.isVisible());
  }

  @Override
  public Node getRenderingNode() {
    return this.viewGraphic;
  }
}