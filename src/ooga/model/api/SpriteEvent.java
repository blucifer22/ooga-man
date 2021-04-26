package ooga.model.api;

public class SpriteEvent {
  private final ObservableSprite sender;
  private final EventType type;

  public SpriteEvent(ObservableSprite sender, EventType type) {
    this.sender = sender;
    this.type = type;
  }

  public ObservableSprite getSender() {
    return sender;
  }

  public EventType getEventType() {
    return type;
  }

  public enum EventType {
    TYPE_CHANGE,
    VISIBILITY,
    TRANSLATE,
    ROTATE
  }
}
