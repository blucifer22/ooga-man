package ooga.model;

import ooga.model.SpriteEvent;

public interface SpriteObserver {

  void onSpriteUpdate(SpriteEvent e);
}