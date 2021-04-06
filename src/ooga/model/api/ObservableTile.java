package ooga.model.api;

import ooga.model.TileCoordinates;
import ooga.model.api.TileEvent.EventType;

public interface ObservableTile {

  TileCoordinates getCoordinates();
  String getType();
  void addTileObserver(TileObserver observer, EventType... events);
}
