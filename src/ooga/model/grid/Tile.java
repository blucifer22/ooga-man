package ooga.model.grid;

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
 * Represents a Tile and its basic properties, including whether the Tile can be occupied by the
 * primary Sprite types.
 *
 * @author George Hong
 * @author David Coffman
 */
public class Tile implements ObservableTile {

  private final TileCoordinates tileCoordinates;
  private final Map<EventType, Set<TileObserver>> observers;
  private String tileType;
  private boolean isOpenToPacman;
  private boolean isOpenToGhosts;

  /**
   * Creates an instance of this class
   *
   * @param tileCoordinates position of this tile on the Grid
   * @param initialType     type of this tile, used for correctly rendering its appearance on the
   *                        grid.
   * @param isOpenToPacman  whether Pac-Man can occupy and enter this tile
   * @param isOpenToGhosts  whether ghosts can occupy and enter this tile.
   */
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

  /**
   * Gets the position of this Tile on the grid
   *
   * @return TileCoordinates object encoding the position of this Tile on the grid using integer
   * indices.
   */
  @JsonGetter
  public TileCoordinates getCoordinates() {
    return tileCoordinates;
  }

  /**
   * Returns whether this Tile is available for Pac-Man to enter
   *
   * @return boolean representing whether this Tile is available for Pac-Man to enter.
   */
  @JsonGetter
  public boolean isOpenToPacman() {
    // true if pacman can move into this tile
    return isOpenToPacman;
  }

  /**
   * Returns whether this Tile is available for Ghosts to enter
   *
   * @return boolean representing whether this Tile is available for Ghosts to enter.
   */
  @JsonGetter
  public boolean isOpenToGhosts() {
    return isOpenToGhosts;
  }

  /**
   * Returns the name of this Tile, based on its permeability properties, allowing Ghosts or Pac-Man
   * to enter, if possible.
   *
   * @return String name of this Tile.
   */
  @JsonGetter
  public String getType() {
    return tileType;
  }

  /**
   * Sets the name and type of this Tile, based on effects from the game.  This changes the Sprite
   * appearance.
   *
   * @param nextTileType name of the Tile type for this tile to adopt.
   */
  public void setType(String nextTileType) {
    tileType = nextTileType;
  }

  /**
   * Allows observers to be alerted of changes to this Tile through the observable-observer pattern.
   * Observers can be notified of the various changes possible to this tile, through type changes.
   *
   * @param observer Observer to be alerted of changes to this tile.
   * @param events   events for the observer to register to.
   */
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

  /**
   * Sets the availability of this Tile to ghosts
   *
   * @param bool boolean indicating whether a ghost should be able to enter this Tile
   */
  public void setIsOpenToGhosts(Boolean bool) {
    isOpenToGhosts = bool;
  }

  /**
   * Sets the availability of this Tile to Pac-Man
   *
   * @param bool boolean indicating whether Pac-Man should be able to enter this Tile
   */
  public void setIsOpenToPacman(Boolean bool) {
    isOpenToPacman = bool;
  }
}
