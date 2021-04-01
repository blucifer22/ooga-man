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
    // handle the SpriteEvent
  }
}