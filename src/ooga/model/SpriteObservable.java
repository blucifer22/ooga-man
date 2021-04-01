package ooga.model;

public interface SpriteObservable {

  String getType();

  SpriteCoordinates getCenter();

  double getOrientation();

  boolean isVisible();

  void addObserver(SpriteObserver so, SpriteEvent.EventType... observedEvents);

  void removeObserver(SpriteObserver so);
}