package ooga.view;

import java.util.HashMap;
import java.util.Map;
import ooga.model.SpriteExistenceObserver;
import ooga.model.SpriteObservable;

public class GameView implements SpriteExistenceObserver {

  private Map<SpriteObservable, SpriteView> views;

  public GameView() {
    this.views = new HashMap<>();
  }

  public void onSpriteCreation(SpriteObservable so) {
    SpriteView sv = new SpriteView(so);
    views.put(so, sv);
    // render the SpriteView
  }

  public void onSpriteDestruction(SpriteObservable so) {
    views.remove(so);
    // de-render the SpriteView
  }
}