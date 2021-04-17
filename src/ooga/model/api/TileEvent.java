package ooga.model.api;

import ooga.model.api.ObservableSprite;

public class TileEvent {
  public enum EventType {
    TYPE_CHANGE
  }

  private final ObservableTile sender;
  private final EventType type;

  public TileEvent(ObservableTile sender, EventType type) {
    this.sender = sender;
    this.type = type;
  }

  public ObservableTile getSender() {
    return sender;
  }

  public EventType getEventType() {
    return type;
  }
}
