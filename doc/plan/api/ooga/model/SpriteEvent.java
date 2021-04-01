package ooga.model;

public class SpriteEvent {
  public enum EventType {
    TYPE_CHANGE, VISIBILITY, TRANSLATE, ROTATE, SCALE
  }

  public SpriteEvent(SpriteObservable sender, EventType type) {

  }

  public SpriteObservable getSender() {
    return null;
  }

  public EventType getEventType() {
    return null;
  }
}
