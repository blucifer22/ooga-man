package ooga.view;

import ooga.model.SpriteObserver;
import ooga.model.SpriteEvent;
import ooga.model.SpriteObservable;

public class SpriteView implements SpriteObserver {

  public SpriteView(SpriteObservable so) {
    so.addObserver(this);
    // create the view
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

  }

  private void updateInternalSize() {

  }

  private void updateVisibility() {

  }
}