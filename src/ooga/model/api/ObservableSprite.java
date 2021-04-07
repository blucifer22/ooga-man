package ooga.model.api;

import ooga.model.SpriteCoordinates;
import ooga.util.Vec2;

public interface ObservableSprite {

  String getType();

  SpriteCoordinates getCenter();

  Vec2 getDirection();

  boolean isVisible();

  void addObserver(SpriteObserver so, SpriteEvent.EventType... observedEvents);

  void removeObserver(SpriteObserver so);
}