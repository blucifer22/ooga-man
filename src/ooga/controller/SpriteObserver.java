package ooga.controller;

import ooga.model.SpriteEvent;

public interface SpriteObserver {

  void onSpriteUpdate(SpriteEvent e);
}