package ooga.model.api;

/**
 * An event affecting a tile.
 */
public class TileEvent {
  private final EventType type;

  /**
   * Construct a tile event.
   * @param type Type of event.
   */
  public TileEvent(EventType type) {
    this.type = type;
  }

  /**
   * Retrieve event type.
   * @return Type.
   */
  public EventType getEventType() {
    return type;
  }

  /**
   * Types of possible events.
   */
  public enum EventType {
    /**
     * Changing costume of a tile.
     */
    TYPE_CHANGE
  }
}
