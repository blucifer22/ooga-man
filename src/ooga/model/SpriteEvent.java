package ooga.model;

public class SpriteEvent {
  public enum EventType {
    TYPE_CHANGE, VISIBILITY, TRANSLATE, ROTATE
  }

  private SpriteObservable sender;
  private EventType type;

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
