package ooga.view;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ooga.model.SpriteCoordinates;
import ooga.model.SpriteEvent;
import ooga.model.SpriteObservable;
import ooga.model.SpriteObserver;
import ooga.util.Vec2;

/**
 * SpriteView handles the rendering of a single Sprite (SpriteObservable, technically).
 */
public class SpriteView implements SpriteObserver, Renderable {

  private final Rectangle viewGraphic;
  private final SpriteObservable dataSource;
  private final DoubleProperty size;

  public SpriteView(SpriteObservable so, DoubleProperty tileSize) {
    // configure data sourcing
    so.addObserver(this);
    this.dataSource = so;
    this.size = tileSize;

    // render
    this.viewGraphic = new Rectangle();
    this.viewGraphic.setFill(Color.BLUE);
    this.viewGraphic.widthProperty().bind(size);
    this.viewGraphic.heightProperty().bind(size);

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

  private void updateType() {
    // TODO: sprite graphics as Rectangle fill
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
    viewGraphic.setRotate(Math.atan2(direction.getY(), direction.getX())*180.0/Math.PI);
  }

  private void updateVisibility() {
    viewGraphic.setVisible(dataSource.isVisible());
  }

  @Override
  public Node getRenderingNode() {
    return this.viewGraphic;
  }
}