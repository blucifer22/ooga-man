package ooga.model;

public class SpriteEvent {
  public enum EventType {
    TYPE_CHANGE, VISIBILITY, TRANSLATE, ROTATE
  }

  private final SpriteObservable sender;
  private final EventType type;

  public SpriteEvent(SpriteObservable sender, EventType type) {
    this.sender = sender;
    this.type = type;
  }

  public SpriteObservable getSender() {
    return sender;
  }

  public EventType getEventType() {
    return type;
  }
}
