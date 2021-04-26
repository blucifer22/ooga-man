package ooga.model.api;

public class TileEvent {
  public enum EventType {
    TYPE_CHANGE
  }

  private final EventType type;

  public TileEvent(EventType type) {
    this.type = type;
  }

  public EventType getEventType() {
    return type;
  }
}
