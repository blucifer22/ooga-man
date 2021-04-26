package ooga.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ooga.model.api.ObservableTile;
import ooga.model.api.TileEvent;
import ooga.model.api.TileEvent.EventType;
import ooga.model.api.TileObserver;

/**
 * Represents basic properties of a Tile such as whether they can be occupied
 *
 * @author George Hong
 * @author David Coffman
 */
public class Tile implements ObservableTile {

  private final TileCoordinates tileCoordinates;
  private final Map<EventType, Set<TileObserver>> observers;
  // TODO: allow for tile types to change
  private String tileType;
  private boolean isOpenToPacman;
  private boolean isOpenToGhosts;

  @JsonCreator
  public Tile(
      @JsonProperty("coordinates") TileCoordinates tileCoordinates,
      @JsonProperty("type") String initialType,
      @JsonProperty("openToPacman") boolean isOpenToPacman,
      @JsonProperty("openToGhosts") boolean isOpenToGhosts) {
    this.tileCoordinates = tileCoordinates;
    this.tileType = initialType;
    this.isOpenToPacman = isOpenToPacman;
    this.isOpenToGhosts = isOpenToGhosts;

    this.observers = new HashMap<>();
  }

  @JsonGetter
  public TileCoordinates getCoordinates() {
    return tileCoordinates;
  }

  @JsonGetter
  public boolean isOpenToPacman() {
    // true if pacman can move into this tile
    return isOpenToPacman;
  }

  @JsonGetter
  public boolean isOpenToGhosts() {
    return isOpenToGhosts;
  }

  @JsonGetter
  public String getType() {
    return tileType;
  }

  public void setType(String nextTileType) {
    tileType = nextTileType;
  }

  @Override
  public void addTileObserver(TileObserver observer, TileEvent.EventType... events) {
    if (events.length > 0) {
      // subscribe to only listed events, if any events are listed
      for (TileEvent.EventType ev : events) {
        observers.putIfAbsent(ev, new HashSet<>());
        observers.get(ev).add(observer);
      }
    } else {
      // otherwise, subscribe to all events
      for (TileEvent.EventType ev : TileEvent.EventType.values()) {
        observers.putIfAbsent(ev, new HashSet<>());
        observers.get(ev).add(observer);
      }
    }
  }

  public void setIsOpenToGhosts(Boolean bool) {
    isOpenToGhosts = bool;
  }

  public void setIsOpenToPacman(Boolean bool) {
    isOpenToPacman = bool;
  }
}
