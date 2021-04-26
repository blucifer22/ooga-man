package ooga.model.api;

public class TileEvent {
  private final EventType type;

  public TileEvent(EventType type) {
    this.type = type;
  }

  public EventType getEventType() {
    return type;
  }

  public enum EventType {
    TYPE_CHANGE
  }
}
