package ooga.model.api;

import ooga.model.api.TileEvent.EventType;
import ooga.model.grid.TileCoordinates;

public interface ObservableTile {

  TileCoordinates getCoordinates();

  String getType();

  void addTileObserver(TileObserver observer, EventType... events);
}
