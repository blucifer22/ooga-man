package ooga.model;

public class TileEvent {
  public enum EventType {
    TYPE_CHANGE
  }

  private final SpriteObservable sender;
  private final EventType type;

  public TileEvent(SpriteObservable sender, EventType type) {
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
