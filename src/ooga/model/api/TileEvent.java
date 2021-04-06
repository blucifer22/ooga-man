package ooga.model.api;

import ooga.model.api.ObservableSprite;

public class TileEvent {
  public enum EventType {
    TYPE_CHANGE
  }

  private final ObservableSprite sender;
  private final EventType type;

  public TileEvent(ObservableSprite sender, EventType type) {
    this.sender = sender;
    this.type = type;
  }

  public ObservableSprite getSender() {
    return sender;
  }

  public EventType getEventType() {
    return type;
  }
}
