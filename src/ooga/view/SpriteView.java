package ooga.view;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import ooga.model.SpriteObserver;
import ooga.model.SpriteEvent;
import ooga.model.SpriteObservable;

public class SpriteView implements SpriteObserver, Renderable {

  private final Rectangle viewGraphic;
  private final SpriteObservable dataSource;

  public SpriteView(SpriteObservable so) {
    so.addObserver(this);
    this.dataSource = so;
    this.viewGraphic = new Rectangle(100, 100);
  }

  public void onSpriteUpdate(SpriteEvent e) {
    switch (e.getEventType()) {
      case TYPE_CHANGE -> updateType();
      case TRANSLATE -> updatePosition();
      case SCALE -> updateInternalSize();
      case ROTATE -> updateOrientation();
      case VISIBILITY -> updateVisibility();
    }
  }

  private void updateType() {

  }

  private void updatePosition() {

  }

  private void updateOrientation() {
    viewGraphic.setRotate(dataSource.getOrientation());
  }

  private void updateInternalSize() {

  }

  private void updateVisibility() {
    viewGraphic.setVisible(dataSource.isVisible());
  }

  @Override
  public Node getRenderingNode() {
    return this.viewGraphic;
  }
}