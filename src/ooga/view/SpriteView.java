package ooga.view;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ooga.model.SpriteCoordinates;
import ooga.model.SpriteObserver;
import ooga.model.SpriteEvent;
import ooga.model.SpriteObservable;

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
    this.viewGraphic = new Rectangle(50, 50, 50, 50);
    this.viewGraphic.setFill(Color.BLUE);
    //this.viewGraphic.widthProperty().bind(size);
    //this.viewGraphic.heightProperty().bind(size);
  }

  public void onSpriteUpdate(SpriteEvent e) {
    switch (e.getEventType()) {
      case TYPE_CHANGE -> updateType();
      case TRANSLATE -> updatePosition();
      case ROTATE -> updateOrientation();
      case VISIBILITY -> updateVisibility();
    }
  }

  private void updateType() {

  }

  private void updatePosition() {
    SpriteCoordinates coordinates = dataSource.getCenter();
    this.viewGraphic.translateXProperty().bind(size.multiply(coordinates.getTileCoordinates().getX()));
    this.viewGraphic.translateYProperty().bind(size.multiply(coordinates.getTileCoordinates().getY()));
  }

  private void updateOrientation() {
    viewGraphic.setRotate(dataSource.getOrientation());
  }

  private void updateVisibility() {
    viewGraphic.setVisible(dataSource.isVisible());
  }

  @Override
  public Node getRenderingNode() {
    return this.viewGraphic;
  }
}