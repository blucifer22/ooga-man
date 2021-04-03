package ooga.model;

import ooga.util.Vec2;

public interface SpriteObservable {

  String getType();

  SpriteCoordinates getCenter();

  Vec2 getDirection();

  boolean isVisible();

  void addObserver(SpriteObserver so, SpriteEvent.EventType... observedEvents);

  void removeObserver(SpriteObserver so);
}